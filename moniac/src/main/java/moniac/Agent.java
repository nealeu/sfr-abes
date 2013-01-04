/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moniac;

import java.util.HashMap;
import java.util.Map;

/** models an individual, a group, a company or any other entity that is capable of making decisions in our world.
 *
 * @author dave
 */
public abstract class Agent
{
    public static Map< Class, RuleBase > rulesForClass = new HashMap<Class, RuleBase>();
    
    public void update( float dt )
    {
        RuleBase rules = rulesForClass.get( getClass() );
        
        if( rules != null )
            rules.update( this, dt );
    }
}
