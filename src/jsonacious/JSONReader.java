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

	public Map<String, Object> parse( Reader reader )
		throws IOException
	{
		nth = 0;
		line = 0;
		pos = 0;
		last = 0;
		back = false;

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

	public Map<String, Object> parseMap( Reader reader )
		throws IOException
	{
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
		List<Object> parent = createList();

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

	/**
	 * Override this method to use a different List implementation. For whatever reason.
	 *
	 * @return
	 */
	private List<Object> createList()
	{
		return new ArrayList<Object>();
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
