package maker;

import facebook.Location;
import facebook.User;
import jsonacious.JSONReader;
import jsonacious.JSONBaker;
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

//		LocationReflector lr = new LocationReflector();
//		Reflector.add( Location.class, lr );

//		Reflector rr = new CompileSourceInMemory().compile();
//		Reflector.add( Location.class, rr );

		UserReflector ur = new UserReflector();
		Reflector.add( User.class, ur );
		JSONBaker baker = new JSONBaker();
//		Location location = baker.parse( content, Location.class );
		User user = baker.parse( content, User.class );
		System.out.println( "ugh " + user );
	}
}
