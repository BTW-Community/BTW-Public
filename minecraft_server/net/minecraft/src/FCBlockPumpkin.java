// FCMOD

package net.minecraft.src;

public class FCBlockPumpkin extends FCBlockGourd
{
    protected FCBlockPumpkin( int iBlockID, boolean bStub )
    {
        super( iBlockID );        
        
        setHardness(1.0F);
        
        setStepSound(soundWoodFootstep);
        
        setUnlocalizedName("fcBlockPumpkinFresh");    
    }

    //------------- FCBlockGourd ------------//
    
    @Override
	protected Item ItemToDropOnExplode()
    {
    	return Item.pumpkinSeeds;
    }
	
    @Override
	protected int ItemCountToDropOnExplode()
    {
    	return 4;
    }
	
    @Override
	protected int AuxFXIDOnExplode()
    {
    	return FCBetterThanWolves.m_iPumpkinExplodeAuxFXID;
    }
    
	protected DamageSource GetFallDamageSource()
	{
		return FCDamageSourceCustom.m_DamageSourcePumpkin;
	}
}
