package bn.blaszczyk.version.model;

import java.io.File;

public class Project
{
	private final String groupId;
	private final String artifactId;
	private final String version;
	private final File baseDir;
	
	public Project(final String groupId, final String artifactId, final String version, final File baseDir)
	{
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
		this.baseDir = baseDir;
	}

	public final String getGroupId()
	{
		return groupId;
	}

	public final String getArtifactId()
	{
		return artifactId;
	}

	public final String getVersion()
	{
		return version;
	}
	
	public final File getBaseDir()
	{
		return baseDir;
	}
	
}
