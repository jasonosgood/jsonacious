package jsonbeans;

import com.esotericsoftware.jsonbeans.Json;
import jsonacious.Fruit;
import jsonacious.MediaContent;

import java.nio.file.Files;
import java.nio.file.Paths;


public class TestJsonbeansFruit
{
	public static final void main( String[] args )
		throws Exception
	{

		Json reader = new Json();
		String content = new String( Files.readAllBytes( Paths.get( "./bench/data/mediacontent.json" ) ) );
//		String content = new String( Files.readAllBytes( Paths.get( "./bench/data/fruit.json" ) ) );

		MediaContent value = null;

		int warmup = 1000;
		int iterations = 30;
		int testrun = 10000;

//		int warmup = 1;
//		int iterations = 1;
//		int testrun = 10;

		for( int nth = 0; nth < iterations; nth++ )
		{
			for( int ugh = 0; ugh < warmup; ugh++ )
			{
				value = reader.fromJson(  MediaContent.class, content  );
			}
		}

		long running = 0;

		for( int nth = 0; nth < iterations; nth++ )
		{
			long start = System.currentTimeMillis();
			for( int ugh = 0; ugh < testrun; ugh++ )
			{
				value = reader.fromJson(  MediaContent.class, content  );
			}
			long elapsed = System.currentTimeMillis() - start;
			running += elapsed;

			System.out.printf( "\nrun: %d elapsed: %d", nth, elapsed );
		}

		System.out.printf( "\naverage: %d", ( running / iterations ));

		// defeat dead code elimination
		System.out.println();
		System.out.println( value );

	}
}
