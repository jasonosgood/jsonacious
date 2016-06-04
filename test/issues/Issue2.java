package issues;


import jsonacious.JSONReader;

/**

Fail casting numbers, eg Integer to long

https://github.com/jasonosgood/jsonacious/issues/2

 */
public class Issue2
{
	public byte a;
	public int b;
	public long c;
	public float d;
	public double e;

	public static final void main( String[] args )
		throws Exception
	{
		String json = "{ 'a' : 1, 'b' : 1, 'c' : 1, 'd' : 1, 'e' : 1  }".replace( '\'', '"' );

		JSONReader reader = new JSONReader();

		Issue2 issue2 = reader.parse( json, Issue2.class );
		System.out.println( issue2.a );

	}
}
