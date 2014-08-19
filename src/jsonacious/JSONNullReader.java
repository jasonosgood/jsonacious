package jsonacious;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.*;

/**
 * Used to time parsing, minus the object creation. Just parses, does not
 * create output maps, lists, etc.
 *
 */

public class
	JSONNullReader
extends
	JSONReader
{

	static class NullMap implements Map
	{

		@Override
		public int size()
		{
			return 0;
		}

		@Override
		public boolean isEmpty()
		{
			return false;
		}

		@Override
		public boolean containsKey( Object key )
		{
			return false;
		}

		@Override
		public boolean containsValue( Object value )
		{
			return false;
		}

		@Override
		public Object get( Object key )
		{
			return null;
		}

		@Override
		public Object put( Object key, Object value )
		{
			return null;
		}

		@Override
		public Object remove( Object key )
		{
			return null;
		}

		@Override
		public void putAll( Map m )
		{

		}

		@Override
		public void clear()
		{

		}

		@Override
		public Set keySet()
		{
			return null;
		}

		@Override
		public Collection values()
		{
			return null;
		}

		@Override
		public Set<Entry> entrySet()
		{
			return null;
		}
	}

	static Map<String, Object> NULL_MAP = new NullMap();

	public Map<String, Object> createMap()
	{
		return NULL_MAP;
	}

	static class NullList<T> implements List
	{
		@Override
		public int size()
		{
			return 0;
		}

		@Override
		public boolean isEmpty()
		{
			return false;
		}

		@Override
		public boolean contains( Object o )
		{
			return false;
		}

		@Override
		public Iterator iterator()
		{
			return null;
		}

		@Override
		public Object[] toArray()
		{
			return new Object[ 0 ];
		}

		@Override
		public boolean add( Object o )
		{
			return false;
		}

		@Override
		public boolean remove( Object o )
		{
			return false;
		}

		@Override
		public boolean addAll( Collection c )
		{
			return false;
		}

		@Override
		public boolean addAll( int index, Collection c )
		{
			return false;
		}

		@Override
		public void clear()
		{
		}

		@Override
		public Object get( int index )
		{
			return null;
		}

		@Override
		public Object set( int index, Object element )
		{
			return null;
		}

		@Override
		public void add( int index, Object element )
		{

		}

		@Override
		public Object remove( int index )
		{
			return null;
		}

		@Override
		public int indexOf( Object o )
		{
			return 0;
		}

		@Override
		public int lastIndexOf( Object o )
		{
			return 0;
		}

		@Override
		public ListIterator listIterator()
		{
			return null;
		}

		@Override
		public ListIterator listIterator( int index )
		{
			return null;
		}

		@Override
		public List subList( int fromIndex, int toIndex )
		{
			return null;
		}

		@Override
		public boolean retainAll( Collection c )
		{
			return false;
		}

		@Override
		public boolean removeAll( Collection c )
		{
			return false;
		}

		@Override
		public boolean containsAll( Collection c )
		{
			return false;
		}

		@Override
		public T[] toArray( Object[] a )
		{
			return null;
		}
	}

	static List<Object> NULL_LIST = new NullList();

	private List<Object> createArray()
	{
		return NULL_LIST;
	}

//	public void fill() {}


	public String readString( int delim )
		throws IOException
	{
		char c;
		while( (  c = read() ) != delim )
		{
			switch( c )
			{
				case '\\':
				{
					switch( c )
					{
						case '"':
						case '/':
						case '\\':
						case 'b':
						case 'f':
						case 'n':
						case 'r':
						case 't':
						case 'u':
							break;

						default:
							throw new IOException( "what is '\\" + (char) c + "'?" );
					}

					break;
				}


				case (char) -1:
				{
					throw new IOException( "unexpected end of file" );
				}
				default:
					break;
			}
		}
		return "";
	}


	public Number readNumber( char c )
		throws IOException
	{
		loop:
		while( true )
		{
			int d = read();
			switch( d )
			{
				case '.':
				case '-':
				case '+':
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case 'E':
				case 'e':
					break;

				case -1:
					throw new IOException( "unexpected end of file" );

				default:
					break loop;
			}
		}
		return 1;
	}

}
