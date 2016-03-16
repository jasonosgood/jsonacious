package facebook;
import jsonacious.Reflector;
import java.lang.reflect.Type;

public class UserReflector2 extends Reflector {
  public void put( Object target, String key, Object value ) {
    facebook.User temp = (facebook.User) target;
    switch( key ) {
      case "id":
        temp.id = (java.lang.String) value;
        break;
      case "name":
        temp.name = (java.lang.String) value;
        break;
      case "location":
        temp.location = (facebook.Location) value;
        break;
      case "alias":
        temp.alias = (java.util.ArrayList<java.lang.String>) value;
        break;
    }
  }

  static Type idType;
  static Type nameType;
  static Type locationType;
  static Type aliasType;

  static {
    try {
      idType = facebook.User.class.getField( "id" ).getGenericType();
      nameType = facebook.User.class.getField( "name" ).getGenericType();
      locationType = facebook.User.class.getField( "location" ).getGenericType();
      aliasType = facebook.User.class.getField( "alias" ).getGenericType();
    } catch( NoSuchFieldException e ) {}
  }
  public Type getValueType( String key ) {
    switch( key ) {
      case "id": return idType;
      case "name": return nameType;
      case "location": return locationType;
      case "alias": return aliasType;
    }
    return Object.class;
  }
}

