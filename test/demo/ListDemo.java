package demo;

import jsonacious.JSONReader;

import java.io.File;

public class ListDemo
{
    public static void main( String[] args )
        throws Exception
	{
		File file = new File( "./test/demo/ListList.json" );
		System.out.println( file.getCanonicalFile() );

		JSONReader jsonReader = new JSONReader();
//		List map = jsonReader.parse( file, ArrayList.class );
//		System.out.println( map );
	}
}
