package jsonacious;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import demo.MediaItem;

/**
 * Created by jasonosgood on 9/3/14.
 */
public class PlayReflect
{
	public static void main( String[] args )
		throws Exception
	{
		String x1 = Integer.class.getSimpleName();
		String x2 = int.class.getSimpleName();
		System.out.println( x1 );
		System.out.println( x2 );
		String key = "blitz";
		String setter = "set" + capitalize( key );
		ConstructorAccess ca = ConstructorAccess.get( MediaItem.Content.class  );
		Object o = ca.newInstance();
		MethodAccess ma = MethodAccess.get( MediaItem.Content.class );
		String[] methods = ma.getMethodNames();
		Class[][] params = ma.getParameterTypes();
//		int a = ma.getIndex( "getPhotos" );
//		int b = ma.getIndex( "getPhotos", 2 );
		int c = ma.getIndex( "setFormat", 1 );
		ma.invoke( o, c, (Object) null );
		Object roar = null;
		ma.invoke( o, c, roar );
//		ma.invoke( o, c, null );
//		ma.invoke( o, c );

		System.out.println();

		int d = ma.getIndex( "getFormat" );
		Object result = ma.invoke( o, d );

	}

	private static String capitalize( String propertyName )
	{
		if (propertyName.length() == 0) return null;
		return propertyName.substring( 0, 1 ).toUpperCase() + propertyName.substring( 1 );
	}

}
