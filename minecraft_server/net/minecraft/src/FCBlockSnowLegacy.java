// FCMOD

package net.minecraft.src;

public class FCBlockSnowLegacy extends BlockSnowBlock
{
    protected FCBlockSnowLegacy( int iBlockID )
    {
    	super( iBlockID );
    	
    	setHardness( 0.2F );
    	SetShovelsEffectiveOn();
    	
    	SetBuoyant();
    	
    	setStepSound( soundSnowFootstep );
    	
    	setUnlocalizedName( "snow" );
    	
        setCreativeTab( null );    	
    }
    
    @Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
}
