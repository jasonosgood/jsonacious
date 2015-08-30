package demo;

import jsonacious.JSONTanker;

import java.io.IOException;
import java.util.Map;

/**
 */
public class DemoTanker
{

    public static void main( String[] args )
        throws IOException
	{
		JSONTanker jsonTanker = new JSONTanker();
		String payload =
			"{ " +
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
			"'O' : [ 123, 0, -1, 9223372036854775807, 1.7976931348623157E308 ] \n" +

			"} ";

		payload = "{ 'A' : 'apple', 'B' : {} }";

		Map map = jsonTanker.parseMap( payload );
		System.out.println( map );
	}


}
