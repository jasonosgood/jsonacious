package jsonacious;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import static javax.tools.JavaFileObject.Kind.SOURCE;


/**
 * Compile Java class in memory. Input class name and source code as Strings,
 * receive a java.lang.Class. Synthesized from answers found here:
 * <p>
 * http://stackoverflow.com/q/12173294/1371250
 * <p>
 * Uses javax.tools.JavaCompiler. Meaning jsonacious requires tools.jar from JDK.
 */

final public class MemoryBasedCompiler
{
	public static Class<?> compile( String name, String source )
		throws ClassNotFoundException
	{
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if( compiler == null )
		{
			// TODO throw configuration related exception
			System.err.println( "JavaCompiler not found, jsonacious requires tools.jar from JDK distro to be in classpath." );
			return null;
		}

		JavaFileManager fileManager = new MemoryBasedFileManager( compiler );

		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

		URI uri = toURI( name, SOURCE );
		SimpleJavaFileObject sjfo = new SimpleJavaFileObject( uri, SOURCE )
		{
			@Override
			public CharSequence getCharContent( boolean ignoreEncodingErrors )
			{
				return source;
			}
		};
		List<JavaFileObject> files = Arrays.asList( sjfo );

		JavaCompiler.CompilationTask task = compiler.getTask( null, fileManager, diagnostics, null, null, files );

		if( task.call() )
		{
			ClassLoader classLoader = fileManager.getClassLoader( null );
			Class<?> clazz = classLoader.loadClass( name );
			return clazz;
		}
		else
		{
			for( Diagnostic diagnostic : diagnostics.getDiagnostics() )
			{
				System.out.println( diagnostic.getCode() );
				System.out.println( diagnostic.getKind() );
				System.out.println( diagnostic.getPosition() );
				System.out.println( diagnostic.getStartPosition() );
				System.out.println( diagnostic.getEndPosition() );
				System.out.println( diagnostic.getSource() );
				System.out.println( diagnostic.getMessage( null ) );
			}
			// TODO throw exception
			return null;
		}
	}

	public static URI toURI( String className, Kind kind )
	{
		String replaced = className.replace( '.', '/' );
		return URI.create( "string:///" + replaced + kind.extension );
	}
}