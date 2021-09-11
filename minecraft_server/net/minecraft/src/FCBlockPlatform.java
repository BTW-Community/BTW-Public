// FCMOD

package net.minecraft.src;

public class FCBlockPlatform extends Block
{
	private boolean bPlatformAlreadyConsideredForEntityConversion[][][];
	private boolean bPlatformAlreadyConsideredForConnectedTest[][][];
	
	protected FCBlockPlatform( int iBlockID )
	{
        super( iBlockID, Material.wood );

        setHardness( 2F );
        SetAxesEffectiveOn();
        
        SetBuoyancy( 1F );
        
		SetFireProperties( FCEnumFlammability.WICKER );
		
        setStepSound( soundWoodFootstep );
        
        setUnlocalizedName( "fcBlockPlatform" );
        
        bPlatformAlreadyConsideredForEntityConversion = new boolean[5][5][5];
        bPlatformAlreadyConsideredForConnectedTest = new boolean[5][5][5];
        
        ResetPlatformConsideredForEntityConversionArray();
        ResetPlatformConsideredForConnectedTestArray();
        
        setCreativeTab( CreativeTabs.tabTransport );
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
	public boolean HasCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	return true;
	}
    
    @Override
	public boolean HasLargeCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
    	return iFacing <= 1;
	}
    
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess iBlockAccess, int i, int j, int k )
	{
		return true;
	}
	
	@Override
	public boolean CanTransmitRotationHorizontallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
	@Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
	@Override
    public boolean IsNormalCube( IBlockAccess blockAccess, int i, int j, int k )
    {
		// so that torches can be placed on its sides and such
		
		return true;
    }
    
    //------------- Class Specific Methods ------------//
    
    private void ConvertToEntity( World world, int i, int j, int k, FCEntityMovingAnchor associatedAnchorEntity )
    {
		FCEntityMovingPlatform entityPlatform = new FCEntityMovingPlatform( world, 
	    		(float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, 
	    		associatedAnchorEntity ); 
				
        world.spawnEntityInWorld( entityPlatform );
        
        // convert any suitable blocks above to entities as well
        
        AttemptToLiftBlockWithPlatform( world, i, j + 1, k );
        
        world.setBlockWithNotify( i, j, k, 0 );        
    }
    
    private void AttemptToLiftBlockWithPlatform( World world, int i, int j, int k )
    {
        if ( FCEntityBlockLiftedByPlatform.CanBlockBeConvertedToEntity( world, i, j, k ) )
        {
        	new FCEntityBlockLiftedByPlatform( world, i, j, k );
        }
    }
    
    private int GetDistToClosestConnectedAnchorPoint( World world, int i, int j, int k )
    {
    	// returns -1 if no anchor point found within range
    	
    	int iClosestDist = -1;
    	
    	for ( int tempI = i - 2; tempI <= i + 2; tempI++ )
    	{
        	for ( int tempJ = j - 2; tempJ <= j + 2; tempJ++ )
        	{
            	for ( int tempK = k - 2; tempK <= k + 2; tempK++ )
            	{
            		int iTempBlockID = world.getBlockId( tempI, tempJ, tempK );
            		
            		if ( iTempBlockID == blockID )
            		{
            			// this is another platform, check if it has an attached anchor
            			
            			int iUpwardsBlockID = world.getBlockId( tempI, tempJ + 1, tempK );
            			
            			if ( iUpwardsBlockID == FCBetterThanWolves.fcAnchor.blockID )
            			{
            				if ( ((FCBlockAnchor)(FCBetterThanWolves.fcAnchor)).GetFacing( world, tempI, tempJ + 1, tempK ) == 1 )
            				{
            					if ( IsPlatformConnectedToAnchorPoint( world, i, j, k, tempI, tempJ, tempK ) )
            					{            							
	            					int iTempDist = Math.abs( tempI - i ) + Math.abs( tempJ - j )  +  
	            						Math.abs( tempK - k );
	            					
	            					if ( iClosestDist == -1 || iTempDist < iClosestDist )
	            					{
	            						iClosestDist = iTempDist;
	            					}
            					}
            				}
            			}
            		}            		
            	}
        	}
    	}
    	
    	return iClosestDist;
    }
    
    private boolean IsPlatformConnectedToAnchorPoint( World world,
		int platformI, int platformJ, int platformK,
		int anchorPointI, int anchorPointJ, int anchorPointK )
    {
    	ResetPlatformConsideredForConnectedTestArray();
    	
    	if ( platformI == anchorPointI &&
			platformJ == anchorPointJ &&
			platformK == anchorPointK )
    	{
    		return true;    		
    	}
    	
    	return PropogateTestForConnected( world, 
			anchorPointI, anchorPointJ, anchorPointK,
			anchorPointI, anchorPointJ, anchorPointK,
			platformI, platformJ, platformK );    	
    }    
    
    private boolean PropogateTestForConnected( World world, int i, int j, int k, 
    		int sourceI, int sourceJ, int sourceK, 
    		int targetI, int targetJ, int targetK )
    {
    	int iDeltaI = i - sourceI;
    	int iDeltaJ = j - sourceJ;
    	int iDeltaK = k - sourceK;
    	
		if ( bPlatformAlreadyConsideredForConnectedTest[iDeltaI + 2][iDeltaJ + 2][iDeltaK + 2] )
		{
			return false;
		}
		
		bPlatformAlreadyConsideredForConnectedTest[iDeltaI + 2][iDeltaJ + 2][iDeltaK + 2] = true;
		
    	for ( int iFacing = 0; iFacing < 6; iFacing++ )
    	{
    		FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k );
    		
    		tempPos.AddFacingAsOffset( iFacing );
    		
    		if ( tempPos.i == targetI &&
				tempPos.j == targetJ &&
				tempPos.k == targetK )
    		{
    			return true;
    		}
    		
    		int iTempBlockID = world.getBlockId( tempPos.i, tempPos.j, tempPos.k );
    		
    		if ( iTempBlockID == blockID )
    		{
	    		int tempDistI = Math.abs( sourceI - tempPos.i );
	    		int tempDistJ = Math.abs( sourceJ - tempPos.j );
	    		int tempDistK = Math.abs( sourceK - tempPos.k );
	    		
	    		if ( tempDistI <= 2 && tempDistJ <= 2 && tempDistK <= 2  )
	    		{
	    			if ( PropogateTestForConnected( world, tempPos.i, tempPos.j, tempPos.k, 
    					sourceI, sourceJ, sourceK, targetI, targetJ, targetK ) )
	    			{
	    				return true;
	    			}
    			}
    		}
    	}
    	
    	return false;
    }
    
    void ResetPlatformConsideredForEntityConversionArray()
    {
    	for ( int tempI = 0; tempI < 5; tempI++ )
    	{
        	for ( int tempJ = 0; tempJ < 5; tempJ++ )
        	{
            	for ( int tempK = 0; tempK < 5; tempK++ )
            	{
            		bPlatformAlreadyConsideredForEntityConversion[tempI][tempJ][tempK] = false;
            	}
        	}
    	}
    }
    
    void ResetPlatformConsideredForConnectedTestArray()
    {
    	for ( int tempI = 0; tempI < 5; tempI++ )
    	{
        	for ( int tempJ = 0; tempJ < 5; tempJ++ )
        	{
            	for ( int tempK = 0; tempK < 5; tempK++ )
            	{
            		bPlatformAlreadyConsideredForConnectedTest[tempI][tempJ][tempK] = false;
            	}
        	}
    	}
    }
    
    public void CovertToEntitiesFromThisPlatform( World world, int i, int j, int k, 
		FCEntityMovingAnchor associatedAnchorEntity )
    {
    	ResetPlatformConsideredForEntityConversionArray();
    	
    	PropogateCovertToEntity( world, i, j, k, associatedAnchorEntity, i, j, k );
    }
    
    private void PropogateCovertToEntity( World world, int i, int j, int k, 
		FCEntityMovingAnchor associatedAnchorEntity, int sourceI, int sourceJ, int sourceK )
    {
    	int iDeltaI = i - sourceI;
    	int iDeltaJ = j - sourceJ;
    	int iDeltaK = k - sourceK;
    	
		if ( bPlatformAlreadyConsideredForEntityConversion[iDeltaI + 2][iDeltaJ + 2][iDeltaK + 2] )
		{
			return;
		}
		
		bPlatformAlreadyConsideredForEntityConversion[iDeltaI + 2][iDeltaJ + 2][iDeltaK + 2] = true;
		
    	int distToSource = Math.abs( iDeltaI ) + Math.abs( iDeltaJ )  +  Math.abs( iDeltaK );
    	
    	int closestAnchorDist = GetDistToClosestConnectedAnchorPoint( world, i, j, k );

    	if ( closestAnchorDist == -1 || distToSource <= closestAnchorDist )
    	{
    		ConvertToEntity( world, i, j, k, associatedAnchorEntity );
    	}
    	else
    	{
    		return;
    	}
    	
    	for ( int iFacing = 0; iFacing < 6; iFacing++ )
    	{
    		FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k );
    		
    		tempPos.AddFacingAsOffset( iFacing );
    		
    		int iTempBlockID = world.getBlockId( tempPos.i, tempPos.j, tempPos.k );
    		
    		if ( iTempBlockID == blockID )
    		{
	    		int tempDistI = Math.abs( sourceI - tempPos.i );
	    		int tempDistJ = Math.abs( sourceJ - tempPos.j );
	    		int tempDistK = Math.abs( sourceK - tempPos.k );
	    		
	    		if ( tempDistI <= 2 && tempDistJ <= 2 && tempDistK <= 2  )
	    		{
	    			PropogateCovertToEntity( world, tempPos.i, tempPos.j, tempPos.k, 
    					associatedAnchorEntity, sourceI, sourceJ, sourceK );	    		
    			}
    		}
    	}
    }
    
	//----------- Client Side Functionality -----------//
}
