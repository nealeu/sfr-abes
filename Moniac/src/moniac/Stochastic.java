/*
 * Stochastics.java
 *
 * Created on 13 January 2006, 15:34
 */

package moniac;

import java.util.Map;
import java.util.Random;

/** Utility class holding assorted methods for stochastic processes.
 *  For repeatability, each instance holds its own seed.
 *
 * @author Dave, (c) Short Fuze Ltd, 13 January 2006
 */
public class Stochastic extends Random
{
    public Stochastic()
    {
        super();
    }
    
    public Stochastic(long seed)
    {
        super(seed);
    }
    
    public float nextPoissonTime (float mean, float max)
    {
        return Math.min (max, (float) -Math.log (nextFloat ()) * mean); // negexp process
    }

    public double nextPoissonTime( double mean, double max )
    {
        return Math.min( max, -Math.log( nextDouble() ) * mean ); // negexp process
    }
    
    public int nextPoisson( float mean )
    {
        double l = (float) Math.exp( -mean );
        double p = 1;
        for( int k = 0; true; k += 1 )            
        {
            p = p * nextDouble();
            if( p <= l )
                return k;
        }
    }
    
    public int nextInt (float [] weights)
    {
        float sum = 0;
        for (int n = 0; n < weights.length; n++)
            sum += weights [n];
        
        if (sum == 0)
            return -1;
        
        float pivot = nextFloat () * sum;
        sum = 0;
        for (int n = 0; n < weights.length; n++)
        {
            sum += weights [n];
            if (sum >= pivot)
                return n;
        }        
        return weights.length-1;
    }
    
    /** Delivers the key of a random entry in the supplied map where the values are Floats interpreted as weights.
     */
    public <T> T nextKey (Map<T, Float> params)
    {
        float sum = 0;
        for (Map.Entry<T, Float> entry : params.entrySet ())
            sum += entry.getValue ();
        if ( sum == 0 )
            return( null );
        
        float pivot = nextFloat () * sum;
        sum = 0;
        for (Map.Entry<T, Float> entry : params.entrySet ())
        {
            sum += entry.getValue ();
            if (sum >= pivot)
                return entry.getKey ();
        }
        return null;
    }
}
