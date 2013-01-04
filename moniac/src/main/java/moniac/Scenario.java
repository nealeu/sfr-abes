/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moniac;

import moniac.Human.Farmer;

/**
 *
 * @author dave
 */
public abstract class Scenario
{
    public String name;

    public Scenario( String name )
    {
        this.name = name;
    }
    
    public abstract void setup();
    
    public static Scenario scenario1 = new Scenario( "Scenario 1" )
    {
        @Override
		public void setup()
        {
            World.instance = new SimpleWorld( 10, 10 );
            
            Agent.rulesForClass.put( Human.class, RuleBase.standardHumanRules );
            Agent.rulesForClass.put( Farmer.class, RuleBase.standardHumanRules );
            Agent.rulesForClass.put( Tile.class, RuleBase.standardTileRules );
            
            for( Tile tile : World.instance.getTiles() )
            {
                int pop = Math.max( 0, (int) Math.round( 100 + World.random.nextGaussian() * 100 ) );
                tile.getPeople().add( new Human( pop ) );
                
                tile.add( new Farm( "Farm", 200, tile ) );
                
//                for( int age = 10; age < 90; age += 10 )
//                {
//                    int pop = Math.max( 0, (int) Math.round( World.random.nextGaussian() * 100 ) );
//                    location.getPeople().add( new Human( pop, age ) );
//                }
            }
        }
    };
}
