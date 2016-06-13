package variety;
import jsonacious.Reflector;
import jsonacious.JSONWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class VarietyReflector extends Reflector {
  public void put( Object target, String key, Object value ) {
    variety.Variety temp = (variety.Variety) target;
    switch( key ) {
      case "text1":
        temp.text1 = (java.lang.String) value;
        break;
      case "text2":
        temp.text2 = (java.lang.String) value;
        break;
      case "text3":
        temp.text3 = (java.lang.String) value;
        break;
      case "null1":
        temp.null1 = (java.lang.Object) value;
        break;
      case "bool1":
        temp.bool1 = (boolean) value;
        break;
      case "bool2":
        temp.bool2 = (boolean) value;
        break;
      case "bool3":
        temp.bool3 = (java.lang.Boolean) value;
        break;
      case "bool4":
        temp.bool4 = (java.lang.Boolean) value;
        break;
      case "map1":
        temp.map1 = (java.util.HashMap<java.lang.String, java.lang.String>) value;
        break;
      case "map2":
        temp.map2 = (java.util.HashMap<java.lang.String, java.lang.String>) value;
        break;
      case "list1":
        temp.list1 = (java.util.ArrayList<java.lang.String>) value;
        break;
      case "list2":
        temp.list2 = (java.util.ArrayList<java.lang.String>) value;
        break;
      case "listOfMap":
        temp.listOfMap = (java.util.ArrayList<java.util.HashMap>) value;
        break;
      case "listOfList":
        temp.listOfList = (java.util.ArrayList<java.util.List>) value;
        break;
      case "listOfObject":
        temp.listOfObject = (java.util.ArrayList<java.lang.Object>) value;
        break;
      case "number07":
        temp.number07 = toInt( value );
        break;
      case "number08":
        temp.number08 = toLong( value );
        break;
      case "number09":
        temp.number09 = toFloat( value );
        break;
      case "number10":
        temp.number10 = toDouble( value );
        break;
      case "child":
        temp.child = (variety.Variety) value;
        break;
    }
  }

  Type text1Type;
  Type text2Type;
  Type text3Type;
  Type null1Type;
  Type bool1Type;
  Type bool2Type;
  Type bool3Type;
  Type bool4Type;
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

  public VarietyReflector() {
    try {
      text1Type = variety.Variety.class.getField( "text1" ).getGenericType();
      text2Type = variety.Variety.class.getField( "text2" ).getGenericType();
      text3Type = variety.Variety.class.getField( "text3" ).getGenericType();
      null1Type = variety.Variety.class.getField( "null1" ).getGenericType();
      bool1Type = variety.Variety.class.getField( "bool1" ).getGenericType();
      bool2Type = variety.Variety.class.getField( "bool2" ).getGenericType();
      bool3Type = variety.Variety.class.getField( "bool3" ).getGenericType();
      bool4Type = variety.Variety.class.getField( "bool4" ).getGenericType();
      map1Type = variety.Variety.class.getField( "map1" ).getGenericType();
      map2Type = variety.Variety.class.getField( "map2" ).getGenericType();
      list1Type = variety.Variety.class.getField( "list1" ).getGenericType();
      list2Type = variety.Variety.class.getField( "list2" ).getGenericType();
      listOfMapType = variety.Variety.class.getField( "listOfMap" ).getGenericType();
      listOfListType = variety.Variety.class.getField( "listOfList" ).getGenericType();
      listOfObjectType = variety.Variety.class.getField( "listOfObject" ).getGenericType();
      number07Type = variety.Variety.class.getField( "number07" ).getGenericType();
      number08Type = variety.Variety.class.getField( "number08" ).getGenericType();
      number09Type = variety.Variety.class.getField( "number09" ).getGenericType();
      number10Type = variety.Variety.class.getField( "number10" ).getGenericType();
      childType = variety.Variety.class.getField( "child" ).getGenericType();
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
      case "bool3": return bool3Type;
      case "bool4": return bool4Type;
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
    return null;
  }
  public void write( JSONWriter writer, Object source )
    throws IOException
  {
    variety.Variety temp = (variety.Variety) source;
    writer.leftSquiggle();

    writer.writePair( "text1", temp.text1 );
    writer.comma();
    writer.writePair( "text2", temp.text2 );
    writer.comma();
    writer.writePair( "text3", temp.text3 );
    writer.comma();
    writer.writePair( "null1", temp.null1 );
    writer.comma();
    writer.writePair( "bool1", temp.bool1 );
    writer.comma();
    writer.writePair( "bool2", temp.bool2 );
    writer.comma();
    writer.writePair( "bool3", temp.bool3 );
    writer.comma();
    writer.writePair( "bool4", temp.bool4 );
    writer.comma();
    writer.writePair( "map1", temp.map1 );
    writer.comma();
    writer.writePair( "map2", temp.map2 );
    writer.comma();
    writer.writePair( "list1", temp.list1 );
    writer.comma();
    writer.writePair( "list2", temp.list2 );
    writer.comma();
    writer.writePair( "listOfMap", temp.listOfMap );
    writer.comma();
    writer.writePair( "listOfList", temp.listOfList );
    writer.comma();
    writer.writePair( "listOfObject", temp.listOfObject );
    writer.comma();
    writer.writePair( "number07", temp.number07 );
    writer.comma();
    writer.writePair( "number08", temp.number08 );
    writer.comma();
    writer.writePair( "number09", temp.number09 );
    writer.comma();
    writer.writePair( "number10", temp.number10 );
    writer.comma();
    writer.writePair( "child", temp.child );
    writer.rightSquiggle();
  }

}

