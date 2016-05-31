JSON reader and writer for Java. 

Notable features, differences.

 - Strict compliance with JSON spec.
 - Correct, useful parse error messages.
 - Supports Maps, Lists, and POJOs.
 - No JavaBeans or accessors, only supports public fields.
 - No annotations.
 - Fast enough LA(1) style parser.
 - Small library (~40kb)
 - Requires JDK's JavaCompiler (tools.jar) at runtime
 - Code generate POJO from JSON.

**Reader**

JSONReader deserializes JSON data to Java objects. Pass in the Class 
to isntantiate. Must be a concrete subclass of either Map or List, or 
your own POJO. 

    import jsonacious.JSONReader;
    import java.util.Map;

    ...
    
    JSONReader jsonReader = new JSONReader();
    String json = "{ 'A' : 'apple', 'B' : {} }".replace( '\'', '"' );
    HashMap map = jsonReader.parse( json, HashMap.class );
    System.out.println( map );

Outputs this, using method HashMap.toString():

    {A=apple, B={}}

JSONReader instances are reuseable, but are not thread-safe.

**Writer**

JSONWriter serializes Maps, Lists, POJOs to JSON data.

JSONWriter instances are reuseable, but are not thread-safe.


**The Rules**

JSONacious supports public fields with instantiable or primitive (scalar)
classes. Field names and JSON keys must match exactly; there is no mapping.

    class ExamplePOJO 
    {
        // Supported
        public String      example1;
        public int         example2;
        public Integer     example3;
        public HashMap     example4;
        public ExamplePOJO example5;
        
        // Ignored
        String                  example6;
        public final String     example7;
        public static String    example8;
        public transient String example9;
        public volatile String  example10;
        public Map              example11; // interface
        public AbstractMap      example12; // abstract class
        
    }

Arrays are not supported. Enums will likely be supported. Special 
handling for Dates might happen.

**Generator**

POJOnacious converts example JSON data to Java class source code. JSON 
keys become Java fields, JSON values become Java default field values.

**Reflector**

(Say something here about runtime in-memory JavaCompiler.)

Customization

Subclasses of Reflector can be registered manually. Capture the generated 
source code, add it to your project, tweak as needed.

**TODO**

 - Maven (blech)
 - Support enums.
 - Support arrays (maybe).
 - Support Dates (maybe).
 - Figure out how to debug runtime generated Reflectors.
 - Command line utility to generate POJOs from example JSON.
 - Command line utility to generate Reflectors (eg during build).