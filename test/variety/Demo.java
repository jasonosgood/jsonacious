package variety;

import jsonacious.JSONBaker;
import jsonacious.JSONReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Demo
{

    public static void main( String[] args )
        throws Exception
	{
//		{
//		FileReader reader = new FileReader( "/Users/jasonosgood/Projects/jsonacious/test/variety/variety.json" );
//
//		JSONReader jsonReader = new JSONReader();
//
//		Map map = jsonReader.map( reader );
//		System.out.println( map );
//		}

//		Integer ugh = new Integer( 0 );
//		ugh.shortValue();
//		short bah = ((Number) ugh).shortValue();
//		Class a = short.class;
//		System.out.println( a.getTypeName() );
//		System.out.println( a.getName() );
//		Class b = Short.class;
//		System.out.println( b.getTypeName() );
//		System.out.println( b.getName() );

		{
			FileReader reader = new FileReader( "/Users/jasonosgood/Projects/jsonacious/test/variety/variety.json" );

			JSONBaker baker = new JSONBaker();
			Variety variety = baker.parse( reader, Variety.class );
			System.out.println( variety );
		}
	}


}
