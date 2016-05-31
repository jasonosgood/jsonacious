package maker;

import facebook.Location;
import facebook.User;
import jsonacious.Reflector;

import java.io.FileWriter;

public class GeneratedDemo
{

    public static void main(final String[] args)
		throws Exception
    {
//        String s = Reflector.generate( Location.class );
//		FileWriter writer = new FileWriter( "/Users/jasonosgood/Projects/jsonacious/test/maker/LocationReflector.java" );
        String s = Reflector.generate( User.class );
		FileWriter writer = new FileWriter( "/Users/jasonosgood/Projects/jsonacious/test/facebook/UserReflector.java" );
//        String s = Reflector.generate( variety.Variety.class );
//		FileWriter writer = new FileWriter( "/Users/jasonosgood/Projects/jsonacious/test/variety/VarietyReflector.java" );
		writer.write( s );
		writer.close();

//        StringBuilder sb = new StringBuilder();
//        sb.append("package jsonacious;\n");
//        sb.append("public class Generated {\n");
//        sb.append("    public static void main(final String[] args) {");
////        sb.append("        final Test test = new Test();\n");
//        sb.append("    }\n");
//        sb.append("    public String toString() {\n");
//        sb.append("        return this.getClass().toString();\n\n" );
//        sb.append("    }\n");
//        sb.append("}\n");
//        Class<?> ugh = MemoryBasedCompiler.compile( "jsonacious.Generated", sb.toString() );
//		final Object instance = ugh.newInstance();
//        String abc = instance.toString();
//        System.out.printf( abc );
    }

}