package variety;

import jsonacious.JSONBaker;
import java.io.FileReader;

public class Demo
{

    public static void main( String[] args )
        throws Exception
	{
		FileReader reader = new FileReader( "/Users/jasonosgood/Projects/jsonacious/test/variety/variety.json" );

		JSONBaker baker = new JSONBaker();
		Variety variety = baker.parse( reader, Variety.class );
		System.out.println( variety );
	}
}
