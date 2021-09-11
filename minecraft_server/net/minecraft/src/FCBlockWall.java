// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockWall extends BlockWall
{
    public FCBlockWall( int iBlockID, Block baseBlock )
    {
    	super( iBlockID, baseBlock );
    	
        setCreativeTab( CreativeTabs.tabDecorations );        
    }
    
    @Override
	public boolean HasCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
		return iFacing == 0 || iFacing == 1;
	}
    
	@Override
    public int GetWeightOnPathBlocked( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return -3;
    }
    
    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
		int iType = iMetadata & 1;
		
		if ( iType == 0 ) // cobble
		{
			return FCBetterThanWolves.fcItemStone.itemID;
		}
		
		return super.idDropped( iMetadata, rand, iFortuneModifier );
    }
    
	@Override
	public int damageDropped( int iMetadata )
    {
		int iType = iMetadata & 1;
		
		if ( iType == 0 ) // cobble
		{
			return 0;
		}
		
		return super.damageDropped( iMetadata );
    }
    
	@Override
    public void dropBlockAsItemWithChance( World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier )
    {
		int iType = iMetadata & 1;
		
		if ( iType == 0 ) // cobble
		{
	        if ( !world.isRemote )
	        {
		        int iNumDropped = 4;
		        
		        for(int k1 = 0; k1 < iNumDropped; k1++)
		        {
		            int iItemID = idDropped( iMetadata, world.rand, iFortuneModifier );
		            
		            if ( iItemID > 0 )
		            {
		                dropBlockAsItem_do( world, i, j, k, new ItemStack( iItemID, 1, damageDropped( iMetadata ) ) );
		            }
		        }
	        }	        
		}
		else
		{
			super.dropBlockAsItemWithChance( world, i, j, k, iMetadata, fChance, iFortuneModifier );
		}
    }

    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
    	return world.doesBlockHaveSolidTopSurface( i, j - 1, k );
    }
    
    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return -1F;        
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
        boolean bKNeg = canConnectWallTo( blockAccess, i, j, k - 1 );
        boolean bKPos = canConnectWallTo( blockAccess, i, j, k + 1 );
        boolean bINeg = canConnectWallTo( blockAccess, i - 1, j, k );
        boolean bIPos = canConnectWallTo( blockAccess, i + 1, j, k );
        
        float fXMin = 0.25F;
        float fXMax = 0.75F;
        
        float fZMin = 0.25F;
        float fZMax = 0.75F;
        
        float fYMax = 1.0F;

        if ( bKNeg )
        {
            fZMin = 0.0F;
        }

        if ( bKPos )
        {
            fZMax = 1.0F;
        }

        if ( bINeg )
        {
            fXMin = 0.0F;
        }

        if ( bIPos )
        {
            fXMax = 1.0F;
        }

        if ( bKNeg && bKPos && !bINeg && !bIPos )
        {
            fYMax = 0.8125F;
            fXMin = 0.3125F;
            fXMax = 0.6875F;
        }
        else if ( !bKNeg && !bKPos && bINeg && bIPos )
        {
            fYMax = 0.8125F;
            fZMin = 0.3125F;
            fZMax = 0.6875F;
        }

    	return AxisAlignedBB.getAABBPool().getAABB(         	
    		fXMin, 0.0F, fZMin, fXMax, fYMax, fZMax );
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
    	AxisAlignedBB box = GetBlockBoundsFromPoolBasedOnState( world, i, j, k );
    	
        box.maxY = 1.5D;
        
        return box.offset( i, j, k );
    }
    
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}