package demo;

import jsonacious.JSONReader;
import jsonacious.JSONWriter;

import java.io.FileReader;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jasonosgood on 8/25/14.
 */
public class MyWrites
{
	public static void main( String[] args )
		throws Exception
	{
		FileReader reader = new FileReader( "./data/whatever.json.pwd" );
		JSONReader jsonReader = new JSONReader();
		Map map = jsonReader.parse( reader );
//		LinkedHashMap<String,Object> map = new LinkedHashMap<>();
//		map.put( "A", "apples" );
//		map.put( "B", "banana" );
//		map.put( "C", "cherry" );
//		map.put( "D", null );
//		map.put( "E", true );
//		map.put( "F", false );
//		map.put( "G", 1.0 );
//		map.put( "H", 100 );
		StringWriter sw = new StringWriter();
		JSONWriter writer = new JSONWriter();
		writer.setWriter( sw );
		writer.write( map );
		writer.close();

		System.out.println( sw.toString() );

//		long start = System.currentTimeMillis();
//		int reps = 100000;
//		int warmup = 50000;
//		for( int n = 0; n < reps + warmup; n++ )
//		{
//			if( n ==  warmup)
//			{
//				start = System.currentTimeMillis();
//			}
//			StringWriter sw2 = new StringWriter();
//			writer.setWriter( sw2 );
//
//			writer.write( map );
//		}
//		long elapsed = System.currentTimeMillis() - start;
//
//		System.out.printf( "elapsed: %d msec \n", elapsed );
//		System.out.printf( "each: %f msec \n", (double) elapsed / (double) reps );

	}
}
