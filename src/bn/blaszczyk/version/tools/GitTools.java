package bn.blaszczyk.version.tools;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import bn.blaszczyk.version.VersionException;

public class GitTools 
{
	public static int countCommits(final File repoPath) throws VersionException
	{
		final File repoDir = new File(repoPath,".git");
		int count = 0;
		try
		{
			final Repository repo = FileRepositoryBuilder.create(repoDir);
			final Git git = new Git(repo);
			final Iterable<RevCommit> commits = git.log().call();
			for(final RevCommit commit : commits)
				if(commit != null)
					count++;
		}
		catch (GitAPIException e) 
		{
			throw new VersionException("Error fetching commits for " + repoPath, e);
		}
		catch (IOException e)
		{
			throw new VersionException("Error fetching repository data for " + repoPath, e);
		}
		return count;
	}
}
