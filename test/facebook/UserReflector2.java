package facebook;

import jsonacious.Reflector;

import java.lang.reflect.Type;

public class UserReflector2
    extends Reflector {
  public void put( Object target, String key, Object value ) {
    User temp = (User) target;
    switch( key ) {
      case "id":
        temp.id = (String) value;
        break;
      case "name":
        temp.name = (String) value;
        break;
      case "location":
        temp.location = (Location) value;
        break;
      case "alias":
        temp.alias = (java.util.ArrayList<String>) value;
        break;
    }
  }

  static Type idType;
  static Type nameType;
  static Type locationType;
  static Type aliasType;

  static {
    try {
      idType = User.class.getField( "id" ).getGenericType();
      nameType = User.class.getField( "name" ).getGenericType();
      locationType = User.class.getField( "location" ).getGenericType();
      aliasType = User.class.getField( "alias" ).getGenericType();
    } catch( NoSuchFieldException e ) {}
  }

  public Type getValueType( String key ) {
    switch( key ) {
      case "id": return idType;
      case "name": return nameType;
      case "location":  return locationType;
      case "alias": return aliasType;
    }
    return Object.class;
  }
}
