package variety;

import jsonacious.JSONBaker;
import jsonacious.Reflector;

import java.io.FileReader;

public class TestVariety
{

	public final static void main( String[] args )
		throws Exception
	{
		FileReader reader = new FileReader( "/Users/jasonosgood/Projects/jsonacious/test/variety/variety.json" );

		JSONBaker baker = new JSONBaker();
//		VarietyReflector3 reflector = new VarietyReflector3();
//		Reflector.add( Variety.class, reflector );
		Variety variety = baker.parse( reader, Variety.class );
		System.out.println( "variety " + variety );
	}
}
