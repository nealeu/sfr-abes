/*
 * FilteredCollection.java
 *
 * Created on 22 September 2005, 19:36
 */

package moniac.util;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/** An immutable clone of another collection where only elements 
 *  passing a filter are visible
 *
 * @author Dave, (c) Short Fuze Ltd, 22 September 2005
 */
public class FilteredCollection<E> extends AbstractCollection<E>
{    
    Filter filter;
    Collection original;
    
    public FilteredCollection (Collection original, Filter filter)
    {
        this.filter = filter;
        this.original = original;
    }
    
    public FilteredCollection (Collection original, Class<E> clazz)
    {
        this.filter = new Filter.ClassFilter (clazz);
        this.original = original;
    }
    
    @Override
	public int size ()
    {
        int size = 0;
        for (Object obj : original)
            if (filter.allow (obj))
                size += 1;
        
        return size;
    }

    @Override
	public boolean remove (Object o)
    {
        return original.remove (o);
    }

    @Override
	public boolean contains (Object o)
    {
        return filter.allow (o) && super.contains (o);
    }
    
    @Override
	public Iterator<E> iterator ()
    {
        return new Iterator ()
        {
            Iterator iterator = original.iterator ();
            E inhand;
            
            @Override
			public E next ()
            {
                if (inhand != null)
                {
                    E tmp = inhand;
                    inhand = null;
                    return tmp;
                }
                else
                    while (true)
                    {
                        if (!iterator.hasNext ())
                            throw new NoSuchElementException ();

                        Object next = iterator.next ();

                        if (filter.allow (next))
                            return (E) next;
                    }
            }
            
            @Override
			public boolean hasNext ()
            {
                if (inhand != null)
                {
                    return true;
                }
                else
                    while (true)
                    {
                        if (!iterator.hasNext ())
                            return false;

                        Object next = iterator.next ();

                        if (filter.allow (next))
                        {
                            inhand = (E) next;
                            return true;
                        }
                    }
            }
            
            @Override
			public void remove ()
            {
                iterator.remove ();
            }            
        };
    }        
}
