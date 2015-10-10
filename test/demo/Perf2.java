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

//		FileReader reader = new FileReader( "./data/whatever.json.pwd" );
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

//		long start = 0;
//		int reps = 100000;
//		int warmup = 50000;
//		for( int n = 0; n < reps + warmup; n++ )
//		{
//			if( n ==  warmup)
//			{
//				start = System.currentTimeMillis();
//			}
//			Map map = jsonReader.parseRoot( sparky );
//			sparky.reset();
//		}
//		long elapsed = System.currentTimeMillis() - start;
//
//		System.out.printf( "elapsed: %d msec \n", elapsed );
//		System.out.printf( "each: %f msec \n", (double) elapsed / (double) reps );

		for( int x = 0; x < 10; x++ )
		{
			long start = System.currentTimeMillis();
//			int reps = 5;
			int reps = 50000;
			for( int n = 0; n < reps; n++ )
			{
//			if( n ==  warmup)
//			{
//				start = System.currentTimeMillis();
//			}
//			Map map = jsonReader.parseRoot( payload );
//				StringReader sparky = new StringReader( payload );

				Map map = jsonReader.parse( payload );
//				sparky.reset();
			}
			long elapsed = System.currentTimeMillis() - start;

			System.out.printf( "elapsed: %d msec \n", elapsed );
			System.gc();
//			System.out.printf( "each: %f msec \n", (double) elapsed / (double) reps );
			Thread.sleep( 1000 );
		}


	}


}
