JSONacious
==========

Simple, fast JSON Reader. Converts input into graph of maps and lists. 

    import jsonacious.JSONReader;
    import java.util.Map;

    ...
    
    JSONReader jsonReader = new JSONReader();
    String payload = "{ 'A' : 'apple', 'B' : {} }";
    Map map = jsonReader.parse( payload );
    System.out.println( map );

Outputs this, using method HashMap.toString():

    {A=apple, B={}}


JSONReader is reuseable, but not threadsafe.

