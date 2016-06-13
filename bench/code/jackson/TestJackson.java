package jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

import java.nio.file.Files;
import java.nio.file.Paths;

//import java.io.File;

public class TestJackson
{
	public static final void main( String[] args )
		throws Exception
	{

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule( new AfterburnerModule() );
//		File file = new File( "./bench/data/media.1.cks" );
//		MediaContent value = mapper.readValue( file, MediaContent.class );
		String content = new String( Files.readAllBytes( Paths.get( "./bench/data/media.1.cks" ) ) );
//		System.out.println( content );

		MediaContent value = null;

		int warmup = 1000;
		int iterations = 30;
		int testrun = 10000;

		for( int nth = 0; nth < iterations; nth++ )
		{
			for( int ugh = 0; ugh < warmup; ugh++ )
			{
				value = mapper.readValue( content, MediaContent.class );
			}
		}

		long running = 0;

		for( int nth = 0; nth < iterations; nth++ )
		{
			long start = System.currentTimeMillis();
			for( int ugh = 0; ugh < testrun; ugh++ )
			{
				value = mapper.readValue( content, MediaContent.class );
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
