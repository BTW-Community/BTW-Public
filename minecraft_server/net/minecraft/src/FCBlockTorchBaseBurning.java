// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockTorchBaseBurning extends FCBlockTorchBase
{
    protected FCBlockTorchBaseBurning( int iBlockID )
    {
    	super( iBlockID );
    }
    
    @Override
    public boolean GetCanBlockLightItemOnFire( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return true;
    }    
    
    @Override
	public void OnFluidFlowIntoBlock( World world, int i, int j, int k, BlockFluid newBlock )
	{
    	if ( newBlock.blockMaterial == Material.water )
    	{
	        world.playAuxSFX( FCBetterThanWolves.m_iFireFizzSoundAuxFXID, i, j, k, 0 );
	        
	        dropBlockAsItem_do( world, i, j, k, new ItemStack( FCBetterThanWolves.fcBlockTorchNetherUnlit.blockID, 1, 0 ) );
    	}
    	else
    	{
    		super.OnFluidFlowIntoBlock( world, i, j, k, newBlock );
    	}
	}

	//----------- Client Side Functionality -----------//
}
