package demo;

import jsonacious.JSONReader;
import jsonacious.JSONWriter;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class RoundTripMap
{
	public static void main( String[] args )
		throws Exception
	{
		File in = new File( "./data/content.json" );
		File out = new File( "./data/content.RoundTripMap.json" );
		System.out.println( in.getCanonicalFile() );

		JSONReader reader = new JSONReader();
		Map map = reader.parse( in, LinkedHashMap.class );
//		System.out.println( map );

//		FileWriter fw = new FileWriter( out );
//		JSONWriter writer = new JSONWriter( fw );
		JSONWriter writer = new JSONWriter();
		writer.write( map );
		writer.close();
	}
}
