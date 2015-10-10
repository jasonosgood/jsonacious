package jsonacious;

import java.io.IOException;

import static java.lang.String.format;

public class ParseException extends IOException {
    public ParseException( String msg, int line, int pos )
    {
        super( format( "%s (line: %d pos: %d)", msg, line, pos ));
    }

    public ParseException( char expected, char found, int line, int pos ) {
        super( format( "expected '%c', found '%c' (line: %d pos: %d)", found, expected, line, pos ) );
    }

}
