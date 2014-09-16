package jsonacious;

/**
 * Created by jasonosgood on 8/22/14.
 */
public class
	MethodNotFoundException
extends
	Exception
{
	public MethodNotFoundException( Class clazz, String name )
	{
		super( String.format( "class '%s' does not have methodA (bean) '%s'", clazz.getName(), name ));
	}
}
