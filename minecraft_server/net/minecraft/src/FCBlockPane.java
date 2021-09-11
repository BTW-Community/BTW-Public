// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCBlockPane extends BlockPane
{
    protected FCBlockPane( int iBlockID, String sTextureName, String sSideTextureName, Material material, boolean bCanDropItself )
    {
        super( iBlockID, sTextureName, sSideTextureName, material, bCanDropItself );
        
        InitBlockBounds( 0D, 0D, 0D, 1D, 1D, 1D );
    }
    
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        float fXMin = 0.4375F;
        float fXMax = 0.5625F;
        
        float fZMin = 0.4375F;
        float fZMax = 0.5625F;
        
        boolean bKNeg = canThisPaneConnectToThisBlockID(blockAccess.getBlockId( i, j, k - 1 ) );
        boolean bKPos = canThisPaneConnectToThisBlockID(blockAccess.getBlockId( i, j, k + 1 ) );
        boolean bINeg = canThisPaneConnectToThisBlockID(blockAccess.getBlockId( i - 1, j, k ) );
        boolean bIPos = canThisPaneConnectToThisBlockID(blockAccess.getBlockId( i + 1, j, k ) );

        if ( ( !bINeg || !bIPos ) && ( bINeg || bIPos || bKNeg || bKPos ) )
        {
            if ( bINeg && !bIPos )
            {
                fXMin = 0.0F;
            }
            else if ( !bINeg && bIPos )
            {
                fXMax = 1.0F;
            }
        }
        else
        {
            fXMin = 0.0F;
            fXMax = 1.0F;
        }

        if ( ( !bKNeg || !bKPos ) && ( bINeg || bIPos || bKNeg || bKPos ) )
        {
            if ( bKNeg && !bKPos )
            {
                fZMin = 0.0F;
            }
            else if ( !bKNeg && bKPos )
            {
                fZMax = 1.0F;
            }
        }
        else
        {
            fZMin = 0.0F;
            fZMax = 1.0F;
        }

    	return AxisAlignedBB.getAABBPool().getAABB( fXMin, 0.0F, fZMin, fXMax, 1.0F, fZMax );
    }
    
    @Override
    public void addCollisionBoxesToList( World world, int i, int j, int k, 
    	AxisAlignedBB intersectingBox, List list, Entity entity )
    {
        boolean bKNeg = canThisPaneConnectToThisBlockID( world.getBlockId( i, j, k - 1 ) );
        boolean bKPos = canThisPaneConnectToThisBlockID( world.getBlockId( i, j, k + 1 ) );
        
        boolean bINeg = canThisPaneConnectToThisBlockID( world.getBlockId( i - 1, j, k ) );
        boolean bIPos = canThisPaneConnectToThisBlockID( world.getBlockId( i + 1, j, k ) );

        if ( ( !bINeg || !bIPos ) && ( bINeg || bIPos || bKNeg || bKPos ) )
        {
            if ( bINeg && !bIPos )
            {
            	AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB( 
            		0F, 0F, 0.4375F, 0.5F, 1F, 0.5625F ).offset( i, j, k );

            	tempBox.AddToListIfIntersects( intersectingBox, list );
            }
            else if ( !bINeg && bIPos )
            {
            	AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB( 
                	0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F ).offset( i, j, k );

            	tempBox.AddToListIfIntersects( intersectingBox, list );
            }
        }
        else
        {
        	AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB( 
            	0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F ).offset( i, j, k );
            	
        	tempBox.AddToListIfIntersects( intersectingBox, list );
        }

        if ( ( !bKNeg || !bKPos ) && ( bINeg || bIPos || bKNeg || bKPos ) )
        {
            if ( bKNeg && !bKPos )
            {
        		AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB( 
                	0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F ).offset( i, j, k );
                	
            	tempBox.AddToListIfIntersects( intersectingBox, list );
            }
            else if ( !bKNeg && bKPos )
            {
        		AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB( 
                	0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F ).offset( i, j, k );
                	
            	tempBox.AddToListIfIntersects( intersectingBox, list );
            }
        }
        else
        {
    		AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB( 
            	0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F).offset( i, j, k );
            	
        	tempBox.AddToListIfIntersects( intersectingBox, list );
        }
    }
    
    @Override
    public void setBlockBoundsBasedOnState( IBlockAccess blockAccess, int i, int j, int k )
    {
    	// parent method is deprecated
    }
    
    @Override
    public void setBlockBoundsForItemRender()
    {
    	// parent method is deprecated
    }
    
    //------------- Class Specific Methods ------------//	
    
	//----------- Client Side Functionality -----------//
}