package jsonacious;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class GeneratedDemo
{

    public static void main(final String[] args)
		throws Exception
    {
        StringBuilder sb = new StringBuilder();
        sb.append("package jsonacious;\n");
        sb.append("public class Generated {\n");
        sb.append("    public static void main(final String[] args) {");
//        sb.append("        final Test test = new Test();\n");
        sb.append("    }\n");
        sb.append("    public String toString() {\n");
        sb.append("        return this.getClass().toString();\n" );
        sb.append("    }\n");
        sb.append("}\n");
        Class<?> ugh = MemoryBasedCompiler.compile( "jsonacious.Generated", sb.toString() );
		final Object instance = ugh.newInstance();
		System.out.println( instance );
    }

    public String generate( Class pojo ) throws
        IOException
    {

        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter( writer );

//		out.println( "package baker;" );
//		out.println();
//		out.println();
        out.println( "import facebook.Location;" );
        out.println( "import jsonacious.Reflector;" );
        out.println();
        out.println( "import java.lang.reflect.Type;" );
        out.println();
//		out.println( "public class LocationReflector extends Reflector" );
        out.println( "public class HelloWorld extends Reflector" );
        out.println( "{" );
        out.println( "\t@Override" );
        out.println( "\tpublic void put( Object target, String key, Object value )" );
        out.println( "\t{" );
        out.println( "\t\tLocation temp = (Location) target;" );
        out.println( "\t\tswitch( key ) {" );
        out.println( "\t\t\tcase \"id\":" );
        out.println( "\t\t\t\ttemp.id = value.toString();" );
        out.println( "\t\t\t\tbreak;" );
        out.println( "\t\t\tcase \"name\":" );
        out.println( "\t\t\t\ttemp.name = value.toString();" );
        out.println( "\t\t\t\tbreak;" );
        out.println( "\t\t}" );
        out.println( "\t}" );
        out.println();
        out.println( "\t@Override" );
        out.println( "\tpublic Type getValueType( String key )" );
        out.println( "\t{" );
        out.println( "\t\tswitch( key ) {" );
        out.println( "\t\t\tcase \"id\":" );
        out.println( "\t\t\t\treturn String.class;" );
        out.println( "\t\t\tcase \"name\":" );
        out.println( "\t\t\t\treturn String.class;" );
        out.println( "\t\t}" );
        out.println();
        out.println( "\t\treturn Object.class;" );
        out.println( "\t}" );
        out.println( "}" );
        out.close();

        return writer.toString();
    }
}