Simple, fast enough JSON utilities. Notable features, differences.

 - Strict compliance with JSON spec.
 - Useful error messages (line, position, expected, actual).
 - Hand written LA(1) parser, mostly single pass.
 - No lazy evaluation.
 - No JSON objects. Just Map, List, String, Number, Boolean, null.

JSONReader converts JSON input into Maps and Lists.


JSONReader is reuseable, but not threadsafe.


    import jsonacious.JSONReader;
    import java.util.Map;

    ...
    
    JSONReader jsonReader = new JSONReader();
    String payload = "{ 'A' : 'apple', 'B' : {} }";
    Map map = jsonReader.parse( payload );
    System.out.println( map );

Outputs this, using method HashMap.toString():

    {A=apple, B={}}


