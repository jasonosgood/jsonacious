package maker;

import facebook.User;
import jsonacious.JSONReader;

import java.util.HashMap;

public class TestReader
{

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


		JSONReader reader = new JSONReader();
		User user = reader.parse( content, User.class );
		System.out.println( "user " + user );
		HashMap userMap = reader.parse( content, HashMap.class );
		System.out.println( "userMap " + userMap );
//		Location user = reader.parse( content, Location.class );
//		System.out.println( "ugh " + user );
	}
}
