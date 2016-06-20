package jsonacious;
import jsonacious.Reflector;
import jsonacious.JSONWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class FruitReflectorZ extends Reflector
{
	char[] key01Chars = "key01".toCharArray();
	char[] key02Chars = "key02".toCharArray();
	char[] key03Chars = "key03".toCharArray();

	public int toField( char[] value, int offset, int count )
	{
		if( equals( key01Chars, value, offset, count ) ) return 1;
		if( equals( key02Chars, value, offset, count ) ) return 2;
		if( equals( key03Chars, value, offset, count ) ) return 3;
		return 0;
	}

	public void put( Object target, int field, Object value )
	{
		jsonacious.Fruit temp = (jsonacious.Fruit) target;
		switch( field )
		{
			case 1:
				temp.key01 = (java.lang.String) value;
				break;
			case 2:
				temp.key02 = (java.lang.String) value;
				break;
			case 3:
				temp.key03 = (java.lang.String) value;
				break;
		}
	}

	Type key01Type;
	Type key02Type;
	Type key03Type;

	public FruitReflectorZ()
	{
		try
		{
			key01Type = jsonacious.Fruit.class.getField( "key01" ).getGenericType();
			key02Type = jsonacious.Fruit.class.getField( "key02" ).getGenericType();
			key03Type = jsonacious.Fruit.class.getField( "key03" ).getGenericType();
		}
		catch( NoSuchFieldException e ) { e.printStackTrace(); }
	}

	public Type getValueType( int field )
	{
		switch( field )
		{
			case 1: return key01Type;
			case 2: return key02Type;
			case 3: return key03Type;
		}
		return null;
	}

	public void write( JSONWriter writer, Object source )
		throws IOException
	{
		jsonacious.Fruit temp = (jsonacious.Fruit) source;
		writer.leftSquiggle();

		writer.writePair( "key01", temp.key01 );
		writer.comma();
		writer.writePair( "key02", temp.key02 );
		writer.comma();
		writer.writePair( "key03", temp.key03 );
		writer.rightSquiggle();
	}

}

