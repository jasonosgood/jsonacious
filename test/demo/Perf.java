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

		FileReader reader = new FileReader( "./data/whatever.json" );
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

		long start = System.currentTimeMillis();
		int reps = 23809;
		for( int n = 0; n < reps; n++ )
		{
			Map map = jsonReader.parse( sparky );
			sparky.reset();

		}
		long elapsed = System.currentTimeMillis() - start;

		System.out.printf( "elapsed: %d msec \n", elapsed );
		System.out.printf( "each: %f msec \n", (double) elapsed / (double) reps );
	}


}
