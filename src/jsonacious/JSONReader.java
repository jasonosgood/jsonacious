package jsonacious;

import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 */
public class JSONReader
{
	public Map<String, Object> parse( String payload )
		throws IOException
	{
		Reader reader = new StringReader( payload );
		return parse( reader );
	}

	Reader reader = null;
	public Map<String, Object> parse( Reader reader )
		throws IOException
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
					Map map = parseMap();
					return map;


				// whitespace
				case ' ':
				case '\t':
				case '\r':
				case '\n':
					break;

				case (char) -1:
					return new HashMap<>();

				default:
					throw new IOException( "must start with '{'" );

			}
		}
	}

	/**
	 * Override this method to use a different Map implementation. eg LinkedHashMap
	 * would preserve file order. ArrayMap would be more space efficient.
	 *
	 * @return
	 */
	public Map<String, Object> createMap()
	{
		return new HashMap<String, Object>();
	}

	public Map<String, Object> parseMap()
		throws IOException
	{
		Map<String, Object> parent = createMap();

		String key = null;
		char c = 0;

		while( (  c = read() ) != -1 )
		{
			switch( c )
			{
				case '{':
				{
					Map<String, Object> child = parseMap();
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
					List<Object> child = parseList();
					parent.put( key, child );
					key = null;
					break;
				}

				case '\'':
				case '"':
					String value = readString( c );
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
					consume( 'u' );
					consume( 'l' );
					consume( 'l' );
					parent.put( key, null );
					key = null;
					break;

				case 't':
					consume( 'r' );
					consume( 'u' );
					consume( 'e' );
					parent.put( key, true );
					key = null;
					break;

				case 'f':
					consume( 'a' );
					consume( 'l' );
					consume( 's' );
					consume( 'e' );
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
					Number number = readNumber( c );
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

	public List<Object> parseList()
		throws IOException
	{
		List<Object> parent = createList();

		char c = 0;

		while( (  c = read() ) != -1 )
		{
			switch( c )
			{
				case '{':
				{
					Map<String, Object> child = parseMap();
					parent.add( child );
					break;
				}

				case '[':
				{
					List<Object> child = parseList();
					parent.add( child );
					break;
				}

				case ']':
				{
					return parent;
				}


				case '\'':
				case '"':
					String value = readString( c );
					parent.add( value );

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
					parent.add( true );
					break;

				case 'f':
					consume( 'a' );
					consume( 'l' );
					consume( 's' );
					consume( 'e' );
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
					Number number = readNumber( c );
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

	/**
	 * Override this method to use a different List implementation. For whatever reason.
	 *
	 * @return
	 */
	public List<Object> createList()
	{
		return new ArrayList<Object>();
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
//							sb.append( hex );

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

	public char readHexZ()
		throws IOException
	{
		int result = 0;
		for( int i = 0; i < 4; i++ )
		{
			result <<= 4;
			char x = read();
			if( x >= '0' && x <= '9' )
			{
				result += ( x - '0' );
				continue;
			}
			if( x >= 'a' && x <= 'h' )
			{
				result += ( x - 'a' + 10 );
				continue;
			}
			if( x >= 'A' && x <= 'H' )
			{
				result += ( x - 'A' + 10 );
				continue;
			}
			throw new IOException( "not a hex digit " + (char) x );
		}
		return (char) result;
	}

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
	 * Tracks character count, line count, line position.
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
				nth++;
				fill();
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
}
