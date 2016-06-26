package jsonacious;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Reflector
{

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
	public final static Reflector get( Class clazz )
	{
//		if( Map.class.isAssignableFrom( clazz ))
//		{
//			return _default;
//		}

		Reflector reflector = _reflectors.get( clazz );
		if( reflector != null )
		{
			return reflector;
		}
		else
		{
			String source ;
			try
			{
				String name = clazz.getName();
				String subname = name + "Reflector";
				Generator g = new Generator();
				source = g.generate( clazz );
				Class reflectorClazz = MemoryBasedCompiler.compile( subname, source );
				reflector = (Reflector) reflectorClazz.newInstance();
				Reflector.add( clazz, reflector );
				return reflector;
			}
			catch( ClassNotFoundException | InstantiationException | IllegalAccessException e )
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	public Type getValueType( String key )
	{
		return null;
	}

	public Type getValueType( int field )
	{
		return null;
	}

	public int toField( char[] value, int offset, int count ) { return 0; }

	public void put( Object target, int field, Object value )
	{

	}

//	public void put( Object target, String key, Object value )
//	{
//		((Map) target).put( key, value );
//	}

	public byte toByte( Object value )
	{
		return ((Number) value).byteValue();
	}

	public short toShort( Object value )
	{
		return ((Number) value).shortValue();
	}

	public int toInt( Object value )
	{
		return ((Number) value).intValue();
	}

	public long toLong( Object value )
	{
		return ((Number) value).longValue();
	}

	public float toFloat( Object value )
	{
		return ((Number) value).floatValue();
	}

	public double toDouble( Object value )
	{
		return ((Number) value).doubleValue();
	}

	/*
		Default implementation used for Maps. Overridden by generated POJO-specific Reflector subclasses.
	 */
	public void write( JSONWriter writer, Object source )
		throws IOException
	{
		writer.leftSquiggle();

		boolean comma = false;
		Map<String, Object> map = (Map<String, Object>) source;
		for( Map.Entry<String,Object> entry : map.entrySet() )
		{
			if( comma )
			{
				writer.comma();
			}
			else
			{
				comma = true;
			}
			writer.writePair( entry.getKey(), entry.getValue() );
		}

		writer.rightSquiggle();
	}


	public boolean equals( char[] key, char[] value, int offset, int count )
	{
		if( key.length != count ) return false;
		for( int i = 0; i < count; i++ )
		{
			if( key[i] != value[i + offset] ) return false;
		}
		return true;
	}
}
