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
		FileReader reader = new FileReader( "./data/whatever.json.pwd" );
		MediaItem map = jsonReader.parse( reader, MediaItem.class );
		System.out.println( map );
	}


}
