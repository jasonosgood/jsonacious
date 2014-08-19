package demo;

import jsonacious.JSONReader;

import java.io.FileReader;
import java.io.StringReader;
import java.util.Map;

/**
 */
public class Perf
{

	public static void main( String[] args )
		throws Exception
	{
		JSONReader jsonReader = new JSONReader();
//
		FileReader reader = new FileReader( "./data/whatever.json" );
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
		StringReader sparky = new StringReader( payload );
//		sparky.reset();

		long start = System.currentTimeMillis();
		int reps = 100000;
		int warmup = 50000;
		for( int n = 0; n < reps + warmup; n++ )
		{
			if( n ==  warmup)
			{
				start = System.currentTimeMillis();
			}
//			Map map = jsonReader.parse( payload );
			Map map = jsonReader.parse( sparky );
			sparky.reset();
		}
		long elapsed = System.currentTimeMillis() - start;

		System.out.printf( "elapsed: %d msec \n", elapsed );
		System.out.printf( "each: %f msec \n", (double) elapsed / (double) reps );
	}


}
