// FCMOD

package net.minecraft.src;

public class FCBlockMelon extends FCBlockGourd
{
    protected FCBlockMelon( int iBlockID )
    {
        super( iBlockID );        
    }

    @Override
    public int GetItemIDDroppedOnSaw( World world, int i, int j, int k )
    {
    	return Item.melon.itemID;
    }
    
    @Override
    public int GetItemCountDroppedOnSaw( World world, int i, int j, int k )
    {
    	return 5;
    }
    
    //------------- FCBlockGourd ------------//
    
    @Override
	protected Item ItemToDropOnExplode()
    {
    	return Item.melonSeeds;
    }
	
    @Override
	protected int ItemCountToDropOnExplode()
    {
    	return 5;
    }
	
    @Override
	protected int AuxFXIDOnExplode()
    {
    	return FCBetterThanWolves.m_iMelonExplodeAuxFXID;
    }
    
	protected DamageSource GetFallDamageSource()
	{
		return FCDamageSourceCustom.m_DamageSourceMelon;
	}
	
}
