package demo;

import jsonacious.JSONDecoder;

import java.io.FileReader;

/**
 */
public class DemoDecode
{

    public static void main( String[] args )
        throws Exception
	{
		JSONDecoder jsonReader = new JSONDecoder();
//		payload = "{ 'A' : 'apple', 'B' : {} }";
		FileReader reader = new FileReader( "./data/whatever.json.pwd" );
//		StringBuilder sb = new StringBuilder();
//		while( true )
//		{
//			int c = reader.read();
//			if( c == -1 ) break;
//			sb.append( (char) c );
//		}
//		String payload = sb.toString();
//
//		StringReader sparky = new StringReader( payload );
		MediaItem map = jsonReader.parse( reader, MediaItem.class );
		System.out.println( map );
	}


}
