package jsonacious;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Reflector
{

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
		print( sb, "import jsonacious.JSONWriter;" );
		print( sb, "import java.io.IOException;" );
		print( sb, "import java.lang.reflect.Type;" );
		print( sb );

		print( sb, "public class %sReflector extends Reflector {", simpleClassName );

		// field assignments
		print( sb, "  public void put( Object target, String key, Object value ) {" );
		print( sb, "    %s temp = (%s) target;", className, className );
		if( !reduced.isEmpty() )
		{
			print( sb, "    switch( key ) {" );

			for( Field f : reduced )
			{
				String name = f.getName();
				Type type = f.getGenericType();

				print( sb, "      case %s:", quoted( name ));
				// temp.xyz = (java.lang.Object) value;
				switch( type.getTypeName() )
				{
					case "byte":
					case "java.lang.Byte":
						print( sb, "        temp.%s = toByte( value );", name );
						break;
					case "short":
					case "java.lang.Short":
						print( sb, "        temp.%s = toShort( value );", name );
						break;
					case "int":
					case "java.lang.Integer":
						print( sb, "        temp.%s = toInt( value );", name );
						break;
					case "long":
					case "java.lang.Long":
						print( sb, "        temp.%s = toLong( value );", name );
						break;
					case "float":
					case "java.lang.Float":
						print( sb, "        temp.%s = toFloat( value );", name );
						break;
					case "double":
					case "java.lang.Double":
						print( sb, "        temp.%s = toDouble( value );", name );
						break;
					default:
						print( sb, "        temp.%s = (%s) value;", name, type.getTypeName() );
				}
				print( sb, "        break;" );
			}

			print( sb, "    }" );
		}
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
		if( !reduced.isEmpty() )
		{

			print( sb, "    try {" );
			for( Field f : reduced )
			{
				String name = f.getName();
				//   xyzType = Class.class.getField( "xyz" ).getGenericType();
				print( sb, "      %sType = %s.class.getField( %s ).getGenericType();", name, className, quoted( name ) );
			}
			print( sb, "    } catch( NoSuchFieldException e ) { e.printStackTrace(); }" );
		}
		print( sb, "  }" );

		// field types
		print( sb, "  public Type getValueType( String key ) {" );
		if( !reduced.isEmpty() )
		{
			print( sb, "    switch( key ) {" );
			for( Field f : reduced )
			{
				String name = f.getName();
				// case "xyz": return xyzType;
				print( sb, "      case %s: return %sType;", quoted( name ), name );
			}

			print( sb, "    }" );
		}
		print( sb, "    return null;" );
		print( sb, "  }" );

		print( sb, "  public void write( JSONWriter writer, Object source )", simpleClassName );
		print( sb, "    throws IOException" );
		print( sb, "  {" );
		print( sb, "    %s temp = (%s) source;", className, className );
		print( sb, "    writer.leftSquiggle();" );
		print( sb );

		boolean comma = false;
		for( Field f : reduced )
		{
			if( comma )
			{
				print( sb, "    writer.comma();");
			}
			else
			{
				comma = true;
			}
			String name = f.getName();
 			// writer.writePair( "data", data.field );
			print( sb, "    writer.writePair( %s, temp.%s );", quoted( name ), name );
		}

		print( sb, "    writer.rightSquiggle();" );
		print( sb, "  }" );
		print( sb );

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
		return null;
	}

	public void put( Object target, String key, Object value )
	{
		((Map) target).put( key, value );
	}

	public byte toByte( Object value )
	{
		return ((Number) value).byteValue();
	}

	public short toShort( Object value )
	{
		return ((Number) value).shortValue();
	}

	public int toInt( Object value )
	{
		return ((Number) value).intValue();
	}

	public long toLong( Object value )
	{
		return ((Number) value).longValue();
	}

	public float toFloat( Object value )
	{
		return ((Number) value).floatValue();
	}

	public double toDouble( Object value )
	{
		return ((Number) value).doubleValue();
	}

	/*
		Default implementation used for Maps. Overridden by generated POJO-specific Reflector subclasses.
	 */
	public void write( JSONWriter writer, Object source )
		throws IOException
	{
		writer.leftSquiggle();

		boolean comma = false;
		Map<String, Object> map = (Map<String, Object>) source;
		for( Map.Entry<String,Object> entry : map.entrySet() )
		{
			if( comma )
			{
				writer.comma();
			}
			else
			{
				comma = true;
			}
			writer.writePair( entry.getKey(), entry.getValue() );
		}

		writer.rightSquiggle();
	}

}
