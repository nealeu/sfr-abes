/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moniac;

import moniac.Human.Farmer;
import moniac.Market.JobOffer;
import moniac.Market.Offer;

/**
 *
 * @author dave
 */
public class Farm extends Resource
{
    Tile tile;
    Farmer farmers;
    /** a measure of how much the land is being farmed / fertilised / improved */
    float improvement = 0;
    /** when the clock reaches zero a harvest is created */
    float timeToMature = 1;
    float yieldPerWorker = 10;
    
    Offer outstandingOffer;

    public Farm( String name, float quantity, Tile tile )
    {
        super( name, quantity );
        this.tile = tile;
    }

    public Tile getTile()
    {
        return tile;
    }

    public int getWorkersNeeded()
    {
        return Math.round( quantity / yieldPerWorker );
    }

    public Farmer getFarmers()
    {
        return farmers;
    }

    public void setFarmers( Farmer farmers )
    {
        this.farmers = farmers;
    }

    @Override
	public void update( float dt )
    {
        timeToMature -= dt;
        if (timeToMature > 0)
        {
            // simple rule for improvement
            int workersNeeded = getWorkersNeeded();
            int workersAvailable = farmers != null ? farmers.getPopulation() : 0; // for now
            
            if( outstandingOffer != null )
                World.instance.market.remove( outstandingOffer );
            
            if( workersAvailable < workersNeeded )
                World.instance.market.offer( outstandingOffer = new JobOffer( this, workersNeeded - workersAvailable ) );
            else
                outstandingOffer = null;
            
            // for now
            improvement += (workersAvailable >= workersNeeded ? dt : dt * workersAvailable / workersNeeded);
        }
        else if( improvement > 0 )
        {
            tile.add( new Harvest( "Harvest", quantity * improvement ) );
            improvement = 0; // start again
            // start again
            timeToMature = 1; // annual harvest
            // annual harvest
        }
    }
    
}
