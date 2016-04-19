package jsonacious;

import java.io.IOException;

import static java.lang.String.format;

public class ParseException extends IOException {
    public String blurb;
	public char expected;
	public char found;
    public int line;
	public int pos;

    public ParseException( Throwable cause, int line, int pos )
    {
        super( cause );
        this.line = line;
        this.pos = pos;
    }

    public ParseException( Class clazz, Exception cause, int line, int pos )
    {
        super( format( "cannot create new instanceof %s (line: %d pos: %d)", clazz, line, pos ), cause );
        this.line = line;
        this.pos = pos;
    }

    public ParseException( String blurb, int line, int pos )
    {
        super( format( "%s (line: %d pos: %d)", blurb, line, pos ) );
        this.blurb = blurb;
        this.line = line;
        this.pos = pos;
    }

    public ParseException( char expected, char found, int line, int pos )
    {
        super( format( "expected '%c', found '%c' (line: %d pos: %d)", expected, found, line, pos ) );
        this.expected = expected;
        this.found = found;
        this.line = line;
        this.pos = pos;
    }

    public ParseException( String blurb, char expected, char found, int line, int pos )
    {
        super( format( "%s: expected '%c', found '%c' (line: %d pos: %d)", blurb, expected, found, line, pos ) );

        this.blurb = blurb;
        this.expected = expected;
        this.found = found;
        this.line = line;
        this.pos = pos;
    }


}
