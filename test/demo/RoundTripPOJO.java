package demo;

import jsonacious.JSONReader;
import jsonacious.JSONWriter;

import java.io.File;
import java.io.FileWriter;

public class RoundTripPOJO
{
	public static void main( String[] args )
		throws Exception
	{
		File in = new File( "./data/content.json" );
		File out = new File( "./data/content.RoundTripPOJO.json" );
		System.out.println( in.getCanonicalFile() );

		JSONReader reader = new JSONReader();
		Content content = reader.parse( in, Content.class );
//		System.out.println( map );

//		FileWriter fw = new FileWriter( out );
//		JSONWriter writer = new JSONWriter( fw );
		JSONWriter writer = new JSONWriter();
		writer.write( content );
		writer.close();

	}
}
