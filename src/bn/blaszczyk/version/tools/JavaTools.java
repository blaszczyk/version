package bn.blaszczyk.version.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.List;

import bn.blaszczyk.version.VersionException;

public class JavaTools
{

	public static void updateVersionInJava(final File versionJavaFile, final String versionVariable, final String version) throws VersionException
	{
		try
		{
			final List<String> lines = Files.readAllLines(versionJavaFile.toPath());
			for(int i = 0; i < lines.size(); i++)
			{
				final String line = lines.get(i);
				final String varEquals = versionVariable + " = ";
				if(line.contains(varEquals))
				{
					final int endIndex = line.lastIndexOf(varEquals) + varEquals.length();
					final String newVersion = line.substring(0, endIndex) + "\"" + version + "\";";
					lines.set(i, newVersion);
					break;
				}
			}
			try(final Writer writer = new FileWriter(versionJavaFile))
			{
				for(final String line : lines)
					writer.write(line + "\r\n");				
			}
		}
		catch(IOException e)
		{
			throw new VersionException("Error updating Version Java File", e);
		}
	}
	
}
