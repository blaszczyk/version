package bn.blaszczyk.version.plugin;

import java.io.File;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import bn.blaszczyk.version.VersionException;
import bn.blaszczyk.version.model.Project;
import bn.blaszczyk.version.tools.JavaTools;
import bn.blaszczyk.version.tools.PomTools;


@Mojo(name="all")
public class AllMojo extends AbstractUpdateMojo
{
	@Parameter
	File versionJavaFile;
	
	@Parameter
	String versionVariable;
	
	@Override
	void executeUpdate(final Project project) throws VersionException
	{
		updatePoms(project);
		updateVersionInJava(project.getVersion());
	}
	
	void updateVersionInJava(final String version) throws VersionException
	{
		if(versionJavaFile == null || versionVariable == null)
		{
			getLog().info("java file or variable not specified");
			return;
		}
		final boolean updated = JavaTools.updateVersionInJava(versionJavaFile, versionVariable, version);
		if(updated)
			getLog().info("updated " + versionVariable + " in " + versionJavaFile);
	}
	
	void updatePoms(final Project project) throws VersionException
	{
		File directory = project.getBaseDir();
		while(PomTools.containsPom(directory.getParentFile()))
			directory = directory.getParentFile();
		updatePomsRecursive(directory, project);
	}

	private void updatePomsRecursive(final File directory, final Project project) throws VersionException
	{
		final boolean updated = PomTools.updatePomRecursive(directory, project);
		if(updated)
			getLog().info("updated pom in " + directory.getAbsolutePath());
		for(final File file : directory.listFiles())
			if(file.isDirectory() && PomTools.containsPom(file))
				updatePomsRecursive(file, project);
	}

}
