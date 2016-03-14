package maker;
import facebook.Location;
import jsonacious.Reflector;
import java.lang.reflect.Type;

public class LocationReflector extends Reflector {
  public void put( Object target, String key, Object value ) {
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
  public Type getValueType( String key ) {
    switch( key ) {
      case "id":
        return java.lang.String.class;
      case "name":
        return java.lang.String.class;
    }
    return Object.class;
  }
}
