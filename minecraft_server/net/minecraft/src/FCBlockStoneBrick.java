// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockStoneBrick extends BlockStoneBrick
{
    public FCBlockStoneBrick( int iBlockID )
    {
    	super( iBlockID );
    	
    	setHardness( 2.25F );
    	setResistance( 10F );
        
        SetPicksEffectiveOn();
    	
    	setStepSound( soundStoneFootstep );
    	
    	setUnlocalizedName( "stonebricksmooth" );
    	
        setTickRandomly( true );
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
		int iMetadata = world.getBlockMetadata( i, j, k );
		
		if ( iMetadata == 0 )
		{
			// check for drip conditions
			
			if ( !world.getBlockMaterial( i, j - 1, k ).blocksMovement() )
			{
				int iBlockAboveID = world.getBlockId( i, j + 1, k );
				
				if ( iBlockAboveID == Block.waterMoving.blockID || iBlockAboveID == Block.waterStill.blockID )
				{
					if ( random.nextInt( 15 ) == 0 )
					{
						world.setBlockMetadataWithNotify( i, j, k, 1 );
						
						world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
					}
				}
				else if ( iBlockAboveID == Block.lavaMoving.blockID || iBlockAboveID == Block.lavaStill.blockID )
				{
					if ( random.nextInt( 15 ) == 0 )
					{
						world.setBlockMetadataWithNotify( i, j, k, 2 );
						
						world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
					}
				}
			}			
		}
    }
	
    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcBlockStoneBrickLoose.blockID;
    }
    
	@Override
	public int damageDropped( int iMetadata )
    {
		// override vanilla class that drops full blocks of various subtypes (mossy, cracked, etc.)
		
        return 0;
    }    
    
    @Override
    public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
        dropBlockAsItem( world, i, j, k, iMetadata, 0 );
    }
    
	@Override
	public boolean HasMortar( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
    //------------- Class Specific Methods ------------//    
    
	//----------- Client Side Functionality -----------//
}
