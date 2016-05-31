package demo;

import jsonacious.JSONBaker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListDemo
{
    public static void main( String[] args )
        throws Exception
	{
		File file = new File( "./test/demo/ListList.json" );
		System.out.println( file.getCanonicalFile() );

		JSONBaker jsonReader = new JSONBaker();
//		List map = jsonReader.parse( file, ArrayList.class );
//		System.out.println( map );
	}
}
