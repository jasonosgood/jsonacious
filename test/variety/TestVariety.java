package variety;

import jsonacious.JSONReader;
import jsonacious.Reflector;

import java.io.FileReader;

public class TestVariety
{

	public final static void main( String[] args )
		throws Exception
	{
		FileReader reader = new FileReader( "/Users/jasonosgood/Projects/jsonacious/test/variety/variety.json" );

		JSONReader baker = new JSONReader();
		VarietyReflector reflector = new VarietyReflector();
		Reflector.add( Variety.class, reflector );
		Variety variety = baker.parse( reader, Variety.class );
		System.out.println( "variety " + variety );
	}
}
