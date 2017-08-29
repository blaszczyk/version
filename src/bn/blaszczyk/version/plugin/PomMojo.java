package bn.blaszczyk.version.plugin;

import org.apache.maven.plugins.annotations.Mojo;

import bn.blaszczyk.version.VersionException;
import bn.blaszczyk.version.model.Project;


@Mojo(name="pom")
public class PomMojo extends AllMojo {
	
	@Override
	void executeUpdate(final Project project) throws VersionException
	{
		updatePoms(project);
	}
	
}
