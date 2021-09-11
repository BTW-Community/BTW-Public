// FCMOD

package net.minecraft.src;

public class AaaFCBlockTemplate extends Block
{
    protected AaaFCBlockTemplate( int iBlockID, Material material )
    {
    	super( iBlockID, Material.rock );
    	
        setHardness( 1F );
        setResistance( 10F ); // most blocks don't need setResistance() as it's done in setHardness()
        SetShovelsEffectiveOn( false );
        SetPicksEffectiveOn( false );
        SetAxesEffectiveOn( false );
        SetChiselsEffectiveOn( false );
        
        InitBlockBounds( 0F, 0F, 0F, 1F, 1F, 1F );
        
        SetNonBuoyant();
		SetFireProperties( FCEnumFlammability.NONE );
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.NONE );
    	SetFilterableProperties( Item.m_iFilterable_SolidBlock );
		SetCanBeCookedByKiln( false );
        
        setLightOpacity( 255 ); // most don't need. 255 is fully opaque
        Block.useNeighborBrightness[iBlockID] = false; // used by slabs and such
        
        setStepSound( soundStoneFootstep );        
        
        setUnlocalizedName( "fcBlockTemplate" );
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
