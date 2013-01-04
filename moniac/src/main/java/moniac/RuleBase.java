/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moniac;

import java.util.ArrayList;
import java.util.List;
import moniac.Human.Farmer;
import moniac.Market.JobOffer;
import moniac.Farm;
import moniac.Resource.Harvest;
import moniac.Rule.AlwaysRule;

/**
 *
 * @author dave
 */
public class RuleBase
{
    List< Rule > rules = new ArrayList<Rule>();
    
    static Stochastic random = new Stochastic();
    
    public void update( Agent agent, float dt )
    {
        for( Rule rule : rules )
        {
            float level = rule.preCondition( agent );

            if( random.nextFloat() < level )
            {
                rule.enact( agent, dt );
            }
        }
    }
    
    public static Rule breedAndDie = new AlwaysRule< Human >()
    {
        float birthRate = 0.012f, deathRate = 0.009f;

        public void enact( Human agent, float dt)
        {
//            if( agent.getAge() > 90 )
//            {
//                agent.setPopulation( 0 );
//            }
//            else
            {
                // the birth/deathrate is the probability of birth/death within a year.
                // for an agent with a population of N this is a binomial distribution
                // which is close to a poisson distribution with mean N*rate
                int deaths = random.nextPoisson( agent.getPopulation() * deathRate * dt );
                int births = random.nextPoisson( agent.getPopulation() * birthRate * dt );
            
                agent.setPopulation( agent.getPopulation() + births - deaths );
            }
        }
    };
    
    public static Rule eatOrStarve = new AlwaysRule< Human >() 
    {
        public void enact( Human agent, float dt )
        {
            int pop = agent.getPopulation();
            float food = agent.getFood();
            float foodNeeded = pop * dt;
            
            if( pop == 0 )
            {
                // nothing to do
            }
            else if( food == 0 )
            {
                System.out.println( String.format( "%d starved to death", pop ) );
                agent.setSatiation( 0 );
                agent.setPopulation( 0 );
            }
            else
            {
                // tactic: ration to last a year
                // other tactics: take foodNeeded regardless, satiation always 1 but often starves
                float ration = food > pop ? 1 : food / pop;
                agent.setFood( food - foodNeeded * ration );
                agent.setSatiation( ration );
            }            
        }
    };
    
    public static RuleBase standardHumanRules = new RuleBase()
    {
        {
            rules.add( eatOrStarve );
            rules.add( breedAndDie );
        }
    };
    
    public static Rule divideHarvest = new AlwaysRule< Tile >()
    {
        public void enact( Tile tile, float dt )
        {
            for( Resource resource : tile.getResources() )
            {
                if( resource.getQuantity() > 0 && resource instanceof Harvest )
                {
                    Harvest harvest = (Harvest) resource;
                    
                    int pop = tile.getPopulation();
                    float ration = harvest.getQuantity() > pop ? 1 : harvest.getQuantity() / pop;
                    
                    for( Human person : tile.getPeople() )
                    {
                        person.setFood( person.getFood() + ration * person.getPopulation() );
                    }
                    
                    harvest.setQuantity( Math.max(  0, harvest.getQuantity() - pop * ration ) );
                }
            }
        }        
    };
    
    public static RuleBase standardTileRules = new RuleBase()
    {
        {
            rules.add( divideHarvest );
        }
    };
}
