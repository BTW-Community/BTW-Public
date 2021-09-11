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
    
    @Override
    public void randomDisplayTick( World world, int i, int j, int k, Random rand )
    {
    	Vec3 pos = GetParticalPos( world, i, j, k );
    	
        world.spawnParticle( "smoke", pos.xCoord, pos.yCoord, pos.zCoord, 0D, 0D, 0D );
        world.spawnParticle( "flame", pos.xCoord, pos.yCoord, pos.zCoord, 0D, 0D, 0D );
    }
    
    protected Vec3 GetParticalPos( World world, int i, int j, int k )
    {
    	Vec3 pos = Vec3.createVectorHelper( i + 0.5D, j + 0.92D, k + 0.5D );
    	
        int iOrientation = GetOrientation( world, i, j, k );
        
        double dHorizontalOffset = 0.27D;

        if ( iOrientation == 1 )
        {
        	pos.xCoord -= dHorizontalOffset;
        }
        else if ( iOrientation == 2 )
        {
        	pos.xCoord += dHorizontalOffset;
        }
        else if ( iOrientation == 3 )
        {
        	pos.zCoord -= dHorizontalOffset;        	
        }
        else if ( iOrientation == 4 )
        {
        	pos.zCoord += dHorizontalOffset;
        }
        else
        {
        	pos.yCoord -= 0.22D;
        }
        
    	return 	pos;
    }
}
