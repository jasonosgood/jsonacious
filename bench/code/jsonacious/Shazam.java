package jsonacious;

import java.io.FileWriter;

public class Shazam
{
	public static final void main( String[] args )
		throws Exception
	{
		Generator g = new Generator();
		String source = g.generate( jsonacious.Fruit.class );
		System.out.println( source );
		FileWriter w = new FileWriter( "./bench/code/jsonacious/FruitReflector.java" );
		w.write( source );
		w.close();
	}

}
