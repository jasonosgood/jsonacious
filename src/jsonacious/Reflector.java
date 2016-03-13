package jsonacious;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Reflector {

	private final static HashMap<Class, Reflector> _reflectors = new HashMap<>();

	private final static Reflector _default = new Reflector();



	// TODO: synchronized
	public final static void add( Class clazz, Reflector reflector )
	{
		if( clazz == null )
		{
			throw new NullPointerException( "clazz" );
		}
		if( reflector == null )
		{
			throw new NullPointerException( "reflector" );
		}

		if( !clazz.isInstance( Map.class ))
		{
			_reflectors.put( clazz, reflector );
		}
	}

	// TODO: synchronized
	public final static Reflector get(Class clazz )
	{
		if( clazz.isInstance( Map.class ))
		{
			return _default;
		}

		Reflector reflector = _reflectors.get( clazz );
//		if( reflector != null )
		{
			return reflector;
		}
//		else
//		{
//			// create reflector
//		}
	}

	public Type getValueType( String key )
	{
		return Object.class;
	}

	public void put( Object target, String key, Object value )
	{
		((Map) target).put( key, value );
	}
}
