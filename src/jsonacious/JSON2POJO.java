package jsonacious;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class JSON2POJO
{
	StringBuilder sb = new StringBuilder();

	public static final void main( String[] args )
		throws Exception
	{
		System.out.println( new JSON2POJO().go( args ));
	}

	public String go( String[] args )
		throws Exception
	{
		JSONReader reader = new JSONReader();
//		String filename = args[ 0 ];
		String filename = "./test/variety/variety.json";
		File file = new File( filename );
		Map<String, Object> map = reader.parse( file, LinkedHashMap.class );


		String pkg = "ugh";
		String name = "name";
//		print( "package %s;", pkg );
		print();

		print( "public class %s {", name );

		for ( Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			String type = getType( value );
			String text;
			if( value == null )
			{
				text =  "null";
			}
			else if( value instanceof String )
			{
				text =  quoted( escape( (String) value ));
			}
			else
			{
				text = value.toString();
			}
			print( "public %s %s %s; // %s : %s", type, key, text, quoted( key ), text );
		}
		print( "}" );
		return sb.toString();
	}

	public void print( String format, String... args )
	{
		String formatted = String.format( format, args );
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

	public String getType( Object value )
	{
		if( value == null ) return "java.lang.Object";

		String type = value.getClass().getName();
		switch( type )
		{
			case "bool":
			case "java.lang.Boolean":
				type = "bool";
				break;

			case "short":
			case "java.lang.Short":
				type = "short";
				break;

			case "int":
			case "java.lang.Integer":
				type = "int";
				break;

			case "long":
			case "java.lang.Long":
				type = "long";
				break;

			case "float":
			case "java.lang.Float":
				type = "float";
				break;

			case "double":
			case "java.lang.Double":
				type = "double";
				break;

			case "java.lang.String":
				type = "String";
				break;
			default:
				break;
		}
		return type;
	}

	public String escape( String text )
	{
		StringBuilder sb = new StringBuilder();
		for( int i = 0; i < text.length(); i++ )
		{
			char c = text.charAt( i );
//			if( c <= 0x1f )
//			{
//				sb.append( '\\' );
//				sb.append( 'u' );
//				sb.append( '0' );
//				sb.append( '0' );
//				sb.append( Integer.toBinaryString( (int) c ) );
//			}
//			else
			{
				switch( c )
				{
					case '"':
						sb.append( '\\' );
						sb.append( '"' );
						break;

					case '\\':
						sb.append( '\\' );
						sb.append( '\\' );
						break;

					case '\t':
						sb.append( '\\' );
						sb.append( 't' );
						break;

					case '\b':
						sb.append( '\\' );
						sb.append( 'b' );
						break;

					case '\n':
						sb.append( '\\' );
						sb.append( 'n' );
						break;

					case '\r':
						sb.append( '\\' );
						sb.append( 'r' );
						break;

					case '\f':
						sb.append( '\\' );
						sb.append( 'f' );
						break;

					case '\'':
						sb.append( "\\u0027" );
						break;

					default:
						sb.append( c );
						break;
				}
			}
		}
		return sb.toString();
	}

}
