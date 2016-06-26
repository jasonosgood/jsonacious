package jsonacious;
import jsonacious.Reflector;
import jsonacious.JSONWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class MediaReflectorZ
	extends Reflector
{
	char[] personsChars = "persons".toCharArray();
	char[] playerChars = "player".toCharArray();
	char[] copyrightChars = "copyright".toCharArray();

	public int toField( char[] value, int offset, int count )
	{
		if( equals( personsChars, value, offset, count ) ) return 0;
		if( equals( playerChars, value, offset, count ) ) return 1;
		if( equals( copyrightChars, value, offset, count ) ) return 2;
		return 0;
	}

	public void put( Object target, int field, Object value )
	{
		jsonacious.Media temp = (jsonacious.Media) target;
		switch( field )
		{
			case 0:
				temp.persons = (java.util.ArrayList<java.lang.String>) value;
				break;
			case 1:
				jsonacious.Media.Player ugh = jsonacious.Media.Player.valueOf( (String) value );
				temp.player = ugh;
				break;
			case 2:
				temp.copyright = (java.lang.String) value;
				break;
		}
	}

	Type personsType;
	Type playerType;
	Type copyrightType;

	public MediaReflectorZ()
	{
		try
		{
			personsType = jsonacious.Media.class.getField( "persons" ).getGenericType();
			playerType = jsonacious.Media.class.getField( "player" ).getGenericType();
			copyrightType = jsonacious.Media.class.getField( "copyright" ).getGenericType();
		}
		catch( NoSuchFieldException e ) { e.printStackTrace(); }
	}

	public Type getValueType( int field )
	{
		switch( field )
		{
			case 0: return personsType;
			case 1: return playerType;
			case 2: return copyrightType;
		}
		return null;
	}

	public void write( JSONWriter writer, Object source )
		throws IOException
	{
		jsonacious.Media temp = (jsonacious.Media) source;
		writer.leftSquiggle();

		writer.writePair( "persons", temp.persons );
		writer.comma();
		writer.writePair( "player", temp.player );
		writer.comma();
		writer.writePair( "copyright", temp.copyright );
		writer.rightSquiggle();
	}

}

