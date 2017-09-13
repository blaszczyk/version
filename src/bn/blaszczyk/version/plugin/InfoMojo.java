package bn.blaszczyk.version.plugin;

import org.apache.maven.plugins.annotations.Mojo;

import bn.blaszczyk.version.VersionException;
import bn.blaszczyk.version.model.Project;


@Mojo(name="info")
public class InfoMojo extends AbstractUpdateMojo
{
	
	@Override
	void executeUpdate(final Project project) throws VersionException
	{
		//noop
	}
	
}
