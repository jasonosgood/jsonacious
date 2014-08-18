package jsonacious;

import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 */
public class JSONReader
{

    public static void main( String[] args )
        throws Exception
	{
		JSONReader crunk = new JSONReader();
		String payload =
			"{ " +
//			"'A' : 'apple', \n" +
//			"'B' : \"null\", \n" +
//			"'C' : 'code', \n" +
//			"'D' : {}, \n" +
//			"'E' : { 'Z1' : 'zebra' }, \n" +
//			"'F' : '\\u0048\\u0065\\u006C\\u006C\\u006F World', \n" +
//			"'G' : null, \n" +
//			"'H' : true, \n" +
//			"'I' : false, \n" +
//			"'J' : [], \n" +
//			"'K' : [ 'one', 'two' ] \n" +
//			"'L' : [ { 'side' : 'left' }, { 'side' : 'right' } ], \n" +
//			"'M' : [ ['one', 'two'], ['three','four' ], \n" +
			"'N' : -123.789, \n" +
			"'O' : [ 123, 0, -1, 9223372036854775807, 1.7976931348623157E308 ] \n" +

			"} ";

		FileReader reader = new FileReader( "/Users/jasonosgood/Projects/johannson/data/whatever.json" );
		StringBuilder sb = new StringBuilder();
		while( true )
		{
			int c = reader.read();
			if( c == -1 ) break;
			sb.append( (char) c );
		}
		payload = sb.toString();

		StringReader sparky = new StringReader( payload );
		sparky.reset();

		long start = System.currentTimeMillis();
		int reps = 23809;
		for( int n = 0; n < reps; n++ )
		{
			Map map = crunk.parse( sparky );
//			Map map = crunk.parse( payload );
			sparky.reset();

		}
		long elapsed = System.currentTimeMillis() - start;

//		System.out.println( map );
		System.out.printf( "elapsed: %d \n", elapsed );
		System.out.printf( "each: %f \n", (double) elapsed / (double) reps );
	}

	public Map<String, Object> parse( String payload )
		throws IOException
	{
		Reader reader = new StringReader( payload );
		return parse( reader );
	}

	public Map<String, Object> parse( Reader reader )
		throws IOException
	{
		while( true )
		{
			int c = read( reader );

			switch( c )
			{
				case '{':
					return parseMap( reader );

				// whitespace
				case ' ':
				case '\t':
				case '\r':
				case '\n':
					break;

				case -1:
					return new HashMap<>();

				default:
					throw new IOException( "must start with '{'" );

			}
		}
	}

	static class NullMap implements Map
	{

		@Override
		public int size()
		{
			return 0;
		}

		@Override
		public boolean isEmpty()
		{
			return false;
		}

		@Override
		public boolean containsKey( Object key )
		{
			return false;
		}

		@Override
		public boolean containsValue( Object value )
		{
			return false;
		}

		@Override
		public Object get( Object key )
		{
			return null;
		}

		@Override
		public Object put( Object key, Object value )
		{
			return null;
		}

		@Override
		public Object remove( Object key )
		{
			return null;
		}

		@Override
		public void putAll( Map m )
		{

		}

		@Override
		public void clear()
		{

		}

		@Override
		public Set keySet()
		{
			return null;
		}

		@Override
		public Collection values()
		{
			return null;
		}

		@Override
		public Set<Entry> entrySet()
		{
			return null;
		}
	}

	static Map<String, Object> NULL_MAP = new NullMap();

	public Map<String, Object> createMap()
	{
//		return NULL_MAP;
		return new HashMap<String, Object>();
	}

	public Map<String, Object> parseMap( Reader reader )
		throws IOException
	{
//		Map<String, Object> parent = new HashMap<>();
//		Map<String, Object> parent = new LinkedHashMap<>();
		Map<String, Object> parent = createMap();

		String key = null;
		int c = 0;

        while( (  c = read( reader ) ) != -1 )
        {
            switch( c )
            {
                case '{':
				{
					Map<String, Object> child = parseMap( reader );
					parent.put( key, child );
					key = null;
					break;
				}

                case '}':
				{
					return parent;
				}

 				case '[':
				{
					List<Object> child = parseList( reader );
					parent.put( key, child );
					key = null;
					break;
				}

				case '\'':
				case '"':
					String value = readString( c, reader );
					if( key == null )
					{
						key = value;
					}
					else
					{
						parent.put( key, value );
						key = null;
					}

                    break;

				case ':':
					break;

				case ',':
					break;

				case 'n':
					consume( reader, 'u' );
					consume( reader, 'l' );
					consume( reader, 'l' );
					parent.put( key, null );
					key = null;
					break;

				case 't':
					consume( reader, 'r' );
					consume( reader, 'u' );
					consume( reader, 'e' );
					parent.put( key, true );
					key = null;
					break;

				case 'f':
					consume( reader, 'a' );
					consume( reader, 'l' );
					consume( reader, 's' );
					consume( reader, 'e' );
					parent.put( key, false );
					key = null;
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
					Number number = readNumber( c, reader );
					parent.put( key, number );
					key = null;
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

	public List<Object> parseList( Reader reader )
		throws IOException
	{
//		List<Object> parent = new ArrayList<>();
		List<Object> parent = createArray();

		int c = 0;

		while( (  c = read( reader ) ) != -1 )
		{
			switch( c )
			{
				case '{':
				{
					Map<String, Object> child = parseMap( reader );
					parent.add( child );
					break;
				}

				case '[':
				{
					List<Object> child = parseList( reader );
					parent.add( child );
					break;
				}

				case ']':
				{
					return parent;
				}


				case '\'':
				case '"':
					String value = readString( c, reader );
					parent.add( value );

					break;

				case ',':
					break;

				case 'n':
					consume( reader, 'u' );
					consume( reader, 'l' );
					consume( reader, 'l' );
					parent.add( null );
					break;

				case 't':
					consume( reader, 'r' );
					consume( reader, 'u' );
					consume( reader, 'e' );
					parent.add( true );
					break;

				case 'f':
					consume( reader, 'a' );
					consume( reader, 'l' );
					consume( reader, 's' );
					consume( reader, 'e' );
					parent.add( false );
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
					Number number = readNumber( c, reader );
					parent.add( number );
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

	static class NullList<T> implements List
	{

		@Override
		public int size()
		{
			return 0;
		}

		@Override
		public boolean isEmpty()
		{
			return false;
		}

		@Override
		public boolean contains( Object o )
		{
			return false;
		}

		@Override
		public Iterator iterator()
		{
			return null;
		}

		@Override
		public Object[] toArray()
		{
			return new Object[ 0 ];
		}

		@Override
		public boolean add( Object o )
		{
			return false;
		}

		@Override
		public boolean remove( Object o )
		{
			return false;
		}

		@Override
		public boolean addAll( Collection c )
		{
			return false;
		}

		@Override
		public boolean addAll( int index, Collection c )
		{
			return false;
		}

		@Override
		public void clear()
		{

		}

		@Override
		public Object get( int index )
		{
			return null;
		}

		@Override
		public Object set( int index, Object element )
		{
			return null;
		}

		@Override
		public void add( int index, Object element )
		{

		}

		@Override
		public Object remove( int index )
		{
			return null;
		}

		@Override
		public int indexOf( Object o )
		{
			return 0;
		}

		@Override
		public int lastIndexOf( Object o )
		{
			return 0;
		}

		@Override
		public ListIterator listIterator()
		{
			return null;
		}

		@Override
		public ListIterator listIterator( int index )
		{
			return null;
		}

		@Override
		public List subList( int fromIndex, int toIndex )
		{
			return null;
		}

		@Override
		public boolean retainAll( Collection c )
		{
			return false;
		}

		@Override
		public boolean removeAll( Collection c )
		{
			return false;
		}

		@Override
		public boolean containsAll( Collection c )
		{
			return false;
		}

		@Override
		public T[] toArray( Object[] a )
		{
			return null;
		}
	}

	static List<Object> NULL_LIST = new NullList();

	private List<Object> createArray()
	{
		return new ArrayList<Object>();
//		return NULL_LIST;
	}

	StringBuilder sb = new StringBuilder();
	public String readString( int delim, Reader reader )
		throws IOException
	{
		int c;
		sb.setLength( 0 );
		while( (  c = read( reader ) ) != delim )
		{
			switch( c )
			{
				case '\\':
				{
					c = read( reader );
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
								readHex( reader ) << 12;
							hex += readHex( reader ) << 8;
							hex += readHex( reader ) << 4;
							hex += readHex( reader );
//							System.out.println( " = " + hex );
							sb.append( (char) hex );

							break;

						default:
							throw new IOException( "what is '\\" + (char) c + "'?" );
					}
					break;
				}

				case -1:
				{
					throw new IOException( "unexpected end of file" );
				}
				default:
					sb.append( (char) c );
					break;
			}
		}
		return sb.toString();
	}

	public Number readNumber( int c, Reader reader )
		throws IOException
	{
//		StringBuilder sb = new StringBuilder();
		sb.setLength( 0 );
		sb.append( (char) c );
		boolean decimal = false;
		loop:
		while( true )
		{
			int d = read( reader );
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
					sb.append( (char) d );
					break;

				case -1:
					throw new IOException( "unexpected end of file" );

				default:
					pushBack();
					break loop;
			}
		}

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

	// http://stackoverflow.com/questions/1078953/check-if-bigdecimal-is-integer-value
	boolean isIntegerValue( BigDecimal bd )
	{
		return bd.signum() == 0 || bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0;
	}

	public int readHex( Reader reader )
		throws IOException
	{
		int x = read( reader );
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

	public void consume( Reader reader, char e )
		throws IOException
	{
		char c = (char) read( reader );
		if( c != e )
		{
			throw new IOException( "expected '" + e + "', found '" + c + "'" );
		}
	}

	int nth = 0;
	int line = 0;
	int pos = 0;
	int last = 0;

	/**
	 * Tracks character count, line count, line position.
	 *
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	int read( Reader reader )
		throws IOException
	{
		int c = back ? last : reader.read();
		back = false;

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


}
