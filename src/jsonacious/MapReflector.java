package jsonacious;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MapReflector {

	private final static HashMap<Class,MapReflector> _reflectors = new HashMap<>();

	private final static MapReflector _default = new MapReflector();



	// synchronized
	public final static void add( Class clazz, MapReflector reflector )
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

	// synchronized
	public final static MapReflector get( Class clazz )
	{
		if( clazz.isInstance( Map.class ))
		{
			return _default;
		}

		MapReflector reflector = _reflectors.get( clazz );
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
