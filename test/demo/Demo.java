package demo;

import jsonacious.JSONReader;

import java.io.IOException;
import java.util.Map;

public class Demo
{

    public static void main( String[] args )
        throws IOException
	{
		JSONReader jsonReader = new JSONReader();
		String payload =
			"{ " +
				"\"A\" : \"apple\", \n" +
//				"\"B\" : \"banana \\t \\r \\n \\\\ \", \n" +
				"\"C\" : \"code\", \n" +
				"\"D\" : {}, \n" +
				"\"E\" : { \"Z1\" : \"zebra\" }, \n" +
				"\"F\" : \"\\u0048\\u0065\\u006C\\u006C\\u006F World\", \n" +
				"\"G\" : null, \n" +
				"\"H\" : true, \n" +
				"\"I\" : false, \n" +
				"\"J\" : [], \n" +
				"\"K\" : [ \"one\", \"two\" ], \n" +
				"\"L\" : [ { \"side\" : \"left\" }, { \"side\" : \"right\" } ], \n" +
				"\"M\" : [ [\"one\", \"two\"], [\"three\",\"four\" ] ], \n" +
				"\"N\" : -123.789, \n" +
				"\"O\" : [ 123, 0, -1, 9223372036854775807, 1.7976931348623157E308 ], \n" +
				"\"P\" : [ 123 , 0 , -1 , 9223372036854775807 , 1.7976931348623157E308 ] , \n" +
				"\"Q\" : 123, \n" +
				"\"Z\":123\n" +

			"} ";

		Map map = jsonReader.map( payload );
		System.out.println( map );
	}


}
