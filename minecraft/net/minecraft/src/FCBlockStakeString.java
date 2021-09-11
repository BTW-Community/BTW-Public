// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockStakeString extends Block
{
	static public final double m_dHeight = ( 0.25D / 16D );
	static public final double m_dHalfHeight = ( m_dHeight / 2D );
	
	static public final double m_dSelectionBoxHeight = ( 1D / 16D );
	static public final double m_dSelectionBoxHalfHeight = ( m_dSelectionBoxHeight / 2D );

	static private final long m_lMinTimeBetweenLengthDisplays = 
		( FCUtilsMisc.m_iTicksPerSecond * 10 );
	
	static long m_lTimeOfLastLengthDisplay = 0;
	static int m_iLengthOfLastLengthDisplay = 0;
	
    public FCBlockStakeString( int iBlockID )
    {
        super( iBlockID, Material.circuits );
    	
        SetAxesEffectiveOn( true );        

		SetFireProperties( FCEnumFlammability.EXTREME );
		
        setStepSound( soundClothFootstep );
        
        setUnlocalizedName( "fcBlockStakeString" );        
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
        MovingObjectPosition rayIntersectPos = null;
        MovingObjectPosition possibleIntersectPoints[] = new MovingObjectPosition[8];
        int iCurrentIntersectIndex = 0;
        
        for ( int iAxis = 0; iAxis < 3; iAxis++ )
        {
        	if ( GetExtendsAlongAxis( world, i, j, k, iAxis ) )
        	{
        		Vec3 boxMin = Vec3.createVectorHelper( 0, 0, 0 );
        		Vec3 boxMax = Vec3.createVectorHelper( 0, 0, 0 );
        		
        		GetBlockBoundsForAxis( iAxis, boxMin, boxMax, m_dSelectionBoxHalfHeight );
        		
    	    	possibleIntersectPoints[iCurrentIntersectIndex] = FCUtilsMisc.RayTraceWithBox( world, i, j, k, boxMin, boxMax, startRay, endRay );
    	    	
    	    	if ( possibleIntersectPoints[iCurrentIntersectIndex] != null )
    	    	{
    	    		iCurrentIntersectIndex++;
    	    	}
        	}
        }
        
        if ( iCurrentIntersectIndex > 0 )
        {
            // scan through the list of intersect points we have built, and check for the first intersection
            
	        iCurrentIntersectIndex--;
	        
	        double dMaxDistance = 0D;
	        
	        for ( ; iCurrentIntersectIndex >= 0; iCurrentIntersectIndex-- )
	        {
	            double dCurrentIntersectDistance = possibleIntersectPoints[iCurrentIntersectIndex].hitVec.squareDistanceTo( endRay );

	            if ( dCurrentIntersectDistance  > dMaxDistance )
	            {
	            	rayIntersectPos = possibleIntersectPoints[iCurrentIntersectIndex];
	            	dMaxDistance = dCurrentIntersectDistance ;
	            }
	        }
        }
        
        return rayIntersectPos;    	
    }
    
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
        return null;
    }
    
	@Override
    public int idDropped( int iMetadata, Random random, int iFortuneModifier )
    {
        return Item.silk.itemID;
    }
	
	@Override
    public void dropBlockAsItemWithChance( World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier )
    {
        if ( !world.isRemote )
        {
	        for ( int iAxis = 0; iAxis < 3; iAxis++ )
	        {
	        	if ( GetExtendsAlongAxisFromMetadata( iMetadata, iAxis ) )
	        	{
	        		FCUtilsItem.DropSingleItemAsIfBlockHarvested( world, i, j, k, idDropped( iMetadata, world.rand, iFortuneModifier ), damageDropped( iMetadata ) );
	        	}        	
	        }
        }
    }
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
		ValidateState( world, i, j, k );
    }
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, 
    	int iFacing, float fXClick, float fYClick, float fZClick )
    {		
		int iLength = ComputeStringLength( world, i, j, k );
		double dInvertedLength = 64D - iLength;
		
		if ( !world.isRemote )
		{
	        float fPitch = (float)Math.pow( 2D, ( dInvertedLength - 32 ) / 32D );
	        
	        world.playSoundEffect( i + 0.5D, j + 0.5D, k + 0.5D, 
	        		"note.bass", 3F, fPitch - 0F );	        		
		}
		else
		{
	        world.spawnParticle( "note", (double)i + 0.5D, (double)j + 1.2D, (double)k + 0.5D, 
	        	dInvertedLength / 64D, 0D, 0D );

	    	long lCurrentTime = world.getWorldTime();
	    	long lDeltaTime = lCurrentTime - m_lTimeOfLastLengthDisplay;
	    	
	    	if ( lDeltaTime < 0 || lDeltaTime >= m_lMinTimeBetweenLengthDisplays ||
	    		 m_iLengthOfLastLengthDisplay != iLength )
    		{	    		
	    		player.addChatMessage( "Sounds like " + ( iLength + 1 ) + "." );
	    		
	    		m_lTimeOfLastLengthDisplay = lCurrentTime;
	    		m_iLengthOfLastLengthDisplay = iLength;
    		}
		}			
		
		return true;
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
    
	//------------- Class Specific Methods ------------//
	
	public void SetExtendsAlongAxis( World world, int i, int j, int k, int iAxis, boolean bExtends )
	{
		SetExtendsAlongAxis( world, i, j, k, iAxis, bExtends, true );
	}
	
	public void SetExtendsAlongAxis( World world, int i, int j, int k, int iAxis, boolean bExtends, boolean bNotify )
	{
		int iMetadata = world.getBlockMetadata( i, j, k ) & (~(1 << iAxis)); // filter out old value
		
		if ( bExtends )
		{
			iMetadata |= 1 << iAxis;
		}
		
		if ( bNotify )
		{
			world.setBlockMetadataWithNotify( i, j, k, iMetadata );
		}
		else
		{
			world.setBlockMetadataWithClient( i, j, k, iMetadata );
		}
	}
	
	public void SetExtendsAlongFacing( World world, int i, int j, int k, int iFacing, boolean bExtends )
	{
		SetExtendsAlongAxis( world, i, j, k, ConvertFacingToAxis( iFacing ), bExtends );
	}
	
	public void SetExtendsAlongFacing( World world, int i, int j, int k, int iFacing, boolean bExtends, boolean bNotify )
	{
		SetExtendsAlongAxis( world, i, j, k, ConvertFacingToAxis( iFacing ), bExtends, bNotify );
	}
	
	public boolean GetExtendsAlongAxis( IBlockAccess blockAccess, int i, int j, int k, int iAxis )
	{
		return GetExtendsAlongAxisFromMetadata( blockAccess.getBlockMetadata( i, j, k ), iAxis );		
	}
	
	public boolean GetExtendsAlongAxisFromMetadata( int iMetadata, int iAxis )
	{
		return ( iMetadata & ( 1 << iAxis ) ) > 0;
	}
	
	public boolean GetExtendsAlongFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
	{
		return GetExtendsAlongAxis( blockAccess, i, j, k, ConvertFacingToAxis( iFacing ) );
	}
	
	public boolean GetExtendsAlongOtherFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
	{
		return GetExtendsAlongOtherAxis( blockAccess, i, j, k, ConvertFacingToAxis( iFacing ) );
	}
	
	public boolean GetExtendsAlongOtherAxis( IBlockAccess blockAccess, int i, int j, int k, int iAxis )
	{
		return GetExtendsAlongOtherAxisFromMetadata( blockAccess.getBlockMetadata( i, j, k ), iAxis );		
	}
	
	public boolean GetExtendsAlongOtherAxisFromMetadata( int iMetadata, int iAxis )
	{
		// Filter out the axis being queried
		
		iMetadata &= ~( 1 << iAxis );
		
		return ( iMetadata & 7 ) != 0;		
	}
	
	static public int ConvertFacingToAxis( int iFacing )
	{
		if ( iFacing == 4 || iFacing == 5 )
		{
			return 0;
		}
		else if ( iFacing == 0 || iFacing == 1 )
		{
			return 1;
		}
		else
		{
			return 2;
		}
	}
	
	private void GetBlockBoundsForAxis( int iAxis, Vec3 min, Vec3 max, double dHalfHeight )
	{
		if ( iAxis == 0 )
		{
			min.setComponents( 0F, 0.5F - dHalfHeight, 0.5F - dHalfHeight );
			max.setComponents( 1F, 0.5F + dHalfHeight, 0.5F + dHalfHeight );
		}
		else if ( iAxis == 1 )
		{
			min.setComponents( 0.5F - dHalfHeight, 0F, 0.5F - dHalfHeight ); 
			max.setComponents( 0.5F + dHalfHeight, 1F, 0.5F + dHalfHeight );
		}
		else // 2
		{
			min.setComponents( 0.5F - dHalfHeight, 0.5F - dHalfHeight, 0F ); 
    		max.setComponents( 0.5F + dHalfHeight, 0.5F + dHalfHeight, 1F );
		}
	}
	
	public void ValidateState( World world, int i, int j, int k )
	{
		int iValidAxisCount = 0;
		
		for ( int iTempAxis = 0; iTempAxis < 3; iTempAxis++ )
		{
			if ( GetExtendsAlongAxis( world, i, j, k, iTempAxis ) )
			{
				if ( HasValidAttachmentPointsAlongAxis( world, i, j, k, iTempAxis ) )
				{
					iValidAxisCount++;
				}
				else
				{
					SetExtendsAlongAxis( world, i, j, k, iTempAxis, false );
					
	        		FCUtilsItem.DropSingleItemAsIfBlockHarvested( world, i, j, k, Item.silk.itemID, 0 );
				}
			}
		}
		
		if ( iValidAxisCount <= 0 )
		{
			// we no longer have any valid axis, destroy the block
			
			world.setBlockWithNotify( i, j, k, 0 );
		}
	}
	
	private boolean HasValidAttachmentPointsAlongAxis( World world, int i, int j, int k, int iAxis )
	{
		int iFacing1;
		int iFacing2;
		
		switch ( iAxis )
		{
			case 0: // i
				
				iFacing1 = 4;
				iFacing2 = 5;
				
				break;
				
			case 1: // j
				
				iFacing1 = 0;
				iFacing2 = 1;
				
				break;
				
			default: // 2 k
				
				iFacing1 = 2;
				iFacing2 = 3;
				
				break;				
		}
		
		return HasValidAttachmentPointToFacing( world, i, j, k, iFacing1 ) && HasValidAttachmentPointToFacing( world, i, j, k, iFacing2 );
	}
	
	private boolean HasValidAttachmentPointToFacing( World world, int i, int j, int k, int iFacing )
	{
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
		
		targetPos.AddFacingAsOffset( iFacing );
		
		int iTargetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
		
		if ( iTargetBlockID == blockID )
		{
			if ( GetExtendsAlongFacing( world, targetPos.i, targetPos.j, targetPos.k, iFacing ) )
			{
				return true;
			}
		}
		else if ( iTargetBlockID == FCBetterThanWolves.fcBlockStake.blockID )
		{
			return true;
		}
		
		return false;
	}
	
	protected int ComputeStringLength( World world, int i, int j, int k )
	{
		int iLength = 0;
		
		for ( int iAxis = 0; iAxis < 3; iAxis++ )
		{
			int iAxisLength = ComputeStringLengthAlongAxis( world, i, j, k, iAxis );
			
			if ( iAxisLength > iLength )
			{
				iLength = iAxisLength;
			}
		}
		
		return iLength;
	}
	
	protected int ComputeStringLengthAlongAxis( World world, int i, int j, int k, int iAxis )
	{
		int iLength = 0;
		
		if ( GetExtendsAlongAxis( world, i, j, k, iAxis ) )
		{
			int iTempFacing = GetFirstFacingForAxis( iAxis );
			
			iLength = ComputeStringLengthToFacing( world, i, j, k, iTempFacing );
			
			iTempFacing = Block.GetOppositeFacing( iTempFacing );
			
			iLength += ComputeStringLengthToFacing( world, i, j, k, iTempFacing );
			
			iLength++; // for the block itself			
		}
		
		return iLength;
	}
	
	protected int ComputeStringLengthToFacing( World world, int i, int j, int k, int iFacing )
	{
		int iLength = 0;
		
		FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k, iFacing );
		
		while ( world.blockExists( tempPos.i, tempPos.j, tempPos.k ) && 
			world.getBlockId( tempPos.i, tempPos.j, tempPos.k ) == blockID )
		{
			iLength++;
			
			tempPos.AddFacingAsOffset( iFacing );
		}
		
		return iLength;
	}
	
	protected int GetFirstFacingForAxis( int iAxis )
	{
		if ( iAxis == 0 )
		{
			return 4;
		}
		else if ( iAxis == 1 )
		{
			return 0;
		}
		else // iAxis == 2
		{
			return 2;
		}
	}
	
	//----------- Client Side Functionality -----------//
	
	@Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, 
    	int iNeighborI, int iNeighborJ, int iNeighborK, int iSide )
    {
		FCUtilsBlockPos myPos = new FCUtilsBlockPos( iNeighborI, iNeighborJ, iNeighborK, 
			GetOppositeFacing( iSide ) );
		
		int iMetadata = blockAccess.getBlockMetadata( myPos.i, myPos.j, myPos.k );
		int iAxis = ConvertFacingToAxis( iSide );
	
		if ( GetExtendsAlongAxisFromMetadata( iMetadata, iAxis ) )
		{
			return GetExtendsAlongOtherAxisFromMetadata( iMetadata, iAxis );
		}
		
		return true;
    }	
	
	@Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool( World world, int i, int j, int k )
    {
		double minXBox = (double)i + 0.5D - m_dSelectionBoxHalfHeight;
		double minYBox = (double)j + 0.5D - m_dSelectionBoxHalfHeight;
		double minZBox = (double)k + 0.5D - m_dSelectionBoxHalfHeight;
		double maxXBox = (double)i + 0.5D + m_dSelectionBoxHalfHeight;
		double maxYBox = (double)j + 0.5D + m_dSelectionBoxHalfHeight;
		double maxZBox = (double)k + 0.5D + m_dSelectionBoxHalfHeight;
		
		if ( GetExtendsAlongAxis( world, i, j, k, 0 ) )
		{
			minXBox = (double)i;
			maxXBox = (double)i + 1D;
		}
		
		if ( GetExtendsAlongAxis( world, i, j, k, 1 ) )
		{
			minYBox = (double)j;
			maxYBox = (double)j + 1D;
		}
		
		if ( GetExtendsAlongAxis( world, i, j, k, 2 ) )
		{
			minZBox = (double)k;
			maxZBox = (double)k + 1D;
		}
		
        return AxisAlignedBB.getAABBPool().getAABB( minXBox, minYBox, minZBox, maxXBox, maxYBox, maxZBox );
    }
    
	private void SetRenderBoundsForAxis( RenderBlocks renderBlocks, int iAxis )
	{
    	Vec3 min = Vec3.createVectorHelper( 0, 0, 0 );
    	Vec3 max = Vec3.createVectorHelper( 0, 0, 0 );
    	
    	GetBlockBoundsForAxis( iAxis, min, max, m_dHalfHeight );
    	
    	renderBlocks.setRenderBounds( (float)min.xCoord, (float)min.yCoord, (float)min.zCoord, (float)max.xCoord, (float)max.yCoord, (float)max.zCoord );    	
	}
	
	@Override
    public int idPicked( World world, int i, int j, int k )
    {
        return idDropped( world.getBlockMetadata( i, j, k ), world.rand, 0 );
    }

    @Override
    public boolean RenderBlock( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	IBlockAccess blockAccess = renderBlocks.blockAccess;
    	
    	for ( int iAxis = 0; iAxis < 3; iAxis++ )
    	{
    		if ( GetExtendsAlongAxis( blockAccess, i, j, k, iAxis ) )
    		{
    			SetRenderBoundsForAxis( renderBlocks, iAxis );
    			
    			renderBlocks.renderStandardBlock( this, i, j, k );
    		}
    	}
    	
    	return true;
    }
}
