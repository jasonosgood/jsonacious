package jsonacious;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class JSONTanker
{
	public Object parse( String payload )
		throws IOException
	{
		Reader reader = new StringReader( payload );
		return parse( reader );
	}


	public Object parse( Reader reader )
		throws IOException
	{
		this.reader = reader;

		mark = -1;
		nth = -1;
		line = 0;
		pos = 0;
		last = 0;
		back = false;
		marked = false;
		limit = 0;
		return parse();
	}

	public Object parse()
		throws IOException
	{

		while( true )
		{
//			char c;
//			c = read();
			char c = read();

			switch( c )
			{
				// whitespace
				case ' ':
				case '\t':
				case '\r':
				case '\n':
					break;

				case '\'':
				case '"':
					return readString( c );

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
					return readNumber( c );

				case '[':
					return parseList();

				case '{':
					return parseMap();

				case 'n':
					consume( 'u' );
					consume( 'l' );
					consume( 'l' );
					return null;

				case 't':
					consume( 'r' );
					consume( 'u' );
					consume( 'e' );
					return Boolean.TRUE;

				case 'f':
					consume( 'a' );
					consume( 'l' );
					consume( 's' );
					consume( 'e' );
					return Boolean.FALSE;

				case (char) -1:
					return new HashMap<>();

				default:
					throw new IOException();
			}
		}
	}

	Reader reader = null;

	/**
	 * Override this method to use a different Map implementation. eg LinkedHashMap
	 * would preserve file order. ArrayMap would be more space efficient.
	 *
	 * @return
	 */
	public Map<String, Object> createMap()
	{
		return new HashMap<String, Object>();
//		return new LinkedHashMap<String, Object>();
	}

	public Map<String, Object> parseMap( String payload )
		throws IOException
	{
		Reader reader = new StringReader( payload );
		return parseMap( reader );

	}

	public Map<String, Object> parseMap( Reader reader )
		throws IOException
	{
		this.reader = reader;

		mark = -1;
		nth = -1;
		line = 0;
		pos = 0;
		last = 0;
		back = false;
		marked = false;
		limit = 0;

		while( true )
		{
//			char c;
//			c = read();
			char c = read();

			switch( c )
			{
				// whitespace
				case ' ':
				case '\t':
				case '\r':
				case '\n':
					break;

				case '{':
					return parseMap();

				case (char) -1:
					return new HashMap<>();

				default:
					throw new IOException();
			}
		}
	}

	public Map<String, Object> parseMap()
		throws IOException
	{
		Map<String, Object> map = createMap();

		while( true )
		{
			String key = null;
			char c;
			key:
			while( true )
			{
				c = read();
				switch( c )
				{
					// whitespace
					case ' ':
					case '\t':
					case '\r':
					case '\n':
						break;

					case '\'':
					case '"':
						key = readString( c );
						break key;

					case '}':
						return map;

					default:
						throw new IOException();
				}
			}

			colon:
			while( true )
			{
				c = read();
				switch( c )
				{
					// whitespace
					case ' ':
					case '\t':
					case '\r':
					case '\n':
						break;

					case ':':
						break colon;

					default:
						throw new IOException();
				}
			}

			Object value = parse();
			map.put( key, value );

			comma:
			while( true )
			{
				c = read();
				switch( c )
				{
					// whitespace
					case ' ':
					case '\t':
					case '\r':
					case '\n':
						break;

					case ',':
						break comma;

					case '}':
						return map;

					default:
						throw new IOException();
				}
			}
		}
	}

	public List<Object> parseList()
		throws IOException
	{
		List<Object> list = createList();

		while( true )
		{
			char c;
			item:
			while( true )
			{
				c = read();
				switch( c )
				{
					// whitespace
					case ' ':
					case '\t':
					case '\r':
					case '\n':
						break;

					case ']':
						return list;

					default:
						pushBack();
						Object item = parse();
						list.add( item );
						break item;
				}
			}

			comma:
			while( true )
			{
				c = read();
				switch( c )
				{
					// whitespace
					case ' ':
					case '\t':
					case '\r':
					case '\n':
						break;

					case ',':
						break comma;

					case ']':
						return list;

					default:
						throw new IOException();
				}
			}
		}
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

	public String readString( char delim )
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
							int hex =
								( readHex() << 12 ) +
								( readHex() << 8 ) +
								( readHex() << 4 ) +
								readHex();
							sb.append( (char) hex );
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
//		System.out.println( sb.toString() );
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
						result = new BigInteger( value );
					}
					catch( Exception e3 )
					{
						throw new IOException( e3 );
					}
				}
			}
		}

//		System.out.println( result );
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
	int nth = -1;
	int line = 0;
	int pos = 0;
	char last = 0;
	int limit = 0;

	final static int SIZE = 1024;
//	final static int SIZE = 4;
	char[] buf = new char[SIZE];
	/**
	 * Tracks character count, line count, line position.
	 *
	 */
	char read()
		throws IOException
	{
//		char c;
		if( back )
		{
//			c = last;
			back = false;
			return last;
		}
//		else
		{
			nth++;
			// refill buffer as needed
			if( nth == limit )
			{
				if( marked && mark < nth )
				{
					sb.append( buf, mark + 1, nth - mark - 1 );
				}
				limit = reader.read( buf, 0, SIZE );
				if( limit == -1 ) return (char) -1;
				nth = 0;
				mark = -1;
			}
			char c = buf[ nth ];
			last = c;

			return c;

		}


//		if( c == '\n' || c == '\r' )
//		{
//			line++;
//			pos = 0;
//		}
//		else
//		{
//			pos++;
//		}
//
//		if( c == '\n' && last == '\r' )
//			line--;
//
//		if( c == '\r' && last == '\n')
//			line--;

//		last = c;
//
//		return c;
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

	void fill()
	{
		if( marked && mark < nth )
		{
			sb.append( buf, mark + 1, nth - mark - 1 );
		}
		marked = false;
	}
}
