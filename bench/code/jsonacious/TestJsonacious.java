package jsonacious;


import java.nio.file.Files;
import java.nio.file.Paths;


public class TestJsonacious
{
	public static final void main( String[] args )
		throws Exception
	{

		JSONReader reader = new JSONReader();
//		String content = new String( Files.readAllBytes( Paths.get( "./bench/data/media.json" ) ) );
		String content = new String( Files.readAllBytes( Paths.get( "./bench/data/mediacontent.json" ) ) );

		MediaContent value = null;

		int warmup = 1000;
		int iterations = 30;
		int testrun = 10000;

		for( int nth = 0; nth < iterations; nth++ )
		{
			for( int ugh = 0; ugh < warmup; ugh++ )
			{
				value = reader.parse( content, MediaContent.class );
			}
		}

		long running = 0;

		for( int nth = 0; nth < iterations; nth++ )
		{
			long start = System.currentTimeMillis();
			for( int ugh = 0; ugh < testrun; ugh++ )
			{
				value = reader.parse( content, MediaContent.class );
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
