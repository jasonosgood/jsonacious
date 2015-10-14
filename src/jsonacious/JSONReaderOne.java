package jsonacious;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JSONReaderOne
{
	public final static char EOF = (char) -1;
	final static int SIZE = 1024;

	char[] buf = new char[ SIZE ];

	Reader reader = null;
	StringBuilder sb = new StringBuilder();
	int line = 1;
	int pos = 0;
	int nth = -1;
	int limit = 0;
	int mark = -1;
	char last = 0;
	boolean back = false;
	boolean marked = false;

	/**
	 * Returns either Map or List.
	 */
	public Object parse( String payload )
		throws IOException
	{
		Reader reader = new StringReader( payload );
		return parse( reader );
	}

	/**
	 * Returns either Map or List.
	 */
	public Object parse( Reader reader )
		throws IOException
	{
		reset();
		this.reader = reader;
		return parseRoot();
	}

	Object parseRoot()
		throws IOException
	{
		while( true )
		{
			char c = read();
			switch( c )
			{
				case '[':
					return parseList();

				case '{':
					return parseMap();
			}
		}
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
		reset();
		this.reader = reader;

		char c;
		while( true )
		{
			c = read();
			switch( c )
			{
				case '{':
					return parseMap();

				// whitespace
				case ' ':
				case '\t':
				case '\r':
				case '\n':
					break;

				case EOF:
					throw new ParseException( "EOF, expected '{'", line, pos );

				default:
					throw new ParseException( "map not found", '{', c, line, pos );
			}
		}
	}

//	public String readString( char delim )
//		throws IOException
//	{
//		sb.setLength( 0 );
//		mark();
//		char c;
//		while( (  c = read() ) != delim )
//		{
//			if( c == '\\' ) {
//				readEscapedString();
//			}
//		}
//		fill();
////		System.out.println( sb.toString() );
//		return sb.toString();
//	}
//
//	public void readEscapedString()
//		throws IOException
//	{
//		fill();
//		char c = read();
//		switch( c )
//		{
//			case '"':
//				sb.append( '"' );
//				break;
//
//			case '/':
//				sb.append( '/' );
//				break;
//
//			case '\\':
//				sb.append( '\\' );
//				break;
//
//			case 'b':
//				sb.append( '\b' );
//				break;
//
//			case 'f':
//				sb.append( '\f' );
//				break;
//
//			case 'n':
//				sb.append( '\n' );
//				break;
//
//			case 'r':
//				sb.append( '\r' );
//				break;
//
//			case 't':
//				sb.append( '\t' );
//				break;
//
//			case 'u':
//				int hex =
//					( readHex() << 12 ) +
//						( readHex() << 8 ) +
//						( readHex() << 4 ) +
//						readHex();
////							int hex =
////								readHex() << 12;
////							hex += readHex() << 8;
////							hex += readHex() << 4;
////							hex += readHex();
//				sb.append( (char) hex );
////							char hex = readHexZ();
////							sb.append( (char) hex );
//				break;
//
//			default:
//				throw new ParseException( "what is '\\" + c + "'?", line, pos );
//		}
//
//		mark();
//	}

	public Map<String, Object> parseMap()
		throws IOException
	{
		Map<String, Object> map = createMap();

		while( true )
		{
			String key = parseKey();
			if( key == null ) return map;

			parseColon();

			Object value = parseValue();

			map.put( key, value );

			if( doneMap() ) return map;
		}
	}

	/**
	 * Returns true if next char is a comma ',', false if right curly bracket '}'.
	 * Eats leading whitespace.
	 * Otherwise throws an exception.
	 */
	public boolean doneMap()
		throws IOException
	{
		char c;
		while( true )
		{
			c = read();
			switch( c )
			{
				case ',':
					return false;

				case '}':
					return true;

				// whitespace
				case ' ':
				case '\t':
				case '\r':
				case '\n':
					break;

				case EOF:
					throw new ParseException( "EOF, expected '}'", line, pos );

				default:
					throw new ParseException( "next key not found", ',', c, line, pos );
			}
		}
	}

	public String parseKey()
		throws IOException
	{
		char c;
		while( true )
		{
			c = read();
			switch( c )
			{
				case '"':
				case '\'':
					return readString( c );

				// whitespace
				case ' ':
				case '\t':
				case '\r':
				case '\n':
					break;

				case '}':
					// empty map
					return null;

				case EOF:
					throw new ParseException( "EOF, expected '}'", line, pos );

				default:
					throw new ParseException( '}', c, line, pos );
			}
		}
	}

	public void parseColon()
		throws IOException
	{
		char c;
		while( true )
		{
			c = read();
			switch( c )
			{
				case ':':
					return;

				// whitespace
				case ' ':
				case '\t':
				case '\r':
				case '\n':
					break;

				case EOF:
					throw new ParseException( "EOF, expected ':'", line, pos );

				default:
					throw new ParseException( ':', c, line, pos );
			}
		}
	}

	public List<Object> parseList( Reader reader )
		throws IOException
	{
		reset();
		this.reader = reader;

		char c;
		while( true )
		{
			c = read();
			switch( c )
			{
				case '[':
					return parseList();

				// whitespace
				case ' ':
				case '\t':
				case '\r':
				case '\n':
					break;

				case EOF:
					throw new ParseException( "EOF, expected '['", line, pos );

				default:
					throw new ParseException( "list not found", '[', c, line, pos );
			}
		}
	}

	public List<Object> parseList()
		throws IOException
	{
		List<Object> list = createList();

		while( true )
		{
			Object item = parseValue();

			// check for magic value representing end of list
			if( item == this ) return list;

			list.add( item );

			if( doneList() ) return list;
		}
	}

	/**
	 * Returns true if next char is a comma ',', false if right square bracket ']'.
	 * Eats leading whitespace.
	 * Otherwise throws an exception.
	 */
	public boolean doneList()
		throws IOException
	{
		char c;

		while( true )
		{
			c = read();
			switch( c )
			{
				case ',':
					return false;

				case ']':
					return true;

				// whitespace
				case ' ':
				case '\t':
				case '\r':
				case '\n':
					break;

				case EOF:
					throw new ParseException( "EOF, expected ']'", line, pos );

				default:
					throw new ParseException( "next item not found", ',', c, line, pos );
			}
		}
	}

	public Object parseValue()
		throws IOException
	{
		while( true )
		{
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

				case '[':
					return parseList();

				case '{':
					return parseMap();

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

				case ']':
					// magic value to represent end of list
					return this;

				case EOF:
					throw new ParseException( "EOF, expected value", line, pos );

				default:
					throw new ParseException( "expected value", line, pos );

			}
		}
	}

    public String readString( char delim )
		throws IOException
	{
		sb.setLength( 0 );
		mark();
		char c;
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
//							int hex =
//								readHex() << 12;
//							hex += readHex() << 8;
//							hex += readHex() << 4;
//							hex += readHex();
							sb.append( (char) hex );
//							char hex = readHexZ();
//							sb.append( (char) hex );
							break;

						default:
							throw new ParseException( "what is '\\" + c + "'?", line, pos );
					}

					mark();
					break;
				}
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
		sb.append( c );
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

		Number result;
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

	/*
	'0' 48
	'9' 57
	'A' 65
	'H' 72
	'a' 97
	'h' 104
	 */
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
		throw new IOException( "not a hex digit " + x );
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
			throw new IOException( "not a hex digit " + x );
		}
		return (char) result;
	}

	public void consume( char expected )
		throws IOException
	{
		char c = read();
		if( c != expected )
		{
			throw new ParseException( expected, c, line, pos );
		}
	}

	public void reset() {
		line = 1;
		pos = 0;

		nth = -1;
		limit = 0;

		marked = false;
		mark = -1;

		last = 0;
		back = false;
	}

	/**
	 * Tracks character count, line count, line position. Loads+ buffer. Does bulk copy.
	 *
	 */
	char read()
		throws IOException
	{
		if( back )
		{
			back = false;
			return last;
		}

		nth++;
		// refill buffer as needed
		if( nth == limit )
		{
			if( marked && mark < nth )
			{
				sb.append( buf, mark + 1, nth - mark - 1 );
			}
			limit = reader.read( buf, 0, SIZE );
			if( limit == -1 )
			{
				return (char) -1;
			}
			nth = 0;
			mark = -1;
		}
		char c = buf[ nth ];
		last = c;

		if( c == '\n' )
		{
			line++;
			pos = 0;
		}
		else
		{
			pos++;
		}

		return c;
	}

	void pushBack()
	{
		back = true;
	}

	void mark()
	{
		mark = nth;
		marked = true;
	}

//	void fill()
//	{
//		if( marked && mark < nth )
//		{
//			sb.append( buf, mark + 1, nth - mark - 1 );
//		}
//		marked = false;
//	}

	void fill()
	{
		if( marked && mark < nth )
		{
			fill2();
		}
		marked = false;
	}

	void fill2()
	{
		sb.append( buf, mark + 1, nth - mark - 1 );
	}

	/**
	 * Override this method to use a different Map implementation. eg LinkedHashMap
	 * would preserve file order. ArrayMap would be more space efficient.
	 *
	 * @return
	 */
	public Map<String, Object> createMap()
	{
		return new LinkedHashMap<>();
	}


	/**
	 * Override this method to use a different List implementation. For whatever reason.
	 *
	 * @return
	 */
	public List<Object> createList()
	{
		return new ArrayList<>();
	}

}
