package bn.blaszczyk.version.plugin;

import org.apache.maven.plugins.annotations.Mojo;

import bn.blaszczyk.version.VersionException;
import bn.blaszczyk.version.model.Project;


@Mojo(name="java")
public class JavaMojo extends AllMojo {
	
	@Override
	void executeUpdate(final Project project) throws VersionException
	{
		updateVersionInJava(project.getVersion());
	}
	
}
