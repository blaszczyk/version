package bn.blaszczyk.version.plugin;

import java.io.File;
import java.util.Map;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import bn.blaszczyk.version.VersionException;
import bn.blaszczyk.version.model.Project;
import bn.blaszczyk.version.tools.GitTools;
import bn.blaszczyk.version.tools.JavaTools;
import bn.blaszczyk.version.tools.PomTools;


@Mojo(name="update")
public class UpdateMojo extends AbstractMojo {
	
	public static final String VERSION = "0.0.2";
	
	@Parameter
	private File versionJavaFile;
	
	@Parameter
	private String versionVariable;
	
	@Parameter
	private String versionPrefix;
	
	@Parameter
	private int skipCommits;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException
	{
		try
		{
			final Map<?,?> context = getPluginContext();
			final MavenProject mvnProject = (MavenProject) context.get("project");
			final File baseDir = mvnProject.getBasedir();
			final String version = generateVersion(baseDir);
			final String artifactId = mvnProject.getArtifactId();
			final String groupId = mvnProject.getGroupId();
			final Project currentProject = new Project(groupId, artifactId, version);
			updateVersionInJava(version);
			updatePoms(baseDir, currentProject);			
		}
		catch (VersionException e)
		{
			throw new MojoFailureException("update mojo failed", e);
		}
		catch (Exception e)
		{
			throw new MojoExecutionException("error executing update mojo",e);
		}
	}
	
	private void updateVersionInJava(final String version) throws VersionException
	{
		final boolean updated = JavaTools.updateVersionInJava(versionJavaFile, versionVariable, version);
		if(updated)
			getLog().info("updated " + versionVariable + " in " + versionJavaFile);
	}

	private String generateVersion(final File baseDir) throws VersionException
	{
		final int commitCount = GitTools.countCommits(baseDir);
		getLog().info("commit count: " + commitCount);
		if(skipCommits > commitCount)
			throw new VersionException(String.format("Unable to skip %d commits. Total count is %d.", skipCommits, commitCount));
		final String version = String.format("%s.%d", versionPrefix, commitCount - skipCommits);
		getLog().info("generated version: " + version);
		return version;
	}
	
	private void updatePoms(final File directory, final Project project) throws VersionException
	{
		final File parentDirectory = directory.getParentFile();
		if(PomTools.isMavenBaseDirectory(parentDirectory))
			updatePoms(parentDirectory, project);
		else
			updatePomsRecursive(directory, project);
	}

	private void updatePomsRecursive(final File directory, final Project project) throws VersionException
	{
		if(PomTools.isMavenBaseDirectory(directory))
		{
			final boolean updated = PomTools.updatePomRecursive(directory, project);
			if(updated)
				getLog().info("updated pom in " + directory.getAbsolutePath());
			for(final File file : directory.listFiles())
				if(file.isDirectory() && PomTools.isMavenBaseDirectory(file))
					updatePomsRecursive(file, project);
		}		
	}

}
