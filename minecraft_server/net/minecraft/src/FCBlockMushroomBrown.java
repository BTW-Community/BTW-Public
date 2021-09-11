// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockMushroomBrown extends FCBlockMushroom
{
    protected FCBlockMushroomBrown( int iBlockID, String iconName )
    {
    	super( iBlockID, iconName );
    }
    
    @Override
    public int idDropped( int iMetaData, Random rand, int iFortuneModifier )
    {
		return FCBetterThanWolves.fcItemMushroomBrown.itemID;
    }
    
    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
		// only allow growth in the overworld
    	
    	if ( world.provider.dimensionId == 0 )
    	{
            if ( world.getBlockId( i, j - 1, k ) == Block.mycelium.blockID && rand.nextInt( 50 ) == 0 )
            {
            	// mushrooms growing on mycelium have a chance of sprouting into giant mushrooms
            	
        		fertilizeMushroom( world, i, j, k, rand );
            }
            else
            {
            	CheckForSpread( world, i, j, k, rand );
            }       
    	}    	
    }

    protected boolean CanSpreadToOrFromLocation( World world, int i, int j, int k )
	{
        int iBlockBelowID = world.getBlockId(i, j - 1, k);
        
        return iBlockBelowID == Block.mycelium.blockID || world.getFullBlockLightValue( i, j, k ) == 0;    	
	}
    
    protected void CheckForSpread( World world, int i, int j, int k, Random rand )
    {
    	// basically a copy/paste of the BlockMushroom updateTick cleaned up and with additional requirements that brown mushrooms can only grow in complete darkness
    	
        if ( rand.nextInt( 25 ) == 0 && CanSpreadToOrFromLocation( world, i, j, k ) )
        {
            int iHorizontalSpreadRange = 4;
            int iNeighboringMushroomsCountdown = 5;
            
            for ( int iTempI = i - iHorizontalSpreadRange; iTempI <= i + iHorizontalSpreadRange; ++iTempI )
            {
                for ( int iTempK = k - iHorizontalSpreadRange; iTempK <= k + iHorizontalSpreadRange; ++iTempK )
                {
                    for ( int iTempJ = j - 1; iTempJ <= j + 1; ++iTempJ )
                    {
                        if ( world.getBlockId( iTempI, iTempJ, iTempK ) == blockID )
                        {
                            --iNeighboringMushroomsCountdown;

                            if (iNeighboringMushroomsCountdown <= 0)
                            {
                                return;
                            }
                        }
                    }
                }
            }

            int iSpreadI = i + rand.nextInt(3) - 1;
            int iSpreadK = j + rand.nextInt(2) - rand.nextInt(2);
            int iSpreadJ = k + rand.nextInt(3) - 1;

            for ( int iTempCount = 0; iTempCount < 4; ++iTempCount )
            {
                if ( world.isAirBlock( iSpreadI, iSpreadK, iSpreadJ ) && canBlockStay(world, iSpreadI, iSpreadK, iSpreadJ ) &&
                	CanSpreadToOrFromLocation( world, iSpreadI, iSpreadK, iSpreadJ ) )
                {
                    i = iSpreadI;
                    j = iSpreadK;
                    k = iSpreadJ;
                }

                iSpreadI = i + rand.nextInt(3) - 1;
                iSpreadK = j + rand.nextInt(2) - rand.nextInt(2);
                iSpreadJ = k + rand.nextInt(3) - 1;
            }

            if ( world.isAirBlock( iSpreadI, iSpreadK, iSpreadJ ) && canBlockStay( world, iSpreadI, iSpreadK, iSpreadJ ) && 
            	CanSpreadToOrFromLocation( world, iSpreadI, iSpreadK, iSpreadJ ) )
            {
                world.setBlock( iSpreadI, iSpreadK, iSpreadJ, blockID );
            }
        }
    }
    
    @Override
    public boolean CanBlockStayDuringGenerate( World world, int i, int j, int k )
    {
		if ( j > 24 || world.provider.dimensionId != 0 )
		{
            int iBlockBelowID = world.getBlockId(i, j - 1, k);
            
            if (  iBlockBelowID != Block.mycelium.blockID )
            {
				// only allow brown mushrooms to spawn naturally deep in the earth (bottom strata), and in the overworld unless they're on mycelium
            	
				return false;
            }
		}
    	
    	return super.CanBlockStayDuringGenerate( world, i, j, k );
    }
}