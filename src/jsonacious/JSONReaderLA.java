package jsonacious;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class JSONReaderLA
{
	public final static char EOF = (char) -1;
	final static int SIZE = 4096;

	char[] buf = new char[ SIZE ];

	Reader reader = null;
	StringBuilder sb = new StringBuilder();
	int line = 1;
	int pos = 0;
	int nth = -1;
	int limit = 0;
	int mark = -1;
	char last = 0;
	boolean marked = false;

	/**
	 * Returns either Map or List.
	 */
	public Object parse( String content )
		throws IOException
	{
		reset();
		// Fully "read" String into buffer
		buf = content.toCharArray();
		limit = buf.length;
		return root();
	}

	/**
	 * Returns either Map or List.
	 */
	public Object parse( Reader reader )
		throws IOException
	{
		reset();
		this.reader = reader;
		return root();
	}

	Object root()
		throws IOException
	{
		switch( la() )
		{
			case '[':
				return list();

			case '{':
				return map();

			case EOF:
				throw new ParseException( "EOF, expected '{' or '['", line, pos );

			default:
				throw new ParseException( "map not found", '{', la(), line, pos );
		}
	}

	public Map<String, Object> map( Reader reader )
		throws IOException
	{
		reset();
		this.reader = reader;

		switch( la() )
		{
			case '{':
				return map();

			case EOF:
				throw new ParseException( "EOF, expected '{'", line, pos );

			default:
				throw new ParseException( "map not found", '{', la(), line, pos );
		}
	}

	public Map<String, Object> map( String content )
		throws IOException
	{
		reset();
		// Fully "read" String into buffer
		buf = content.toCharArray();
		limit = buf.length;

		switch( la() )
		{
			case '{':
				return map();

			case EOF:
				throw new ParseException( "EOF, expected '{'", line, pos );

			default:
				throw new ParseException( "map not found", '{', la(), line, pos );
		}
	}

	public Map<String, Object> map()
		throws IOException
	{
		consume();

		Map<String, Object> map = createMap();

		if( la() == '}' )
		{
			consume();
			return map;
		}

		while( true )
		{
			String key = string();

			consume( ':' );

			Object value = value();

			map.put( key, value );

			switch( la() )
			{
				case '}':
					consume();
					return map;

				case ',':
					consume();
					continue;

				case EOF:
					throw new ParseException( "EOF, expected '}'", line, pos );

				default:
					throw new ParseException( "map not closed", '}', la(), line, pos );

			}
		}
	}

	public List<Object> list( Reader reader )
		throws IOException
	{
		reset();
		this.reader = reader;

		switch( la() )
		{
			case '[':
				return list();

			case EOF:
				throw new ParseException( "EOF, expected '['", line, pos );

			default:
				throw new ParseException( "list not found", '[', la(), line, pos );
		}
	}

	public List<Object> list()
		throws IOException
	{
		consume();

		List<Object> list = createList();

		if( la() == ']' )
		{
			consume();
			return list;
		}

		while( true )
		{
			Object value = value();

			list.add( value );

			switch( la() )
			{
				case ']':
					consume();
					return list;

				case ',':
					consume();
					continue;

				case EOF:
					throw new ParseException( "EOF, expected ']'", line, pos );

				default:
					throw new ParseException( "list not closed", ']', la(), line, pos );

			}
		}
	}

	public Object value()
		throws IOException
	{
		mark();
		switch( la() )
		{
			case '"':
				return string();

			case '[':
				return list();

			case '{':
				return map();

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
				return number();

			case 'n':
				consume();
				consume( 'u' );
				consume( 'l' );
				consume( 'l' );
				return null;

			case 't':
				consume();
				consume( 'r' );
				consume( 'u' );
				consume( 'e' );
				return Boolean.TRUE;

			case 'f':
				consume();
				consume( 'a' );
				consume( 'l' );
				consume( 's' );
				consume( 'e' );
				return Boolean.FALSE;

			case EOF:
				throw new ParseException( "EOF, expected value", line, pos );

			default:
				throw new ParseException( "expected value", line, pos );

		}
	}

	public String string()
		throws IOException
	{
		consume( '"' );

		sb.setLength( 0 );

		mark();

		while( true )
		{
			char c = read();

			switch( c )
			{
				case '"':
					fill();
					return sb.toString();

				case EOF:
					throw new ParseException( "EOF, expected '\"'", line, pos );

				case '\\':
					escapedString();
					break;
			}
		}
	}

	public void escapedString()
		throws IOException
	{
		fill();
		switch( la() )
		{
			case '"':
				sb.append( '"' ); // ascii 34
				break;

			case '/':
				sb.append( '/' ); // ascii 47
				break;

			case '\\':
				sb.append( '\\' ); // ascii 92
				break;

			case 'b':
				sb.append( '\b' ); // ascii 08
				break;

			case 't':
				sb.append( '\t' ); // ascii 09
				break;

			case 'n':
				sb.append( '\n' ); // ascii 10
				break;

			case 'f':
				sb.append( '\f' ); // ascii 12
				break;

			case 'r':
				sb.append( '\r' ); // ascii 13
				break;

			case 'u':
				char hex = hex();
				sb.append( hex );
				break;

			default:
				throw new ParseException( "what is '\\" + la() + "'?", line, pos );
		}
		consume();
		mark();
	}

	public Number number()
		throws IOException
	{
		sb.setLength( 0 );

		boolean decimal = false;
//		loop:
		while( true )
		{
			switch( la() )
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
					consume();
					break;

				case EOF:
					throw new IOException( "unexpected end of file" );

				default:
					fill();
					String value = sb.toString().trim();

					return toNumber( value, decimal );
			}
		}
	}

	public Number toNumber( String value, boolean decimal )
		throws IOException
	{
		Number result;
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

		return result;
	}

	public char hex()
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

	public void reset()
	{
		line = 1;
		pos = 0;

		nth = -1;
		limit = 0;

		marked = false;
		mark = -1;

		last = 0;
	}

	char current = 0;
	boolean la = true;

	char la()
		throws IOException
	{
		if( la )
		{
			la = false;
			return whitespace();
		}
		return current;
	}

	public char whitespace()
		throws IOException
	{
		while( true )
		{
			char c = read();
			switch( c )
			{
				// whitespace
				case '\n':
				case ' ':
				case '\t':
				case '\r':
					continue;

				// content
				default:
					current = c;
					pos++;
					return c;
			}
		}
	}

	void consume()
	{
		la = true;
	}

	void consume( char expected )
		throws IOException
	{
		if( la() != expected )
		{
			throw new ParseException( expected, la(), line, pos );
		}

		la = true;
	}

	// tracks line and character position
	char read()
		throws IOException
	{
		nth++;

		// refill buffer as needed
		if( nth == limit )
		{
			fill();
			limit = reader.read( buf, 0, SIZE );
			if( limit == -1 )
			{
				return EOF;
			}
			nth = 0;
			mark = -1;
		}
		char c = buf[ nth ];
		if( c == '\n' )
		{
			line++;
			pos = 0;
		} else
		{
			pos++;
		}
		return c;
	}

	void mark()
	{
		mark = nth;
		marked = true;
	}

	void fill()
	{
		if( marked && mark < nth )
		{
			fill2();
		}
		marked = false;
	}

	// Split method to enable JIT
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
//		return new HashMap<>();
	}

	/**
	 * Override this method to use a different List implementation. For whatever reason.
	 *
	 * @return
	 */
	public List createList()
	{
		return new ArrayList<>();
	}

}
