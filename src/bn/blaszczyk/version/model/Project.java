package bn.blaszczyk.version.model;

public class Project
{
	private final String groupId;
	private final String artifactId;
	private final String version;
	
	public Project(final String groupId, final String artifactId, final String version)
	{
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
	}

	public final String getGroupId() {
		return groupId;
	}

	public final String getArtifactId() {
		return artifactId;
	}

	public final String getVersion() {
		return version;
	}
	
}
