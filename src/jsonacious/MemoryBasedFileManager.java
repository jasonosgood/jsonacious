package jsonacious;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.Map;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;

public class
	MemoryBasedFileManager
extends
	ForwardingJavaFileManager<JavaFileManager>
{
	public MemoryBasedFileManager( JavaCompiler compiler )
	{
		super( compiler.getStandardFileManager( null, null, null ));
	}

	private Map<String, ByteArrayOutputStream> nameMap = new HashMap<>();

	@Override
	public ClassLoader getClassLoader( Location location )
	{
		return new SecureClassLoader()
		{
			@Override
			protected Class<?> findClass( String name )
				throws ClassNotFoundException
			{
				ByteArrayOutputStream bos = nameMap.get( name );
				if( bos == null )
				{
					return null;
				}
				byte[] b = bos.toByteArray();
				Class<?> clazz = super.defineClass( name, b, 0, b.length );
				return clazz;
			}
		};
	}

	@Override
	public JavaFileObject getJavaFileForOutput( Location location, String className, Kind kind, FileObject sibling )
		throws IOException
	{
		URI uri = MemoryBasedCompiler.toURI( className, kind );

		return new SimpleJavaFileObject( uri, kind )
		{
			@Override
			public OutputStream openOutputStream()
				throws IOException
			{
				ByteArrayOutputStream bos = nameMap.get( className );
				if( bos == null )
				{
					bos = new ByteArrayOutputStream();
					nameMap.put( className, bos );
				}
				return bos;
			}
		};
	}

}
