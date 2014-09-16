package jsonacious;

/**
 * Created by jasonosgood on 9/4/14.
 */
public class VarArgsNullParamTest
{
	public static void main( String[] args )
	{
		// Passes array length 1, contain one null item
		methodA( new Object[]{ null } );
		methodA( (Object) null );
		Object emptyB = null;
		methodA( emptyB );

		// Passes array length 0
		methodA( new Object[]{ } );
		methodA();

		// Passes null array
		methodA( null );

		System.out.println();

		// Passes array length 1, contain one null item
		methodB( new Object[]{ null } );

		// Passes single argument
		methodB( (Object) null );
		Object empty = null;
		methodB( empty );

		// Passes array length 0
		methodB( new Object[]{ } );
		methodB();

		// Passes null array
		methodB( null );
	}

	public static void methodA( Object... value )
	{
		if( value == null )
		{
			System.out.println( value );
		}
		else
		{
			System.out.println( "array length: " + value.length );
		}
	}


	public static void methodB( Object... value )
	{
		if( value == null )
		{
			System.out.println( value );
		}
		else
		{
			System.out.println( "array length: " + value.length );
		}
	}

	public static void methodB( Object value )
	{
		System.out.println( "single arg: " + value );
	}

}
