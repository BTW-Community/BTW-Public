// FCMOD

package net.minecraft.src;

public class FCEntityBat extends EntityBat
{
    public FCEntityBat( World world )
    {
        super( world );
    }
    
    @Override
    public void CheckForScrollDrop()
    {    	
    	if ( rand.nextInt( 250 ) == 0 )
    	{
            ItemStack itemstack = new ItemStack( FCBetterThanWolves.fcItemArcaneScroll, 1, 
            	Enchantment.featherFalling.effectId );
            
            entityDropItem( itemstack, 0F );
    	}
    }
    
    @Override
    protected void dropFewItems( boolean bPlayerKilled, int iFortuneLevel )
    {
    	int iNumDrop = 1;
    	
    	if ( rand.nextInt( 4 ) - iFortuneLevel <= 0 )
    	{
    		iNumDrop = 2;
    	}
    	
        for ( int iTempCount = 0; iTempCount < iNumDrop; iTempCount++ )
        {
            dropItem( FCBetterThanWolves.fcItemBatWing.itemID, 1 );
        }    	
    }
    
    @Override
    public boolean AttractsLightning()
    {
    	return false;
    }
}