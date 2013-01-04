/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moniac;

import moniac.Human.Farmer;

/** models anything that can be thought of as a finite, fixed or renewable, resource.
 *
 * @author dave
 */
public abstract class Resource extends Agent
{
    String name;
    float quantity;

    public Resource( String name, float quantity  )
    {
        this.name = name;
        this.quantity = quantity;
    }
    
    public float getQuantity()
    {
        return quantity;
    }

    public void setQuantity( float quantity )
    {
        this.quantity = quantity;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }
    
    public static class Harvest extends Resource
    {
        public Harvest( String name, float quantity )
        {
            super( name, quantity );
        }
        
        public void update( float dt )
        {
            float lossPerYear = 1;
            
            quantity -= quantity * dt * lossPerYear;
        }        
    }
}
