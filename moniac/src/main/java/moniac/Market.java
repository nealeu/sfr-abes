/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moniac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import moniac.Human.Farmer;
import moniac.World.MigrationRecord;
import moniac.util.FilteredCollection;

/** provides a simple marketplace for offering up jobs and goods in one tile to be satisfied by another.
 *  There may be one or more local or global markets (later we may need a mechanism to control market access).
 *  It's really just a glorified noticeboard that gets reset every tick.
 *
 * @author dave
 */
public class Market
{
    public static abstract class Offer< AGENTA extends Agent, AGENTB extends Agent >
    {
        AGENTA offerer;
        
        public class Bid
        {
            AGENTB bidder;
            float bid;

            public Bid( AGENTB bidder, float bid )
            {
                this.bidder = bidder;
                this.bid = bid;
            }
        }
        
        List< Bid > bids = new ArrayList<Bid>();

        public Offer( AGENTA offerer )
        {
            this.offerer = offerer;
        }
        
        public void bid( AGENTB bidder, float bid )
        {
            if( bid > 0 )
                bids.add( new Bid( bidder, bid ) );
        }
        
        public void arbitrate()
        {
            // choose the top bid for now
            Bid top = null;
            for( Bid bid : bids)
                if( top == null )
                    top = bid;
                else if( bid.bid > top.bid )
                    top = bid;
            
            if( top != null )
                contract( top.bidder );
        }
        
        public abstract void consider( AGENTB bidder );
        
        public abstract void contract( AGENTB bidder );
    }
    
    public static class JobOffer extends Offer< Farm, Tile >
    {
        int workersNeeded;

        public JobOffer( Farm offerer, int workersNeeded )
        {
            super( offerer );
            this.workersNeeded = workersNeeded;
        }

        @Override
		public void consider( Tile bidder )
        {
            bid( bidder, bidder.getPopulation( Human.class ) ); // simple count of unemployed for now
        }

        @Override
		public void contract( Tile bidder )
        {
            Farmer farmers = offerer.getFarmers();
            Farmer migrants = bidder.migrateFarmers( workersNeeded );
            
            if( migrants == null )
                return;
            
            System.out.printf( "%d farmers migrated\n", migrants.population );
            
            World.instance.migrationLog.add( new MigrationRecord( bidder, offerer.getTile(), migrants.population ) );
            
            workersNeeded -= migrants.population;
            
            if( farmers == null )
            {
                offerer.setFarmers( migrants );
            
                offerer.getTile().add( migrants );
            }
            else
            {
                farmers.population += migrants.population;
            }            
        }
    }
    
    public List< Offer > offers = new ArrayList<Offer>();
    
    public void offer( Offer offer )
    {
        offers.add( offer );
    }
    
    public void remove( Offer offer )
    {
        offers.remove( offer );
    }
    
    public < OFFER extends Offer > Collection< OFFER > getOffers( Class< OFFER > type )
    {
        return new FilteredCollection<OFFER>( offers, type );
    }
    
    public void arbitrate()
    {
        for( Offer offer : offers )
            offer.arbitrate();
    }
    
    public void expireAll()
    {
        offers.clear();
    }
}
