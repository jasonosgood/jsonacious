package jsonacious;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.esotericsoftware.reflectasm.FieldAccess;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class JSONDecoder
{
	public <T> T parse( String payload, Class<T> clazz  )
		throws
		Exception
	{
		Reader reader = new StringReader( payload );
		return parse( reader, clazz );
	}

	Reader reader = null;

	public <T> T parse( Reader reader, Class<T> clazz )
		throws
		Exception
	{
		try
		{
			this.reader = reader;

			mark = -1;
			nth = 0;
			line = 0;
			pos = 0;
			last = 0;
			back = false;
			marked = false;
			limit = 0;

			while( true )
			{
				char c = read();

				switch( c )
				{
					case '{':
						T victory = parseMap( clazz );
						return victory;

					// whitespace
					case ' ':
					case '\t':
					case '\r':
					case '\n':
						break;

					case (char) -1:
//					return new HashMap<>();
						return null;

					default:
						throw new IOException( "must start with '{'" );

				}
			}
		}
		catch( Exception e )
		{
			System.out.printf( "nth %d\n", nth );
			System.out.printf( "line %d\n", line );
			System.out.printf( "pos %d\n", pos );
			throw e;
		}
	}

//	public <V> List<V> createList( Class<V> listClass )
//	{
//		return new ArrayList<Object>();
//	}

	HashMap<Class,ConstructorAccess> caMap = new HashMap<>();
	HashMap<Class,MethodAccess> maMap = new HashMap<>();

	public <T> T parseMap( Class<T> parentClazz )
		throws IOException, MethodNotFoundException
	{
		ConstructorAccess<T> access = null;
		if( caMap.containsKey( parentClazz ))
		{
			access = caMap.get( parentClazz );
		}
		else
		{
			access = ConstructorAccess.get( parentClazz );
			caMap.put( parentClazz, access );
		}
		T parent = access.newInstance();

		MethodAccess ma = null;
		if( maMap.containsKey( parentClazz ))
		{
			ma = maMap.get( parentClazz );
		}
		else
		{
			ma = MethodAccess.get( parentClazz );
			maMap.put( parentClazz, ma );
		}

		String key = null;
		char c = 0;

		while( (  c = read() ) != -1 )
		{
			switch( c )
			{
				case '{':
				{
					int index = findMethodIndex( ma, key );
					Class childClazz = ma.getParameterTypes()[ index ][ 0 ];
					Object child = parseMap( childClazz );
					ma.invoke( parent, index, child );
					key = null;
					break;
				}

				case '}':
				{
					return parent;
				}

				case '[':
				{
					int index = findMethodIndex( ma, key );
					Class childClazz = ma.getParameterTypes()[ index ][ 0 ];
					List<Object> child = parseList( childClazz );
					ma.invoke( parent, index, child );
					key = null;
					break;
				}

				case '\'':
				case '"':
				{
					String value = readString( c );
					if( key == null )
					{
						key = value;
					} else
					{
						int index = findMethodIndex( ma, key );
						Class childClazz = ma.getParameterTypes()[ index ][ 0 ];
						if( childClazz.isEnum() )
						{
							Enum converted = Enum.valueOf( childClazz, value );
							ma.invoke( parent, index, converted );
						} else
						{
							ma.invoke( parent, index, value );
						}
						key = null;
					}

					break;
				}
				case ':':
					break;

				case ',':
					break;

				case 'n':
				{
					consume( 'u' );
					consume( 'l' );
					consume( 'l' );
					int index = findMethodIndex( ma, key );
					ma.invoke( parent, index, (Object) null );
					key = null;
					break;
				}

				case 't':
				{
					consume( 'r' );
					consume( 'u' );
					consume( 'e' );
					int index = findMethodIndex( ma, key );
					ma.invoke( parent, index, true );
					key = null;
					break;
				}

				case 'f':
				{
					consume( 'a' );
					consume( 'l' );
					consume( 's' );
					consume( 'e' );
					int index = findMethodIndex( ma, key );
					ma.invoke( parent, index, false );
					key = null;
					break;
				}

				case '-':
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				{
					String value = readNumber2( c );
					Number number = null;
					int index = findMethodIndex( ma, key );
					Class childClazz = ma.getParameterTypes()[ index ][ 0 ];
					switch( childClazz.getSimpleName() )
					{
						case "Integer":
						case "int":
							number = Integer.valueOf( value );
							break;

						case "Long":
						case "long":
							number = Long.valueOf( value );
							break;

						default:
//							number = readNumber( c );
							break;

					}
					ma.invoke( parent, index, number );
					key = null;
					break;
				}
				// whitespace
				case ' ':
				case '\t':
				case '\r':
				case '\n':
					break;

				default:
					// TODO Something went wrong

					break;

			}
		}

		return parent;
	}

	private String capitalize( String propertyName )
	{
		if (propertyName.length() == 0) return null;
		return propertyName.substring( 0, 1 ).toUpperCase() + propertyName.substring( 1 );
	}


	public <U> List<U> parseList( Class<U> itemClazz )
		throws IOException, MethodNotFoundException
	{
		// TODO: Pass in list class to be created
		List<U> parent = new ArrayList<U>();

		char c = 0;

		while( (  c = read() ) != -1 )
		{
			switch( c )
			{
				case '{':
				{
					U child = parseMap( itemClazz );
					parent.add( child );
					break;
				}

				case '[':
				{
//					List<Object> child = parseList();
//					parent.add( child );
					break;
				}

				case ']':
				{
					return parent;
				}


				case '\'':
				case '"':
					String value = readString( c );
					parent.add( (U) value );

					break;

				case ',':
					break;

				case 'n':
					consume( 'u' );
					consume( 'l' );
					consume( 'l' );
					parent.add( null );
					break;

				case 't':
					consume( 'r' );
					consume( 'u' );
					consume( 'e' );
					parent.add( (U) Boolean.TRUE );
					break;

				case 'f':
					consume( 'a' );
					consume( 'l' );
					consume( 's' );
					consume( 'e' );
					parent.add( (U) Boolean.FALSE );
					break;

				case '-':
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					Number number = readNumber( c );
					// TODO: This will need some conversion and casting
					parent.add( (U) number );
					break;

				// whitespace
				case ' ':
				case '\t':
				case '\r':
				case '\n':
					break;

				default:
					// TODO Something went wrong
					break;

			}
		}
		return parent;
	}

	StringBuilder sb = new StringBuilder();

	public String readString( int delim )
		throws IOException
	{
		char c;
		sb.setLength( 0 );
		mark();
		while( (  c = read() ) != delim )
		{
			switch( c )
			{
				case '\\':
				{
					fill();
					unmark();
					c = read();
					switch( c )
					{
						case '"':
							sb.append( '"' );
							break;

						case '/':
							sb.append( '/' );
							break;

						case '\\':
							sb.append( '\\' );
							break;

						case 'b':
							sb.append( '\b' );
							break;

						case 'f':
							sb.append( '\f' );
							break;

						case 'n':
							sb.append( '\n' );
							break;

						case 'r':
							sb.append( '\r' );
							break;

						case 't':
							sb.append( '\t' );
							break;

						case 'u':
//							int hex =
//								readHex( reader ) << 12 +
//								readHex( reader ) << 8 +
//								readHex( reader ) << 4 +
//								readHex( reader );
							int hex =
								readHex() << 12;
							hex += readHex() << 8;
							hex += readHex() << 4;
							hex += readHex();
//							System.out.println( " = " + hex );
//							sb.append( (char) hex );
//							char hex = readHexZ();
							sb.append( hex );

							break;

						default:
							throw new IOException( "what is '\\" + (char) c + "'?" );
					}

					mark();

					break;
				}


				case (char) -1:
				{
					throw new IOException( "unexpected end of file" );
				}
				default:
					break;
			}
		}
		fill();
		unmark();
		return sb.toString();
	}

	public Number readNumber( char c )
		throws IOException
	{
		sb.setLength( 0 );
		sb.append( (char) c );
		mark();
		boolean decimal = false;
		loop:
		while( true )
		{
			int d = read();
			switch( d )
			{
				case '.':
					decimal = true;
				case '-':
				case '+':
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case 'E':
				case 'e':
					break;

				case -1:
					throw new IOException( "unexpected end of file" );

				default:
					break loop;
			}
		}
		fill();
		unmark();
		pushBack();

		Number result = null;
		String value = sb.toString();
		if( decimal )
		{
			try
			{
				result = Float.parseFloat( value );
				if( Float.isInfinite( result.floatValue() ))
				{
					result = Double.parseDouble( value );
					if( Double.isInfinite( result.doubleValue() ))
					{
						result = new BigDecimal( value );
					}
				}
			}
			catch( Exception e )
			{
				throw new IOException( e );
			}
		}
		else
		{
			try
			{
				result = Integer.parseInt( value );
			}
			catch( Exception e )
			{
				try
				{
					result = Long.parseLong( value );
				}
				catch( Exception e2 )
				{
					try
					{
						result = new BigDecimal( value );
					}
					catch( Exception e3 )
					{
						throw new IOException( e3 );
					}
				}
			}
		}
		return result;
	}

	public String readNumber2( char c )
		throws IOException
	{
		sb.setLength( 0 );
		sb.append( (char) c );
		mark();
		boolean decimal = false;
		loop:
		while( true )
		{
			int d = read();
			switch( d )
			{
				case '.':
					decimal = true;
				case '-':
				case '+':
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case 'E':
				case 'e':
					break;

				case -1:
					throw new IOException( "unexpected end of file" );

				default:
					break loop;
			}
		}
		fill();
		unmark();
		pushBack();

		String value = sb.toString();
		return value;
	}

	public int readHex()
		throws IOException
	{

		char x = read();
		if( x >= '0' && x <= '9' )
		{
//			System.out.print( x - '0' );
			return x - '0';
		}
		if( x >= 'a' && x <= 'h' )
		{
//			System.out.print( x - 'a' + 10 );
			return x - 'a' + 10;
		}
		if( x >= 'A' && x <= 'H' )
		{
//			System.out.print( x - 'A' + 10 );
			return x - 'A' + 10;
		}
		throw new IOException( "not a hex digit " + (char) x );
	}

//	public char readHexZ()
//		throws IOException
//	{
//		int result = 0;
//		for( int i = 0; i < 4; i++ )
//		{
//			result <<= 4;
//			char x = read();
//			if( x >= '0' && x <= '9' )
//			{
//				result += ( x - '0' );
//				continue;
//			}
//			if( x >= 'a' && x <= 'h' )
//			{
//				result += ( x - 'a' + 10 );
//				continue;
//			}
//			if( x >= 'A' && x <= 'H' )
//			{
//				result += ( x - 'A' + 10 );
//				continue;
//			}
//			throw new IOException( "not a hex digit " + (char) x );
//		}
//		return (char) result;
//	}

	public void consume( char e )
		throws IOException
	{
		char c = read();
		if( c != e )
		{
			throw new IOException( "expected '" + e + "', found '" + c + "'" );
		}
	}

	int mark = -1;
	int nth = 0;
	int line = 0;
	int pos = 0;
	char last = 0;
	int limit = 0;

	final static int SIZE = 1024;
	char[] buf = new char[SIZE];

	/**
	 * Tracks character count, line count, line position. Refills buffer as necessary.
	 *
	 */
	char read()
		throws IOException
	{

		char c = 0;
		if( back )
		{
			c = last;
			back = false;
		}
		else
		{
			if( nth == limit )
			{
				if( marked && mark < nth )
				{
					sb.append( buf, mark, nth - mark );
				}

				limit = reader.read( buf, 0, SIZE );
				nth = 0;
				mark = 0;
			}
			c = buf[ nth ];
		}

		nth++;

		if( c == '\n' || c == '\r' )
		{
			line++;
			pos = 0;
		}
		else
		{
			pos++;
		}

		if( c == '\n' && last == '\r' )
			line--;

		if( c == '\r' && last == '\n')
			line--;

		last = c;

		return c;
	}

	boolean back = false;
	void pushBack()
	{
		back = true;
	}

	boolean marked = false;
	void mark()
	{
		mark = nth;
		marked = true;
	}

	void unmark()
	{
		mark = -1;
		marked = false;
	}

	void fill()
	{
		if( marked && mark < nth )
		{
			sb.append( buf, mark, nth - mark - 1 );
		}
	}

	class ClassMethodKey
	{
		Class c;
		String m;

		public boolean equals( Object object )
		{
			if( this == object ) return true;
			if( object instanceof ClassMethodKey )
			{
				ClassMethodKey that = (ClassMethodKey) object;
				boolean a = this.c.equals( that.c );
				boolean b = this.m.equals( that.m );
				return a && b;
			}
			return false;
		}

		public int hashCode()
		{
			return c.hashCode() * m.hashCode();
		}
	}

	HashMap<ClassMethodKey,Integer> indexMap = new HashMap<>();
	ClassMethodKey spareKey = new ClassMethodKey();

	public int findMethodIndex( MethodAccess ma, String name )
		throws MethodNotFoundException
	{
		spareKey.c = ma.getClass();
		spareKey.m = name;
		if( indexMap.containsKey( spareKey ))
		{
			return indexMap.get( spareKey );
		}

		String setter = "set" + capitalize( name );

		int fuzzyIndex = -1;
		int max = ma.getMethodNames().length;
		for( int nth = 0; nth < max; nth++ )
		{
			if( ma.getParameterTypes()[nth].length != 1 )
			{
				continue;
			}
			String temp = ma.getMethodNames()[nth];
			if( setter.equals( temp ))
			{
				fuzzyIndex = nth;
				break;
			}
			if( setter.equalsIgnoreCase( temp ))
			{
				fuzzyIndex = nth;
			}
		}

		if( fuzzyIndex == -1 )
		{
			// Delegate exception throwing to ReflectASM
			ma.getIndex( setter );
		}

		ClassMethodKey key = new ClassMethodKey();
		key.c = ma.getClass();
		key.m = name;
		indexMap.put( key, fuzzyIndex );

		return fuzzyIndex;
	}

	public Class<?> getParamClass( Method method )
	{
		Type[] ooh = method.getGenericParameterTypes();

		Type y = ooh[0];
		if( y instanceof ParameterizedType )
		{
			ParameterizedType duff = (ParameterizedType) y;
			Type[] actual = duff.getActualTypeArguments();
			return (Class) actual[0];
//			Type owner = duff.getRawType();
//			return (Class) owner;
		}
		else
		{
			return (Class) y;
		}
	}

//	public void setChild( Object parent, Method methodA, Object child )
//		throws IOException
//	{
//		try
//		{
//			methodA.invoke( parent, child );
//		}
//		catch( IllegalAccessException e )
//		{
//			e.printStackTrace();
//			throw new IOException( e );
//		}
//		catch( InvocationTargetException e )
//		{
//			e.printStackTrace();
//			throw new IOException( e );
//		}
//
//	}

}
