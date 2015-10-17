package maker;

import jsonacious.JSONMaker;
import jsonacious.JSONReader;

import java.io.FileReader;
import java.util.Map;

/**
 */
public class LoadContentJSON
{

	public static void main( String[] args )
		throws Exception
	{
        JSONMakerContent jsonReader = new JSONMakerContent();
		FileReader reader = new FileReader( "./data/content.json" );
		StringBuilder sb = new StringBuilder();
		while( true )
		{
			int c = reader.read();
			if( c == -1 ) break;
			sb.append( (char) c );
		}
		String payload = sb.toString();
        Content map = null;


		for( int x = 0; x < 10; x++ )
		{
			long start = System.currentTimeMillis();
			int reps = 50000;
			for( int n = 0; n < reps; n++ )
			{
				map = jsonReader.parse( payload, Content.class );
			}
			long elapsed = System.currentTimeMillis() - start;

			System.out.printf( "elapsed: %d msec \n", elapsed );
			System.gc();
			Thread.sleep( 1000 );
		}
        System.out.println( "map: " + map );
    }


}
