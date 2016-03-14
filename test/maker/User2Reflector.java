package maker;

import facebook.Location2;
import facebook.User;
import facebook.User2;
import jsonacious.Reflector;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class User2Reflector
	extends Reflector
{
	@Override
	public void put( Object target, String key, Object value )
	{
		// TODO Exception handling
		User2 temp = (User2) target;
		switch( key ) {
			case "id":
				temp.id = value.toString();
				break;
			case "name":
				temp.name = value.toString();
				break;
			case "location":
				temp.location = (Location2) value;
				break;
			case "alias":
				temp.alias = (ArrayList<String>) value;
				break;
		}

	}

	@Override
	public Type getValueType( String key )
	{
		switch( key ) {
			case "id":
				return String.class;
			case "name":
				return String.class;
			case "location":
				return Location2.class;
			case "alias":
//				User.alias;
				try {
					Field integerListField = User.class.getDeclaredField("alias");
					ParameterizedType integerListType = (ParameterizedType) integerListField.getGenericType();
					return integerListType;
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}

				return Object.class;
		}

		return Object.class;
	}
}