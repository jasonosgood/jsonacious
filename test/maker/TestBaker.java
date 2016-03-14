package maker;

import facebook.Location2;
import facebook.User;
import facebook.User2;
import jsonacious.JSONReader;
import jsonacious.JSONBaker;
import jsonacious.MemoryBasedCompiler;
import jsonacious.Reflector;

import java.util.Map;

public class TestBaker {

	public final static void main( String[] args )
		throws Exception
	{
		String content = "{ 'id' : 'abc', 'name': 'xyz', 'location' : { 'id' : 'tops' } }".replace( '\'', '"' );
//		String content = "{ 'id' : 'abc', 'location' : { 'id' : 'tops' } }".replace( '\'', '"' );
//		String content = "{ 'alias' : [ 'one', 'two' ] }".replace( '\'', '"' );
//		String content = "{ 'id' : 'abc' }".replace( '\'', '"' );
//		String content = "{ 'id' : 'abc', 'name': 'xyz' }".replace( '\'', '"' );
		JSONReader parser = new JSONReader();
		Map map = parser.map( content );
		System.out.println( map.toString() );


		User2Reflector ur = new User2Reflector();
		Reflector.add( User2.class, ur );

		String source = GeneratedDemo.generate( Location2.class );
		Class<? extends Reflector> spurge =  (Class<? extends Reflector>) MemoryBasedCompiler.compile( "Location2Reflector", source );
		Reflector lr = spurge.newInstance();
		Reflector.add( Location2.class, lr );


		JSONBaker baker = new JSONBaker();
		User2 user = baker.parse( content, User2.class );
		System.out.println( "ugh " + user );
	}
}
