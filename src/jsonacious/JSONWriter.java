package jsonacious;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

/**
 * Created by jasonosgood on 8/25/14.
 */

// TODO: Loop / Cycle detection

public class
	JSONWriter
{
	Writer writer = new PrintWriter( System.out );

	public void setWriter( Writer writer )
	{
		if( writer == null )
		{
			throw new NullPointerException( "writer" );
		}
		this.writer = writer;
	}

	public Writer getWriter()
	{
		return writer;
	}

	public void write( Map<String, Object> map )
		throws IOException
	{
		writer.append( '{' );
		writer.append( '\n' );
		boolean second = false;
		for( Map.Entry<String,Object> entry : map.entrySet() )
		{
			if( second )
			{
				writer.append( ',' );
				writer.append( '\n' );

			}
			else
			{
				second = true;
			}
			writer.append( '"' );
			writer.append( entry.getKey() );
			writer.append( '"' );
			writer.append( ':' );

			Object value = entry.getValue();
			writeValue( value );
		}
		writer.append( '}' );
		writer.append( '\n' );
	}

	void write( Collection collection )
		throws IOException
	{
		writer.append( '[' );
		writer.append( '\n' );

		boolean second = false;
		for( Object value : collection )
		{
			if( second )
			{
				writer.append( ',' );
				writer.append( '\n' );

			}
			else
			{
				second = true;
			}
			writeValue( value );
		}
		writer.append( ']' );
		writer.append( '\n' );

	}

	void writeValue( Object value )
		throws IOException
	{
		if( value == null )
		{
			writer.append( null );
		}
		else
		if( value instanceof Boolean )
		{
			writer.append( ((Boolean) value).booleanValue() ? "true" : "false" );
		}
		else
		if( value instanceof Number )
		{
			writer.append( ((Number) value).toString() );
		}
		else
		if( value instanceof Collection )
		{
			write( (Collection) value );
		}
		else
		if( value instanceof Map )
		{
			write( (Map) value );
		}
		else
		{
			writer.append( '"' );
//			escapeChar( writer, value.toString() );
			writer.append( value.toString() );
			writer.append( '"' );
		}
	}

	void escapeChar( Writer w, String text )
		throws IOException
	{
		for( int i = 0; i < text.length(); i++ )
		{
			char c = text.charAt( i );
			if( c <= 0x1f )
			{
				w.append( '\\' );
				w.append( 'u' );
				w.append( '0' );
				w.append( '0' );
				w.append( Integer.toBinaryString( (int) c ) );
			}
			else
			{
				switch( c )
				{
					case '"':
						w.append( '\\' );
						w.append( '"' );
						break;

					case '\\':
						w.append( '\\' );
						w.append( '\\' );
						break;

					case '\t':
						w.append( '\\' );
						w.append( 't' );
						break;

					case '\b':
						w.append( '\\' );
						w.append( 'b' );
						break;

					case '\n':
						w.append( '\\' );
						w.append( 'n' );
						break;

					case '\r':
						w.append( '\\' );
						w.append( 'r' );
						break;

					case '\f':
						w.append( '\\' );
						w.append( 'f' );
						break;

					case '<':
						w.append( "\\u003c" );
						break;

					case '>':
						w.append( "\\u003e" );
						break;

					case '&':
						w.append( "\\u0026" );
						break;

					case '=':
						w.append( "\\u003d" );
						break;

					case '\'':
						w.append( "\\u0027" );
						break;

					default:
						break;
				}
			}
		}
	}

	public void close()
		throws IOException
	{
		writer.flush();
		writer.close();
	}
}
