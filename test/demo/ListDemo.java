package demo;

import jsonacious.JSONReader;

import java.io.File;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;


public class ListDemo
{
    public static void main( String[] args )
        throws Exception
	{
		{
			class StringList extends ArrayList<String>{};
			identity( ArrayList.class );
//			identity( new ArrayList<String>().getClass() );
			identity( StringList.class );
		}
//		File file = new File( "./test/demo/ListList.json" );
//		System.out.println( file.getCanonicalFile() );
//
//		JSONReader reader = new JSONReader();
////		{
////			List list = reader.parse( file, ArrayList.class );
////			System.out.println( list );
////		}
//		{
//			List list = reader.parse( file, StringList.class );
//			System.out.println( list );
//		}

	}

	static void identity( Class clazz )
	{
		System.out.println( clazz.toGenericString() );
		TypeVariable<Class<?>>[] a = clazz.getTypeParameters();
		System.out.println( a );
//		Type c = clazz.getGenericSuperclass();
//		clazz.
		Type c = clazz.getGenericSuperclass();

		System.out.println( c );

	}
}
