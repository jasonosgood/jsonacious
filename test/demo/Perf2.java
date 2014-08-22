package demo;

import jsonacious.JSONNullReader;
import jsonacious.JSONReader;

import java.io.FileReader;
import java.io.StringReader;
import java.util.Map;

/**
 */
public class Perf2
{

    public static void main( String[] args )
        throws Exception
	{
		JSONNullReader jsonReader = new JSONNullReader();

		FileReader reader = new FileReader( "./data/whatever.json.pwd" );
		StringBuilder sb = new StringBuilder();
		while( true )
		{
			int c = reader.read();
			if( c == -1 ) break;
			sb.append( (char) c );
		}
		String payload = sb.toString();

		StringReader sparky = new StringReader( payload );
		sparky.reset();

		long start = 0;
		int reps = 100000;
		int warmup = 50000;
		for( int n = 0; n < reps + warmup; n++ )
		{
			if( n ==  warmup)
			{
				start = System.currentTimeMillis();
			}
			Map map = jsonReader.parse( sparky );
			sparky.reset();
		}
		long elapsed = System.currentTimeMillis() - start;

		System.out.printf( "elapsed: %d msec \n", elapsed );
		System.out.printf( "each: %f msec \n", (double) elapsed / (double) reps );

	}


}
