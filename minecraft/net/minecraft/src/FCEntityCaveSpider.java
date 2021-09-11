// FCMOD

package net.minecraft.src;

public class FCEntityCaveSpider extends FCEntitySpider
{
    public FCEntityCaveSpider( World world )
    {
        super( world );
        
        texture = "/mob/cavespider.png";
        
        setSize( 0.7F, 0.5F );
    }
    
    @Override
    public int getMaxHealth()
    {
        return 12;
    }

    @Override
    public boolean attackEntityAsMob( Entity target )
    {
        if ( super.attackEntityAsMob( target ) )
        {
            if ( target instanceof EntityLiving )
            {
                ((EntityLiving)target).addPotionEffect( new PotionEffect(
                	Potion.poison.id, 7 * 20, 0 ) );
            }

            return true;
        }
        
        return false;
    }

    @Override
    public void initCreature() 
    {
    	// skip spider jockey chance in parent    	
    }
    
    @Override
	public boolean DoesLightAffectAggessiveness()
	{
		return false;
	}
    
    @Override
    protected boolean DropsSpiderEyes()
    {
    	return false;
    }
    
    @Override
	protected void CheckForSpiderSkeletonMounting()
	{
	}
	
	//----------- Client Side Functionality -----------//

    @Override
    public float spiderScaleAmount()
    {
        return 0.7F;
    }
}