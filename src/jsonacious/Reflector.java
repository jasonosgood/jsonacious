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
		if( Map.class.isAssignableFrom( clazz ))
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
			catch( ClassNotFoundException | InstantiationException | IllegalAccessException e )
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String generate( Class pojo )
	{
		String pkg = pojo.getPackage().getName();
		String className = pojo.getName();
		String simpleClassName = pojo.getSimpleName();

		Field[] fields = pojo.getFields();

		ArrayList<Field> reduced = new ArrayList<>();
		for( Field f : fields )
		{
			{
				// Only support public fields; no static, final, transient, volatile
				int mod = f.getModifiers();
				if( Modifier.isFinal( mod )) continue;
				if( Modifier.isStatic( mod )) continue;
				if( Modifier.isTransient( mod )) continue;
				if( Modifier.isVolatile( mod )) continue;
			}

			{
				// Only support instantiable classes, no interfaces, abstracts
				Class<?> type = f.getType();

				// Special case primitive classes (eg int.class) because they are "abstract final"
				//
				// http://stackoverflow.com/questions/13180600/why-are-java-primitive-types-modifiers-public-abstract-final
				if( !type.isPrimitive() )
				{
					int mod = type.getModifiers();
					if( Modifier.isInterface( mod )) continue;
					if( Modifier.isAbstract( mod )) continue;
				}
			}

			reduced.add( f );
		}

		StringBuilder sb = new StringBuilder();

		print( sb, "package %s;", pkg );
		print( sb, "import jsonacious.Reflector;" );
		print( sb, "import java.lang.reflect.Type;" );
		print( sb );

		// field assignments
		print( sb, "public class %sReflector extends Reflector {", simpleClassName );
		print( sb, "  public void put( Object target, String key, Object value ) {" );
		print( sb, "    %s temp = (%s) target;", className, className );
		print( sb, "    switch( key ) {" );

		for( Field f : reduced )
		{
			String name = f.getName();
			Type type = f.getGenericType();

			print( sb, "      case %s:", quoted( name ));
			// temp.xyz = (java.lang.Object) value;
			print( sb, "        temp.%s = (%s) value;", name, type.getTypeName() );
			print( sb, "        break;" );
		}

		print( sb, "    }" );
		print( sb, "  }" );
		print( sb );

		// type variables
		for( Field f : reduced )
		{
			String name = f.getName();
			//	Type xyzType;
			print( sb, "  Type %sType;", name );
		}
		print( sb );

		// Constructor, initialize type variables
		print( sb, "  public %sReflector() {", simpleClassName );
		print( sb, "    try {" );
		for( Field f : reduced )
		{
			String name = f.getName();
			//   xyzType = Class.class.getField( "xyz" ).getGenericType();
			print( sb, "      %sType = %s.class.getField( %s ).getGenericType();", name, className, quoted( name ) );
		}
		print( sb, "    } catch( NoSuchFieldException e ) { e.printStackTrace(); }" );
		print( sb, "  }" );

		// field types
		print( sb, "  public Type getValueType( String key ) {" );
		print( sb, "    switch( key ) {" );

		for( Field f : reduced )
		{
			String name = f.getName();
			// case "xyz": return xyzType;
			print( sb, "      case %s: return %sType;", quoted( name ), name );
		}

		print( sb, "    }" );
		print( sb, "    return Object.class;" );
		print( sb, "  }" );
		print( sb, "}" );
		print( sb );

		return sb.toString();
	}

	public static void print( StringBuilder sb, String format, String... args )
	{
		String formatted = String.format( format, args );
		sb.append( formatted );
		sb.append( '\n' );
	}

	public static void print( StringBuilder sb )
	{
		sb.append( '\n' );
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
