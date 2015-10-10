package demo;

import jsonacious.JSONDecoder;
import jsonacious.JSONReader;

import java.io.FileReader;
import java.util.Map;

/**
 */
public class PerfDecode
{

	public static void main( String[] args )
		throws Exception
	{
		JSONDecoder jsonReader = new JSONDecoder();
//
		FileReader reader = new FileReader( "/Users/jasonosgood/Projects/jsonacious/data/whatever.json.pwd" );
		StringBuilder sb = new StringBuilder();
		while( true )
		{
			int c = reader.read();
			if( c == -1 ) break;
			sb.append( (char) c );
		}
		String payload = sb.toString();

//		String payload = "{ 'A' : 'abcdefgh\\r\\n\\t\\u0064ijklmnopqrstuvwxyz' }";
//		String payload = "{ 'A' : 'a \\r b \\n c \\t d \\u0064 e' }";
//		String payload = "{ 'A' : '1\\u0064 1\\u0064 1\\u0064 11\\u006411\\u0064111\\u00641111\\u0064' }";
//		String payload = "{ 'A' : '1\\u0064 1\\u0064 1\\u0064' }";
//		String payload = "{ 'A' : '1\\u00641\\u0064 X' }";
//		String payload = "{ 'A' : 'a\\rb' }";
//		String payload = "{ 'A' : 'd \\u0064 e' }";
//		String payload = "{ 'A' : '\\ra' }";
//		sparky.reset();
		MediaItem map = null;

		for( int x = 0; x < 10; x++ )
		{
			long start = System.currentTimeMillis();
			int reps = 50000;
			for( int n = 0; n < reps; n++ )
			{
//			if( n ==  warmup)
//			{
//				start = System.currentTimeMillis();
//			}
//			Map map = jsonReader.parseRoot( payload );
//				StringReader sparky = new StringReader( payload );

				map = jsonReader.parse( payload, MediaItem.class );
//				sparky.reset();
			}
			long elapsed = System.currentTimeMillis() - start;

			System.out.printf( "elapsed: %d msec \n", elapsed );
//			System.out.printf( "each: %f msec \n", (double) elapsed / (double) reps );
			Thread.sleep( 1000 );
		}
	}


}
