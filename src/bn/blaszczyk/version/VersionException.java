package bn.blaszczyk.version;

public class VersionException extends Exception
{
	
	private static final long serialVersionUID = 1L;

	public VersionException(final String message) 
	{
		super(message);
	}

	public VersionException(final Throwable cause)
	{
		super(cause);
	}

	public VersionException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

}
