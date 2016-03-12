package maker;

import facebook.Location;
import facebook.User;
import jsonacious.JSONBaker;
import jsonacious.Reflector;

public class TestBaker {

	public final static void main( String[] args )
		throws Exception
	{
//		String content = "{ 'id' : 'abc', 'name': 'xyz', location : { 'id' : 'tops' } }".replace( '\'', '"' );
//		String content = "{ 'id' : 'abc', 'location' : { 'id' : 'tops' } }".replace( '\'', '"' );
		String content = "{ 'alias' : [ 'one', 'two' ] }".replace( '\'', '"' );
//		String content = "{ 'id' : 'abc' }".replace( '\'', '"' );
		LocationReflector lr = new LocationReflector();
		Reflector.add( Location.class, lr );
		UserReflector ur = new UserReflector();
		Reflector.add( User.class, ur );
		JSONBaker baker = new JSONBaker();
//		Location location = baker.parse( content, Location.class );
		User user = baker.parse( content, User.class );
		System.out.println( "ugh " + user );
	}
}
