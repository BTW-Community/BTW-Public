// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockSnowCover extends FCBlockGroundCover
{
    protected FCBlockSnowCover( int iBlockID )
    {
        super( iBlockID, Material.snow );
        
        setTickRandomly( true );
        
        setStepSound( soundSnowFootstep );
        
        setUnlocalizedName( "snow" );
    }
    
    @Override
    public boolean canPlaceBlockAt( World world, int i, int j, int k )
    {
    	int iBlockBelowID = world.getBlockId( i, j - 1, k );
    	Block blockBelow = Block.blocksList[iBlockBelowID];
    	
    	if ( blockBelow != null && blockBelow.GetIsBlockWarm( world, i, j - 1, k ) )
    	{
    		return false;
    	}
    	
    	return super.canPlaceBlockAt( world, i, j, k );
    }
    
	// FCTODO: I don't think this function is necessary
    /*
    @Override
    public void harvestBlock( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
        int iItemID = Item.snowball.itemID;
        
        dropBlockAsItem_do( world, i, j, k, new ItemStack( iItemID, 1, 0 ) );
        world.setBlockToAir( i, j, k );
        player.addStat( StatList.mineBlockStatArray[this.blockID], 1 );
    }
    */
    
    @Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Item.snowball.itemID;
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 1;
    }
    
	@Override
    public boolean canDropFromExplosion( Explosion explosion )
    {
        return false;
    }
    
    @Override
    public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
        dropBlockAsItem( world, i, j, k, iMetadata, 0 );
    }
    
    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
        if ( world.getSavedLightValue( EnumSkyBlock.Block, i, j, k ) > 11 )
        {
            world.setBlockToAir( i, j, k );
        }
    }
    
    @Override
	public void OnFluidFlowIntoBlock( World world, int i, int j, int k, BlockFluid newBlock )
	{
    	// override to prevent dropping of snowball
	}

    @Override
    public boolean GetCanBeSetOnFireDirectly( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public boolean GetCanBeSetOnFireDirectlyByItem( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return false;
    }
    
    @Override
    public boolean SetOnFireDirectly( World world, int i, int j, int k )
    {
    	world.setBlockToAir( i, j, k );
		
    	return true;
    }
    
    @Override
    public int GetChanceOfFireSpreadingDirectlyTo( IBlockAccess blockAccess, int i, int j, int k )
    {
		return 60; // same chance as leaves and other highly flammable objects
    }
    
    @Override
    public void OnBrokenByPistonPush( World world, int i, int j, int k, int iMetadata )
    {
    	// override to prevent drop
    }
    
    //------------- Class Specific Methods ------------//
    
    public static boolean CanSnowCoverReplaceBlock( World world, int i, int j, int k )
    {
    	Block block = Block.blocksList[world.getBlockId( i, j, k )];
    	
    	return block == null || block.IsAirBlock() || ( block.IsGroundCover() && block != Block.snow );
    }
    
	//----------- Client Side Functionality -----------//
}
