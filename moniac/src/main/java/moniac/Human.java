/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moniac;

/**
 *
 * @author dave
 */
public class Human extends Agent
{
    int population;
    float satiation;
    float food;

    public Human( int population )
    {
        this.population = population;
        this.food = population * 1.5f;
        this.satiation = 1;
    }
    
    public int getPopulation()
    {
        return population;
    }

    public void setPopulation( int population )
    {
        this.population = population;
    }

    public float getFood()
    {
        return food;
    }

    public void setFood( float food )
    {
        this.food = food;
    }

    public float getSatiation()
    {
        return satiation;
    }

    public void setSatiation( float satiation )
    {
        this.satiation = satiation;
    }
    
    public static class Farmer extends Human
    {
        public Farmer( int population )
        {
            super( population );
        }
    }
}
