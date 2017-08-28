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
	public static boolean updateVersionInJava(final File versionJavaFile, final String versionVariable, final String version) throws VersionException
	{
		try
		{
			boolean updated = false;
			final List<String> lines = Files.readAllLines(versionJavaFile.toPath());
			for(int i = 0; i < lines.size(); i++)
			{
				final String line = lines.get(i);
				final String varEquals = versionVariable + " = ";
				if(line.contains(varEquals))
				{
					final int endIndex = line.lastIndexOf(varEquals) + varEquals.length();
					final String newVersionLine = line.substring(0, endIndex) + "\"" + version + "\";";
					if(line.equals(newVersionLine))
						return false;
					lines.set(i, newVersionLine);
					updated = true;
					break;
				}
			}
			if(updated)
				try(final Writer writer = new FileWriter(versionJavaFile))
				{
					for(final String line : lines)
						writer.write(line + "\r\n");				
				}
			return updated;
		}
		catch(IOException e)
		{
			throw new VersionException("Error updating Version Java File", e);
		}
	}
	
}
