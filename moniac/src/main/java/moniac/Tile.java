/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moniac;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import moniac.Human.Farmer;
import moniac.Market.JobOffer;
import moniac.util.FilteredCollection;

/** models a discrete parcel of space/land. Geometry/topology is modelled by neighbour connections.
 *
 * @author dave
 */
public class Tile extends Agent
{
    Shape perimeter;
    
    List< Human >    people     = new ArrayList<Human>();
    
    Map< String, Resource > resources  = new HashMap();
 
    List< Tile > neighbours = new ArrayList<Tile>();

    public Tile( Shape perimeter )
    {
        this.perimeter = perimeter;
    }

    public Shape getPerimeter()
    {
        return perimeter;
    }

    public List<Human> getPeople()
    {
        return people;
    }
    
    public void add( Human agent )
    {
        people.add( agent );
    }
    
    public int getPopulation()
    {
        int population = 0;
        
        for( Human agent : people )
            population += agent.getPopulation();
        
        return population;
    }

    public int getPopulation( Class type )
    {
        int population = 0;
        
        for( Human agent : people )
            if( agent.getClass() == type )
                population += agent.getPopulation();
        
        return population;
    }

    public int getPopulation( Filter filter )
    {
        int population = 0;
        
        for( Human agent : people )
            if( filter.accept( agent ) )
                population += agent.getPopulation();
        
        return population;
    }
    
    public int getUnemployed()
    {
        return getPopulation( Human.class );
    }
    
    public Collection<Resource> getResources()
    {
        return resources.values();
    }
    
    public < R extends Resource > Collection< R > getResources( Class< R > type )
    {
        return new FilteredCollection( resources.values(), type );
    }
    
    public void add( Resource resource )
    {
        resources.put( resource.getName(), resource );
    }
    
    public Resource getResource( String name )
    {
        return resources.get( name );
    }

    @Override
	public void update( float dt )
    {
        super.update( dt );
        
        for( Resource resource : resources.values() )
            resource.update( dt );
        
        for( Human agent : people )
            agent.update( dt );
        
        for( JobOffer offer : World.instance.market.getOffers( JobOffer.class ) )
            offer.consider( this );
        
        // remove zero pop agents
        
//        for( Iterator< Human > iAgent = people.iterator(); iAgent.hasNext(); )
//            if( iAgent.next().getPopulation() <= 0 )
//                iAgent.remove();
    }

    Farmer migrateFarmers( int workersNeeded )
    {
        int workersSupplied = 0;
        
        for( Human agent : people )
            if( agent.getClass() == Human.class )
            {
                int workers = Math.min( agent.getPopulation(), workersNeeded - workersSupplied );

                workersSupplied += workers;
                
                agent.setPopulation( agent.getPopulation() - workers );                
            }
        
        return workersSupplied > 0 ? new Farmer( workersSupplied ) : null;
    }
    
    public interface Filter< T >
    {
        public boolean accept( T t );
    }
}
