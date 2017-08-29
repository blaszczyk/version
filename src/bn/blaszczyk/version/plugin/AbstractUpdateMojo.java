package bn.blaszczyk.version.plugin;

import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import bn.blaszczyk.version.VersionException;
import bn.blaszczyk.version.model.Project;
import bn.blaszczyk.version.tools.GitTools;


public abstract class AbstractUpdateMojo extends AbstractMojo {
	
	public static final String VERSION = "0.0.6";
	
	@Parameter
	private String versionPrefix;
	
	@Parameter(defaultValue="0")
	private int skipCommits;

	@Parameter
	File versionJavaFile;
	
	@Parameter
	String versionVariable;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException
	{
		if(versionPrefix == null)
		{
			getLog().info("version update not configured (maybe versionPrefix missing?)");
			return;
		}
		try
		{
			final MavenProject mvnProject = (MavenProject) getPluginContext().get("project");
			
			final File baseDir = mvnProject.getBasedir();
			final String artifactId = mvnProject.getArtifactId();
			final String groupId = mvnProject.getGroupId();
			
			final String version = generateVersion(baseDir);
			
			final Project currentProject = new Project(groupId, artifactId, version, baseDir);
			
			executeUpdate(currentProject);
		}
		catch (VersionException e)
		{
			throw new MojoFailureException("version update failed", e);
		}
		catch (Exception e)
		{
			throw new MojoExecutionException("error executing version update",e);
		}
	}
	
	abstract void executeUpdate(final Project project) throws VersionException;

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

}
