package issues;


import jsonacious.JSONReader;
import jsonacious.ParseException;

import java.util.Map;

/**

Incorrect parse error message

https://github.com/jasonosgood/jsonacious/issues/1

 */
public class Issue1
{
	public static final void main( String[] args )
		throws Exception
	{
		String json = "{ 'a' : '123', 'b' , '456' }".replace( '\'', '"' );

		JSONReader parser = new JSONReader();

		try
		{
			Map map = parser.map( json );
			System.out.println( map );
		}
		catch( ParseException e )
		{
			if( e.pos == 20 )
			{
				System.out.println( "victory" );
			}
			else
			{
				throw e;
			}
		}



	}
}
