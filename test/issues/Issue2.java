package issues;


import jsonacious.JSONReader;

/**

Fail casting numbers, eg Integer to long

https://github.com/jasonosgood/jsonacious/issues/2

 */
public class Issue2
{
	public long data;

	public static final void main( String[] args )
		throws Exception
	{
		String json = "{ 'data' : 2 }".replace( '\'', '"' );

		JSONReader reader = new JSONReader();

		Issue2 issue2 = reader.parse( json, Issue2.class );
		System.out.println( issue2.data );

	}
}
