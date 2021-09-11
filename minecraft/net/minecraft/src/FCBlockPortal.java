// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockPortal extends BlockPortal
{
	private static final int m_iChanceOfCheckingForPossession = 10;
	private static final int m_iCreaturePossessionRange = 16;
	
    public FCBlockPortal( int iBlockID )
    {
    	super( iBlockID );
    	
    	setHardness( -1F );
    	
    	setLightValue( 0.75F );
    	
    	setStepSound( soundGlassFootstep );
    	
    	setUnlocalizedName( "portal" );
    }

    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
    	// override to prevent pigmen spawning around portals in the overworld and to possess nearby animals and villagers
    	
    	if ( rand.nextInt( m_iChanceOfCheckingForPossession ) == 0 && world.provider.isSurfaceWorld() )
    	{
    		EntityCreature.AttemptToPossessCreaturesAroundBlock( world, i, j, k, 1, m_iCreaturePossessionRange );
    	}
    	
    	// set this here due to legacy portals having potentially not set it on creation
    	
		FCUtilsWorld.GameProgressSetNetherBeenAccessedServerOnly();    	
    }
    
    @Override
    public boolean tryToCreatePortal( World world, int i, int j, int k )
    {
    	// function overriden to test properly for air blocks
    	
        byte bObsidianNeighboringI = 0;
        byte bObsidianNeighboringK = 0;

        if ( world.getBlockId( i - 1, j, k ) == Block.obsidian.blockID || world.getBlockId( i + 1, j, k ) == Block.obsidian.blockID )
        {
            bObsidianNeighboringI = 1;
        }

        if ( world.getBlockId( i, j, k - 1 ) == Block.obsidian.blockID || world.getBlockId( i, j, k + 1 ) == Block.obsidian.blockID )
        {
            bObsidianNeighboringK = 1;
        }

        if ( bObsidianNeighboringI == bObsidianNeighboringK )
        {
            return false;
        }
        else
        {
	        if ( world.getBlockId( i - bObsidianNeighboringI, j, k - bObsidianNeighboringK ) != Block.obsidian.blockID )
            {
                i -= bObsidianNeighboringI;
                k -= bObsidianNeighboringK;
            }

            int var7;
            int var8;

            for (var7 = -1; var7 <= 2; ++var7)
            {
                for (var8 = -1; var8 <= 3; ++var8)
                {
                    boolean var9 = var7 == -1 || var7 == 2 || var8 == -1 || var8 == 3;

                    if (var7 != -1 && var7 != 2 || var8 != -1 && var8 != 3)
                    {
                        int var10 = world.getBlockId(i + bObsidianNeighboringI * var7, j + var8, k + bObsidianNeighboringK * var7);

                        if (var9)
                        {
                            if (var10 != Block.obsidian.blockID)
                            {
                                return false;
                            }
                        }
		                else if (!world.isAirBlock(i + bObsidianNeighboringI * var7, j + var8, k + bObsidianNeighboringK * var7) && 
		                	var10 != Block.fire.blockID && 
		                	var10 != FCBetterThanWolves.fcBlockCampfireLarge.blockID &&
		                	var10 != FCBetterThanWolves.fcBlockCampfireMedium.blockID &&
		                	var10 != FCBetterThanWolves.fcBlockCampfireSmall.blockID &&
		                	var10 != FCBetterThanWolves.fcBlockCampfireUnlit.blockID )
                        {
                            return false;
                        }
                    }
                }
            }

            for (var7 = 0; var7 < 2; ++var7)
            {
                for (var8 = 0; var8 < 3; ++var8)
                {
                    world.setBlock( i + bObsidianNeighboringI * var7, j + var8, k + bObsidianNeighboringK * var7, Block.portal.blockID, 0, 2);
                }
            }
            
    		FCUtilsWorld.GameProgressSetNetherBeenAccessedServerOnly();    	

            return true;
        }
    }
    
	@Override
    public void setBlockBoundsBasedOnState( IBlockAccess blockAccess, int i, int j, int k )
    {
    	// override to deprecate parent
    }
	
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        float fHalfWidth;
        float fHalfDepth;

        if ( blockAccess.getBlockId( i - 1, j, k ) != blockID && 
        	blockAccess.getBlockId( i + 1, j, k ) != blockID )
        {
            fHalfWidth = 0.125F;
            fHalfDepth = 0.5F;
        }
        else
        {
            fHalfWidth = 0.5F;
            fHalfDepth = 0.125F;
        }
        
    	return AxisAlignedBB.getAABBPool().getAABB(         	
    		0.5F - fHalfWidth, 0.0F, 0.5F - fHalfDepth, 
    		0.5F + fHalfWidth, 1.0F, 0.5F + fHalfDepth );
    }
    
    @Override
    public ItemStack GetStackRetrievedByBlockDispenser( World world, int i, int j, int k )
    {	 
    	return null; // can't be picked up
    }
    
	//------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}