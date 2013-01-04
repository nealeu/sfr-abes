/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moniac;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** a singleton for the global simulation context
 *
 * @author dave
 */
public abstract class World
{
    public static World instance;
    
    public static Random random = new Random();

    List< Tile > tiles = new ArrayList<Tile>();
    
    Market market = new Market();

    public List<Tile> getTiles()
    {
        return tiles;
    }

    public Market getMarket()
    {
        return market;
    }
    
    public abstract Rectangle getBounds(); 
    
    public static class MigrationRecord
    {
        Tile from, to;
        int population;

        public MigrationRecord( Tile from, Tile to, int population )
        {
            this.from = from;
            this.to = to;
            this.population = population;
        }        
    }
    
    public List< MigrationRecord > migrationLog = new ArrayList<MigrationRecord>();
    
    public void update( float dt )
    {
//        market.expireAll();
        
        migrationLog.clear();
        
        for( Tile tile : tiles )
            tile.update( dt );
        
        market.arbitrate();
    }
}
