package variety;

import jsonacious.Reflector;

import java.lang.reflect.Type;

public class VarietyReflector3
	extends Reflector {
  public void put( Object target, String key, Object value ) {
    Variety temp = (Variety) target;
    switch( key ) {
      case "text1":
        temp.text1 = (String) value;
        break;
      case "text2":
        temp.text2 = (String) value;
        break;
      case "text3":
        temp.text3 = (String) value;
        break;
      case "null1":
        temp.null1 = (Object) value;
        break;
      case "bool1":
        temp.bool1 = (boolean) value;
        break;
      case "bool2":
        temp.bool2 = (boolean) value;
        break;
      case "map1":
        temp.map1 = (java.util.HashMap<String, String>) value;
        break;
      case "map2":
        temp.map2 = (java.util.HashMap<String, String>) value;
        break;
      case "list1":
        temp.list1 = (java.util.ArrayList<String>) value;
        break;
      case "list2":
        temp.list2 = (java.util.ArrayList<String>) value;
        break;
      case "listOfMap":
        temp.listOfMap = (java.util.ArrayList<java.util.HashMap>) value;
        break;
      case "listOfList":
        temp.listOfList = (java.util.ArrayList<java.util.List>) value;
        break;
      case "listOfObject":
        temp.listOfObject = (java.util.ArrayList<Object>) value;
        break;
      case "number07":
        temp.number07 = (Integer) value;
        break;
      case "number08":
        temp.number08 = (Long) value;
        break;
      case "number09":
        temp.number09 = (Float) value;
        break;
      case "number10":
        temp.number10 = (Double) value;
        break;
      case "child":
        temp.child = (Variety) value;
        break;
    }
  }

  Type text1Type;
  Type text2Type;
  Type text3Type;
  Type null1Type;
  Type bool1Type;
  Type bool2Type;
  Type map1Type;
  Type map2Type;
  Type list1Type;
  Type list2Type;
  Type listOfMapType;
  Type listOfListType;
  Type listOfObjectType;
  Type number07Type;
  Type number08Type;
  Type number09Type;
  Type number10Type;
  Type childType;

  public VarietyReflector3() {
    try {
      text1Type = Variety.class.getField( "text1" ).getGenericType();
      text2Type = Variety.class.getField( "text2" ).getGenericType();
      text3Type = Variety.class.getField( "text3" ).getGenericType();
      null1Type = Variety.class.getField( "null1" ).getGenericType();
      bool1Type = Variety.class.getField( "bool1" ).getGenericType();
      bool2Type = Variety.class.getField( "bool2" ).getGenericType();
      map1Type = Variety.class.getField( "map1" ).getGenericType();
      map2Type = Variety.class.getField( "map2" ).getGenericType();
      list1Type = Variety.class.getField( "list1" ).getGenericType();
      list2Type = Variety.class.getField( "list2" ).getGenericType();
      listOfMapType = Variety.class.getField( "listOfMap" ).getGenericType();
      listOfListType = Variety.class.getField( "listOfList" ).getGenericType();
      listOfObjectType = Variety.class.getField( "listOfObject" ).getGenericType();
      number07Type = Variety.class.getField( "number07" ).getGenericType();
      number08Type = Variety.class.getField( "number08" ).getGenericType();
      number09Type = Variety.class.getField( "number09" ).getGenericType();
      number10Type = Variety.class.getField( "number10" ).getGenericType();
      childType = Variety.class.getField( "child" ).getGenericType();
    } catch( NoSuchFieldException e ) { e.printStackTrace(); }
  }
  public Type getValueType( String key ) {
    switch( key ) {
      case "text1": return text1Type;
      case "text2": return text2Type;
      case "text3": return text3Type;
      case "null1": return null1Type;
      case "bool1": return bool1Type;
      case "bool2": return bool2Type;
      case "map1": return map1Type;
      case "map2": return map2Type;
      case "list1": return list1Type;
      case "list2": return list2Type;
      case "listOfMap": return listOfMapType;
      case "listOfList": return listOfListType;
      case "listOfObject": return listOfObjectType;
      case "number07": return number07Type;
      case "number08": return number08Type;
      case "number09": return number09Type;
      case "number10": return number10Type;
      case "child": return childType;
    }
    return Object.class;
  }
}

