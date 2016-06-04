package variety;

import jsonacious.JSONReader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Demo
{
    public static void main( String[] args )
        throws Exception
	{
		File file = new File( "./test/variety/variety.json" );
		System.out.println( file.getCanonicalFile() );

		JSONReader jsonReader = new JSONReader();
		Map map = jsonReader.parse( file, HashMap.class );
		System.out.println( map );
	}
}
