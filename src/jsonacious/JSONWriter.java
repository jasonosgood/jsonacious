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
		leftSquiggle();

		boolean second = false;

		for( Map.Entry<String,Object> entry : map.entrySet() )
		{
			if( second )
			{
				comma();
			}
			else
			{
				second = true;
			}
			tabs();
			writer.append( '"' );
			writer.append( entry.getKey() );
			writer.append( '"' );
			colon();

			Object value = entry.getValue();
			writeValue( value, false );
		}

		rightSquiggle();
	}

	void write( Collection collection )
		throws IOException
	{
		leftSquare();

		boolean second = false;
		for( Object value : collection )
		{
			if( second )
			{
				comma();
			}
			else
			{
				second = true;
			}
			writeValue( value, true );
		}

		rightSquare();
	}

	void writeValue( Object value, boolean list )
		throws IOException
	{
		if( value == null )
		{
			if( list ) tabs();
			writer.append( null );
		}
		else
		if( value instanceof Boolean )
		{
			if( list ) tabs();
			writer.append( ((Boolean) value).booleanValue() ? "true" : "false" );
		}
		else
		if( value instanceof Number )
		{
			if( list ) tabs();
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
			if( list ) tabs();
			writer.append( '"' );
			escapeChar( writer, value.toString() );
			writer.append( '"' );
		}
	}

	boolean pretty = true;
	int tabs = 0;

	boolean skipNewline = true;

	void newline()
		throws IOException
	{
		if( !pretty ) return;
		if( skipNewline )
		{
			skipNewline = false;
			return;
		}
		writer.append( '\n' );
		skipNewline = true;
	}

	void tabs()
		throws IOException
	{
		if( !pretty ) return;
		skipNewline = false;
		for( int x = 0; x < tabs; x++ )
		{
			writer.append( '\t' );
//			writer.append( ' ' );
//			writer.append( ' ' );
//			writer.append( ' ' );
//			writer.append( '|' );
		}
	}

	void leftSquiggle()
		throws IOException
	{
		newline();
		tabs();
		writer.append( '{' );
		newline();
		tabs++;
	}

	void rightSquiggle() throws
		IOException
	{
		tabs--;
		newline();
		tabs();
		writer.append( '}' );
	}

	void leftSquare()
		throws IOException
	{
		newline();
		tabs();
		writer.append( '[' );
		newline();
		tabs++;
	}

	void rightSquare() throws
		IOException
	{
		tabs--;
		newline();
		tabs();
		writer.append( ']' );
	}

	void comma() throws
		IOException
	{
		writer.append( ',' );
		newline();
	}

	void colon() throws
		IOException
	{
		if( pretty )
		{
			writer.append( ' ' );
			writer.append( ':' );
			writer.append( ' ' );
		}
		else
		{
			writer.append( ':' );
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
						w.append( c );
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
