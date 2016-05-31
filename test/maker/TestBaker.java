package maker;

import facebook.User;
import jsonacious.JSONBaker;

import java.util.HashMap;

public class TestBaker {

	public final static void main( String[] args )
		throws Exception
	{
//		String content = "{ 'id' : 'abc' }".replace( '\'', '"' );
		String content = "{ 'id' : 'abc', 'name' : 'xyz', 'location' : { 'id' : 'tops' }, 'alias' : [ 'one', 'two' ] }".replace( '\'', '"' );
//		String content = "{ 'id' : 'tops' }".replace( '\'', '"' );
//		String content = "{ 'id' : 'abc', 'location' : { 'id' : 'tops' } }".replace( '\'', '"' );
//		String content = "{ 'alias' : [ 'one', 'two' ] }".replace( '\'', '"' );
//		String content = "{ 'id' : 'abc' }".replace( '\'', '"' );
//		String content = "{ 'id' : 'abc', 'name': 'xyz' }".replace( '\'', '"' );


		JSONBaker baker = new JSONBaker();
		User user = baker.parse( content, User.class );
		System.out.println( "user " + user );
		HashMap userMap = baker.parse( content, HashMap.class );
		System.out.println( "userMap " + userMap );
//		Location user = baker.parse( content, Location.class );
//		System.out.println( "ugh " + user );
	}
}
