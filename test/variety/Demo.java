package variety;

import jsonacious.JSONReader;
import java.io.FileReader;

public class Demo
{

    public static void main( String[] args )
        throws Exception
	{
		FileReader reader = new FileReader( "/Users/jasonosgood/Projects/jsonacious/test/variety/variety.json" );

		JSONReader baker = new JSONReader();
		Variety variety = baker.parse( reader, Variety.class );
		System.out.println( variety );
	}
}
