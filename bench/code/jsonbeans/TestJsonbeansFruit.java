package jsonbeans;

import com.esotericsoftware.jsonbeans.Json;
import jsonacious.Fruit;

import java.nio.file.Files;
import java.nio.file.Paths;


public class TestJsonbeansFruit
{
	public static final void main( String[] args )
		throws Exception
	{

		Json reader = new Json();
		String content = new String( Files.readAllBytes( Paths.get( "./bench/data/fruit.json" ) ) );

//		FruitReflectorZ reflector = new FruitReflectorZ();
//		Reflector.add( Fruit.class, reflector );

		Fruit value = null;

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
				value = reader.fromJson(  Fruit.class, content  );
			}
		}

		long running = 0;

		for( int nth = 0; nth < iterations; nth++ )
//		for( int nth = 0; true; nth++ )
		{
			long start = System.currentTimeMillis();
			for( int ugh = 0; ugh < testrun; ugh++ )
			{
				value = reader.fromJson(  Fruit.class, content  );
			}
			long elapsed = System.currentTimeMillis() - start;
			running += elapsed;

			System.out.printf( "\nrun: %d elapsed: %d", nth, elapsed );
//			Thread.sleep( 1000 );
		}

		System.out.printf( "\naverage: %d", ( running / iterations ));

		// defeat dead code elimination
		System.out.println();
		System.out.println( value );

	}
}
