package jsonacious;

import java.io.FileWriter;
import java.io.IOException;

public class Shazam
{
	public static final void main( String[] args )
		throws Exception
	{
//		String source = g.generate( jsonacious.Fruit.class );
		gosh( Image.class );
		gosh( Media.class );
		gosh( MediaContent.class );
	}

	public static void gosh( Class cls ) throws
		IOException
	{
		Generator g = new Generator();

		String source = g.generate( cls );
		System.out.println( source );
		String fileName = String.format( "./bench/code/jsonacious/%sReflector.java", cls.getSimpleName() );
		FileWriter w = new FileWriter( fileName );
		w.write( source );
		w.close();
	}

}
