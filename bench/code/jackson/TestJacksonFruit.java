package jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import jsonacious.Fruit;

import java.nio.file.Files;
import java.nio.file.Paths;

//import java.io.File;

public class TestJacksonFruit
{
	public static final void main( String[] args )
		throws Exception
	{

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule( new AfterburnerModule() );
//		File file = new File( "./bench/data/mediacontent.json" );
//		MediaContent value = mapper.readValue( file, MediaContent.class );
		String content = new String( Files.readAllBytes( Paths.get( "./bench/data/fruit.json" ) ) );
//		System.out.println( content );

		Fruit value = null;

		int warmup = 1000;
		int iterations = 30;
		int testrun = 10000;

		for( int nth = 0; nth < iterations; nth++ )
		{
			for( int ugh = 0; ugh < warmup; ugh++ )
			{
				value = mapper.readValue( content, Fruit.class );
			}
		}

		long running = 0;

		for( int nth = 0; nth < iterations; nth++ )
		{
			long start = System.currentTimeMillis();
			for( int ugh = 0; ugh < testrun; ugh++ )
			{
				value = mapper.readValue( content, Fruit.class );
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
