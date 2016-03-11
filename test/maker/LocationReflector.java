package maker;

import facebook.Location;
import jsonacious.MapReflector;

import java.lang.reflect.Type;

public class LocationReflector extends MapReflector
{
	@Override
	public void put( Object target, String key, Object value )
	{
		// TODO Exception handling
		Location temp = (Location) target;
		switch( key ) {
			case "id":
				temp.id = value.toString();
				break;
			case "name":
				temp.name = value.toString();
				break;
		}

	}

	@Override
	public Type getValueType(String key )
	{
		switch( key ) {
			case "id":
				return String.class;
			case "name":
				return String.class;
		}

		return Object.class;
	}
}