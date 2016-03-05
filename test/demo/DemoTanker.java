package demo;

import jsonacious.JSONReader;

import java.io.IOException;
import java.util.Map;

/**
 */
public class DemoTanker
{

    public static void main( String[] args )
        throws IOException
	{
        System.out.println( "'0' " + (int) '0');
        System.out.println( "'9' " + (int) '9');
        System.out.println( "'A' " + (int) 'A' );
        System.out.println( "'H' " + (int) 'H' );
        System.out.println( "'a' " + (int) 'a');
        System.out.println( "'h' " + (int) 'h');
        System.out.println( "'{' " + (int) '{');
        System.out.println( "'}' " + (int) '}');
        System.out.println( "'[' " + (int) ']');
        System.out.println( "']' " + (int) ']');
        System.out.println( "'\\t' " + (int) '\t');
        System.out.println( "'\\n' " + (int) '\n');
        System.out.println( "'\\r' " + (int) '\r');
        System.out.println( "' ' " + (int) ' ');
        System.out.println( "':' " + (int) ':');
        System.out.println( "',' " + (int) ',');

		JSONReader jsonReader = new JSONReader();
		String payload =
			"{ \n" +
			"'A' : 'apple', \n" +
//			"'B' : \"banana \\t \\r \\n \\\\ \", \n" +
			"'C' : 'code', \n" +
			"'D' : {}, \n" +
			"'E' : { 'Z1' : 'zebra' }, \n" +
			"'F' : '\\u0048\\u0065\\u006C\\u006C\\u006F World', \n" +
			"'G' : null, \n" +
			"'H' : true, \n" +
			"'I' : false, \n" +
			"'J' : [], \n" +
			"'K' : [ 'one', 'two' ], \n" +
			"'L' : [ { 'side' : 'left' }, { 'side' : 'right' } ], \n" +
			"'M' : [ ['one', 'two'], ['three','four' ] ], \n" +
			"'N' : -123.789, \n" +
			"'O' : [ 123, 0, -1, 9223372036854775807, 1.7976931348623157E308 ], \n" +
			"'Z' : 'zebra' \n" +

        "} ";

//		payload = "{ 'A' : 'apple', 'B' : {} }";
//		payload = "{ 'A' : 'apple', 'B' : {} ";

		Map map = jsonReader.parseMap( payload );
		System.out.println( map );
	}


}
