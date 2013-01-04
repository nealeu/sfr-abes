/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moniac;

/** specifies a single decision an Agent might make.
 *  A rule is split into two parts:
 *  - a pre-condition that yields an activation level for the rule
 *  - an action to be performed if the rule is triggered
 * @author dave
 */
public interface Rule< AGENT extends Agent >
{
    public static float ACTIVATE_NEVER = 0, ACTIVATE_ALWAYS = 1;
    
    public float preCondition( AGENT agent );
    
    public void enact( AGENT agent, float dt);            
    
    public abstract class AlwaysRule< AGENT extends Agent > implements Rule< AGENT >
    {
        @Override
		public float preCondition( AGENT agent )
        {
            return ACTIVATE_ALWAYS;
        }
    }
}
