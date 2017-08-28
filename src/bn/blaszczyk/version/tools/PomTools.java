package bn.blaszczyk.version.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import bn.blaszczyk.version.VersionException;
import bn.blaszczyk.version.model.Project;

public class PomTools
{
	private static final Namespace POM_NS = Namespace.getNamespace("http://maven.apache.org/POM/4.0.0");
	
	private static final String GROUP_ID = "groupId";
	private static final String ARTIFACT_ID = "artifactId";
	private static final String VERSION = "version";
	
	private PomTools() {
	}
	
	public static boolean isMavenBaseDirectory(final File directory)
	{
		final File pomFile = pomContainedIn(directory);
		return pomFile.exists() && pomFile.isFile();
	}
	
	public static boolean updatePomRecursive(final File directory, final Project project) throws VersionException
	{
		final Document document = parsePom(directory);
		final Element root = document.getRootElement();
		final boolean hasChanged = updateRecursive(root,project);
		if(hasChanged)
			writePom(document, directory);
		return hasChanged;
	}
	
	private static Document parsePom(final File directory) throws VersionException
	{
		try
		{
			final File pomFile = pomContainedIn(directory);
			final SAXBuilder builder = new SAXBuilder();
			final Document document = builder.build(pomFile);
			return document;
		}
		catch(IOException | JDOMException e)
		{
			throw new VersionException("Error parsing pom.xml in directory " + directory, e);
		}
	}
		
	private static boolean updateRecursive(final Element element, final Project project)
	{
		boolean updated = false;
		if(matchesProject(element, project))
		{
			updateVersion(element, project.getVersion());
			updated = true;
		}
		for(final Element child : element.getChildren())
			updated |= updateRecursive(child, project);
		return updated;
	}

	private static boolean matchesProject(final Element element, final Project project)
	{
		final Element groupId = element.getChild(GROUP_ID, POM_NS);
		final Element artifactId = element.getChild(ARTIFACT_ID, POM_NS);
		final Element version = element.getChild(VERSION, POM_NS);
		if(groupId == null || artifactId == null || version == null)
			return false;
		return groupId.getValue().equals(project.getGroupId())
				&& artifactId.getValue().equals(project.getArtifactId());
	}
	
	private static void updateVersion(final Element element, final String version)
	{
		final Element versionElement = element.getChild(VERSION, POM_NS);
		versionElement.setText(version);
	}
	
	private static void writePom(final Document document, final File baseFolder) throws VersionException
	{
		final File pomFile = pomContainedIn(baseFolder);
		final XMLOutputter outPutter = new XMLOutputter(Format.getPrettyFormat());
		try
		{
			outPutter.output(document, new FileWriter(pomFile));
		}
		catch(IOException e)
		{
			throw new VersionException("Error parsing pom.xml in directory " + baseFolder, e);
		}
	}

	private static File pomContainedIn(final File directory)
	{
		return new File(directory, "pom.xml");
	}

}
