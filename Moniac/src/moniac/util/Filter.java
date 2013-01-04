package moniac.util;

import java.util.HashSet;
import java.util.Set;


public interface Filter<T>
{
    public boolean allow (T e);
    
    public class ClassFilter implements Filter
    {
        Class clazz;
        
        public ClassFilter (Class clazz) { this.clazz = clazz; }
        
        public boolean allow (Object e) { return clazz.isAssignableFrom( e.getClass() ); }
    }
    
    public class ClassSetFilter implements Filter
    {
        Set<Class> classes;
        
        public ClassSetFilter (Class... classargs) 
        { 
            this.classes = new HashSet (); 
            for (Class clazz : classargs) classes.add (clazz);
        }
        
        public boolean allow (Object e) { return classes.contains (e.getClass ()); }
    }
    
    public static Filter ACCEPT_ALL = new Filter ()
    {
        public boolean allow (Object e)
        {
            return true;
        }
    };
}