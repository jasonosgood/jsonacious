package jsonacious;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Generator
{
	StringBuilder sb = new StringBuilder();

	public String generate( Class pojo )
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


		print( "package %s;", pkg );
		print( "import jsonacious.Reflector;" );
		print( "import jsonacious.JSONWriter;" );
		print( "import java.io.IOException;" );
		print( "import java.lang.reflect.Type;" );
		print();

		print( "public class %sReflector extends Reflector", simpleClassName );
		print( "{" );
		tabs++;

		// field assignments
		print( "public void put( Object target, String key, Object value )" );
		print( "{" );
		tabs++;
		print( "%s temp = (%s) target;", className, className );
		if( !reduced.isEmpty() )
		{
			print( "switch( key )" );
			print( "{" );

			tabs++;
			for( Field f : reduced )
			{
				String name = f.getName();
				Type type = f.getGenericType();

				print( "case %s:", quoted( name ));

				// Default assignment template:
				//
				//   temp.xyz = (java.lang.Object) value;
				//
				// Enum assignment template:
				//
				//   UserEnum e = UserEnum.valueOf( (String) value );
				//   temp.player = e;
				//
				// Array assignment statement:
				//
				//    tbd

				tabs++;

				// Special case scalars
				switch( type.getTypeName() )
				{
					case "byte":
					case "java.lang.Byte":
						print( "temp.%s = toByte( value );", name );
						break;

					case "short":
					case "java.lang.Short":
						print( "temp.%s = toShort( value );", name );
						break;

					case "int":
					case "java.lang.Integer":
						print( "temp.%s = toInt( value );", name );
						break;

					case "long":
					case "java.lang.Long":
						print( "temp.%s = toLong( value );", name );
						break;

					case "float":
					case "java.lang.Float":
						print( "temp.%s = toFloat( value );", name );
						break;

					case "double":
					case "java.lang.Double":
						print( "temp.%s = toDouble( value );", name );
						break;

					default:
						String fixed = fixClassName( type.getTypeName() );
						if( type instanceof Class && ((Class) type).isEnum() )
						{
							print( "%s e = %s.valueOf( (String) value );", fixed, fixed );
							print( "temp.%s = e;", name );
						}
						else
						{
							print( "temp.%s = (%s) value;", name, fixed );
						}
				}
				print( "break;" );
				tabs--;
			}
			tabs--;

			print( "}" );
		}
		tabs--;
		print( "}" );
		print();

		// type variables
		for( Field f : reduced )
		{
			String name = f.getName();
			//	Type xyzType;
			print( "Type %sType;", name );
		}
		print();

		// Constructor, initialize type variables
		print( "public %sReflector()", simpleClassName );
		print( "{" );
		if( !reduced.isEmpty() )
		{
			tabs++;
			print( "try" );
			print( "{" );
			tabs++;
			for( Field f : reduced )
			{
				String name = f.getName();
				//   xyzType = Class.class.getField( "xyz" ).getGenericType();
				print( "%sType = %s.class.getField( %s ).getGenericType();", name, className, quoted( name ) );
			}
			tabs--;
			print( "}" );
			print( "catch( NoSuchFieldException e ) { e.printStackTrace(); }" );
			tabs--;
		}
		print( "}" );
		print();

		// field types
		print( "public Type getValueType( String key )" );
		print( "{" );
		tabs++;
		if( !reduced.isEmpty() )
		{
			print( "switch( key )" );
			print( "{" );
			tabs++;
			for( Field f : reduced )
			{
				String name = f.getName();
				// case "xyz": return xyzType;
				print( "case %s: return %sType;", quoted( name ), name );
			}
			tabs--;
			print( "}" );
		}
		print( "return null;" );
		tabs--;
		print( "}" );
		print();

		print( "public void write( JSONWriter writer, Object source )", simpleClassName );
		print( "\tthrows IOException" );
		print( "{" );
		tabs++;
		print( "%s temp = (%s) source;", className, className );
		print( "writer.leftSquiggle();" );
		print();

		boolean comma = false;
		for( Field f : reduced )
		{
			if( comma )
			{
				print( "writer.comma();");
			}
			else
			{
				comma = true;
			}
			String name = f.getName();
			// writer.writePair( "data", data.field );
			print( "writer.writePair( %s, temp.%s );", quoted( name ), name );
		}

		print( "writer.rightSquiggle();" );
		tabs--;
		print( "}" );
		print();
		tabs--;
		print( "}" );
		print();

		return sb.toString();
	}

	int tabs = 0;

	public void print( String format, Object... args )
	{
		String formatted = String.format( format, args );
		for( int i = 0; i < tabs; i++ )
		{
			sb.append( '\t' );
		}
		sb.append( formatted );
		sb.append( '\n' );
	}

	public void print()
	{
		sb.append( '\n' );
	}

	public static String quoted( String value )
	{
		return "\"" + value + "\"";
	}

	// Change "Class$InnerClass" to "Class.InnerClass" as needed
	public static String fixClassName( String value )
	{
		return value.replace( '$', '.' );
	}
}
