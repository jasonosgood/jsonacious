package maker;

import facebook.Location;
import jsonacious.MemoryBasedCompiler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class GeneratedDemo
{

    public static void main(final String[] args)
		throws Exception
    {
        String s = generate( Location.class );
		FileWriter writer = new FileWriter( "/Users/jasonosgood/Projects/jsonacious/test/maker/LocationReflector.gen" );
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

    public static String generate( Class pojo ) throws
        IOException
    {
		String pkg = "maker";
		String className = pojo.getName();
		String simpleClassName = pojo.getSimpleName();

		Field[] fields = pojo.getFields();

		ArrayList<Field> reduced = new ArrayList<>();
		for( Field f : fields )
		{
			int mod = f.getModifiers();
			if( Modifier.isFinal( mod )) continue;
			if( Modifier.isStatic( mod )) continue;
			if( Modifier.isTransient( mod )) continue;
			if( Modifier.isVolatile( mod )) continue;
			reduced.add( f );
		}

		StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter( writer );

		out.printf( "package %s;\n", pkg );
        out.printf( "import %s;\n", className );
        out.printf( "import jsonacious.Reflector;\n" );
        out.printf( "import java.lang.reflect.Type;\n" );
        out.println();
		out.printf( "public class %sReflector extends Reflector {\n", simpleClassName );
        out.printf( "  public void put( Object target, String key, Object value ) {\n" );
        out.printf( "    %s temp = (%s) target;\n", simpleClassName, simpleClassName );
        out.printf( "    switch( key ) {\n" );

		// Generates cases in this form:
		//
		//		case "id":
		//		temp.id = value.toString();
		//		break;

		for( Field f : reduced )
		{
			String name = f.getName();
			out.printf( "      case %s:\n", quoted( name ));
			out.printf( "        temp.%s = value.toString();\n", name );
			out.printf( "        break;\n" );
		}

        out.printf( "    }\n" );
        out.printf( "  }\n" );
        out.printf( "  public Type getValueType( String key ) {\n" );
        out.printf( "    switch( key ) {\n" );

		// Generates cases in this form:
		//
		//		case "id":
		//		return java.lang.String.class;

		for( Field f : reduced )
		{
			String name = f.getName();
			Type type = f.getGenericType();
			out.printf( "      case %s:\n", quoted( name ));
			out.printf( "        return %s.class;\n", type.getTypeName() );
		}

		out.printf( "    }\n" );
        out.printf( "    return Object.class;\n" );
        out.printf( "  }\n" );
        out.printf( "}\n" );
        out.close();

        return writer.toString();
    }

	public static String quoted( String value )
	{
		return "\"" + value + "\"";
	}
}