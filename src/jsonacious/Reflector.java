package jsonacious;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Reflector {

	private final static HashMap<Class, Reflector> _reflectors = new HashMap<>();

	private final static Reflector _default = new Reflector();

	// TODO: synchronized
	public final static void add( Class clazz, Reflector reflector )
	{
		if( clazz == null )
		{
			throw new NullPointerException( "clazz" );
		}
		if( reflector == null )
		{
			throw new NullPointerException( "reflector" );
		}

		if( !clazz.isInstance( Map.class ))
		{
			_reflectors.put( clazz, reflector );
		}
	}

	// TODO: synchronized
	public final static Reflector get( Class clazz )
	{
		if( clazz.isInstance( Map.class ))
		{
			return _default;
		}

		Reflector reflector = _reflectors.get( clazz );
		if( reflector != null )
		{
			return reflector;
		}
		else
		{
			try
			{
				String name = clazz.getName() + "Reflector";
				String source = generate( clazz );
				Class reflectorClazz = MemoryBasedCompiler.compile( name, source );
				reflector = (Reflector) reflectorClazz.newInstance();
				Reflector.add( clazz, reflector );
				return reflector;
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String generate( Class pojo )
		throws IOException
	{
		String pkg = pojo.getPackage().getName();
		String className = pojo.getName();
		String simpleClassName = pojo.getSimpleName();

		Field[] fields = pojo.getFields();

		ArrayList<Field> reduced = new ArrayList<>();
		for( Field f : fields )
		{
			int mod = f.getModifiers();
			if( Modifier.isFinal( mod )) continue;
			if( Modifier.isStatic( mod )) continue;
			if( Modifier.isTransient( mod )) continue;
			if( Modifier.isVolatile( mod )) continue;
			reduced.add( f );
		}

		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter( writer );

		out.printf( "package %s;\n", pkg );
		out.printf( "import jsonacious.Reflector;\n" );
		out.printf( "import java.lang.reflect.Type;\n" );
		out.println();

		// field assignments
		out.printf( "public class %sReflector extends Reflector {\n", simpleClassName );
		out.printf( "  public void put( Object target, String key, Object value ) {\n" );
		out.printf( "    %s temp = (%s) target;\n", className, className );
		out.printf( "    switch( key ) {\n" );

		for( Field f : reduced )
		{
			String name = f.getName();
			Type type = f.getGenericType();

			out.printf( "      case %s:\n", quoted( name ));
			// temp.xyz = (java.lang.Object) value;
			out.printf( "        temp.%s = (%s) value;\n", name, type.getTypeName() );
			out.printf( "        break;\n" );
		}

		out.printf( "    }\n" );
		out.printf( "  }\n" );
		out.println();

		// static variables
		for( Field f : reduced )
		{
			String name = f.getName();
			//   static Type xyzType;
			out.printf( "  static Type %sType;\n", name );
		}
		out.println();

		// static initializers
		out.printf( "  static {\n" );
		out.printf( "    try {\n" );
		for( Field f : reduced )
		{
			String name = f.getName();
			//   xyzType = Class.class.getField( "xyz" ).getGenericType();
			out.printf( "      %sType = %s.class.getField( %s ).getGenericType();\n", name, className, quoted( name ) );
		}
		out.printf( "    } catch( NoSuchFieldException e ) {}\n" );
		out.printf( "  }\n" );

		// field types
		out.printf( "  public Type getValueType( String key ) {\n" );
		out.printf( "    switch( key ) {\n" );

		for( Field f : reduced )
		{
			String name = f.getName();
			// case "xyz": return xyzType;
			out.printf( "      case %s: return %sType;\n", quoted( name ), name );
		}

		out.printf( "    }\n" );
		out.printf( "    return Object.class;\n" );
		out.printf( "  }\n" );
		out.printf( "}\n" );
		out.close();

		return writer.toString();
	}

	public static String quoted( String value )
	{
		return "\"" + value + "\"";
	}

	public Type getValueType( String key )
	{
		return Object.class;
	}

	public void put( Object target, String key, Object value )
	{
		((Map) target).put( key, value );
	}
}
