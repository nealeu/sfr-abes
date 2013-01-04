/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moniac;

import java.awt.Rectangle;

/**
 *
 * @author dave
 */
public class SimpleWorld extends World
{
    Rectangle bounds;
    
    public SimpleWorld( int numX, int numY )
    {
        for( int x = 0; x < numX; x++ )
        {
            for( int y = 0; y < numY; y++ )
            {
                tiles.add( new Tile( new Rectangle( x, y, 1, 1 ) ) );
            }
        }
        
        bounds = new Rectangle( 0, 0, numX, numY );
    }

    @Override
    public Rectangle getBounds()
    {
        return bounds;
    }
}
