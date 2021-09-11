// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockLens extends Block
{
	public static final int m_iLensMaxRange = 128;
	
    private final static int iLensTickRate = 1;
    
    private final static float fMinTriggerLightValue = 12; 
    
    public FCBlockLens( int iBlockID )
    {
        super( iBlockID, Material.iron );  
        
        setHardness( 3.5F );
        
        setTickRandomly( true );        
        
        setStepSound( Block.soundStoneFootstep );
        
        setUnlocalizedName( "fcBlockLens" );
        
        setCreativeTab( CreativeTabs.tabRedstone );
    }
    
	@Override
    public int tickRate( World world )
    {
        return iLensTickRate;
    }    
    
	@Override
    public void onBlockAdded( World world, int i, int j, int k ) 
	{
        world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );		
	}
	
	@Override
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
        return SetFacing( iMetadata, Block.GetOppositeFacing( iFacing ) );
    }
    
	@Override
	public void onBlockPlacedBy( World world, int i, int j, int k, EntityLiving entityLiving, ItemStack stack )
	{
		int iFacing = FCUtilsMisc.ConvertPlacingEntityOrientationToBlockFacingReversed( entityLiving );
		
		SetFacing( world, i, j, k, iFacing );
	}
    
	@Override
    public void onPostBlockPlaced( World world, int i, int j, int k, int iMetadata ) 
    {    	
		SetupBeam( world, i, j, k );
    }	
    
	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
    	RemoveBeam( world, i, j, k );
    	
        super.breakBlock( world, i, j, k, iBlockID, iMetadata );
    }
    
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int l )
    {
		if ( !world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )
		{
			world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
		}
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
    	int iFacing = GetFacing( world, i, j, k );
    	
		boolean bIsLightDetector = IsDirectlyFacingBlockDetector( world, i, j, k );
		
		if ( !bIsLightDetector )
		{
	    	boolean bLightOn = CheckForInputLight( world, i, j, k ); 
	    	
	    	if ( IsLit( world, i, j, k ) != bLightOn )
	    	{
	    		SetLit( world, i, j, k, bLightOn );
	    		
	    		if ( bLightOn )
	    		{
	    			TurnBeamOn( world, i, j, k );
	    		}
	    		else
	    		{
	    			TurnBeamOff( world, i, j, k );
	    		}
	    	}
	    	
	    	// check if there is an air-block directly in front, which indicates a block was just removed from there and the
	    	// beam needs to be propagated
	    	
	    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
	    	
	    	targetPos.AddFacingAsOffset( iFacing );
	    	
	    	if ( world.getBlockId( targetPos.i, targetPos.j, targetPos.k ) == 0 )
	    	{
	        	FCBlockDetectorLogic logicBlock = (FCBlockDetectorLogic)(FCBetterThanWolves.fcBlockDetectorLogic);
	        	
	        	logicBlock.PropagateBeamsThroughBlock( world, targetPos.i, targetPos.j, targetPos.k );
	    	}
		}
		else
		{			
	    	FCUtilsBlockPos sourcePos = new FCUtilsBlockPos( i, j, k );
	    	
	    	sourcePos.AddFacingAsOffset( Block.GetOppositeFacing( iFacing ) );
	    	
	    	int iSourceLightValue = world.getBlockLightValue( sourcePos.i, sourcePos.j, sourcePos.k ); 
	    	
	    	boolean bShouldBeOn =  iSourceLightValue >= 8;
	    	
	    	if ( IsLit( world, i, j, k ) != bShouldBeOn )
	    	{
	    		SetLit( world, i, j, k, bShouldBeOn );
	    	}
	    	
			// schedule another update immediately to check for light changes
			
	    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );	    	
		}
    }    
    
	@Override
    public int getMobilityFlag()
    {
    	// set the block as immobile so that piston pushing won't cause regeneration of the entire beam
    	
        return 2;
    }
	
	@Override
	public int GetFacing( int iMetadata )
	{
    	return ( iMetadata & (~1) ) >> 1;
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
    	iMetadata = ( iMetadata & 1 ) | ( iFacing << 1 );
    	
		return iMetadata;
	}
	
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess iBlockAccess, int i, int j, int k )
	{
		return false;
	}
	
	@Override
	public boolean CanTransmitRotationHorizontallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return false;
	}
	
	@Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return false;
	}
	
	@Override
	public boolean ToggleFacing( World world, int i, int j, int k, boolean bReverse )
	{		
		int iFacing = GetFacing( world, i, j, k );
		
		iFacing = Block.CycleFacing( iFacing, bReverse );

		SetFacing( world, i, j, k, iFacing );
		
        world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
        
        return true;
	}
	
    //------------- Class Specific Methods ------------//
    
    public boolean IsLit( IBlockAccess iblockaccess, int i, int j, int k )
    {
    	return ( iblockaccess.getBlockMetadata(i, j, k) & 1 ) > 0;
    }
    
    public void SetLit( World world, int i, int j, int k, boolean bOn )
    {
    	if ( bOn != IsLit( world, i, j, k ) )
    	{
	    	int iMetaData = world.getBlockMetadata(i, j, k);
	    	
	    	if ( bOn )
	    	{
	    		iMetaData = iMetaData | 1;	    		
	    	}
	    	else
	    	{
	    		iMetaData = iMetaData & (~1);
	    	}
	    	
	        world.setBlockMetadataWithNotify( i, j, k, iMetaData );
	        
	        world.notifyBlocksOfNeighborChange( i, j, k, blockID );
	        
	        // the following forces a re-render (for texture change)
	        
	        world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );    	
        }
    }
    
    private boolean CheckForInputLight( World world, int i, int j, int k )
    {
    	int iFacing = GetFacing( world, i, j, k );
    	int iTargetFacing = Block.GetOppositeFacing( iFacing );
    	
    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
    	
    	targetPos.AddFacingAsOffset( iTargetFacing );
    	
    	int iTargetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
    	
    	if ( iTargetBlockID > 0 )
    	{
    		if ( lightValue[iTargetBlockID] > fMinTriggerLightValue )
    		{
    			if ( iTargetBlockID != FCBetterThanWolves.fcBlockDetectorGlowingLogic.blockID )
    			{
    				return true;
    			}
    			else
    			{
    				// only power the lens with another lens if it is facing directly into it.
    				
    				FCBlockDetectorLogic logicBlock = (FCBlockDetectorLogic)(FCBetterThanWolves.fcBlockDetectorGlowingLogic);
    				
    				if ( logicBlock.ShouldBeProjectingToFacing( world, targetPos.i, targetPos.j, targetPos.k, iFacing ) )
    				{
    					return true;
    				}
    			}
    		}
    		else if ( iTargetBlockID == blockID )
    		{
    			// Lenses can feed directly into each other
    			
    			if ( IsLit( world, targetPos.i, targetPos.j, targetPos.k ) )
    			{
    				if ( GetFacing( world, targetPos.i, targetPos.j, targetPos.k ) == iFacing )
    				{
    					return true;
    				}
    			}
    		}
    	}
    	
    	return false;
    }
    
    public void SetupBeam( World world, int i, int j, int k )
    {
    	FCBlockDetectorLogic logicBlock = (FCBlockDetectorLogic)(FCBetterThanWolves.fcBlockDetectorLogic);
    	
    	logicBlock.CreateLensBeamFromBlock( world, i, j, k, GetFacing( world, i, j, k ), m_iLensMaxRange ); 
    }

    private void RemoveBeam( World world, int i, int j, int k )
    {
    	FCBlockDetectorLogic logicBlock = (FCBlockDetectorLogic)(FCBetterThanWolves.fcBlockDetectorLogic);
    	
    	logicBlock.RemoveLensBeamFromBlock( world, i, j, k, GetFacing( world, i, j, k ), m_iLensMaxRange ); 
    }
    
	private void TurnBeamOn( World world, int i, int j, int k )
	{
    	FCBlockDetectorLogic logicBlock = (FCBlockDetectorLogic)(FCBetterThanWolves.fcBlockDetectorLogic);
    	
    	logicBlock.TurnBeamOnFromBlock( world, i, j, k, GetFacing( world, i, j, k ), m_iLensMaxRange ); 
	}

	private void TurnBeamOff( World world, int i, int j, int k )
	{
    	FCBlockDetectorLogic logicBlock = (FCBlockDetectorLogic)(FCBetterThanWolves.fcBlockDetectorLogic);
    	
    	logicBlock.TurnBeamOffFromBlock( world, i, j, k, GetFacing( world, i, j, k ), m_iLensMaxRange ); 
	}
	
	private boolean IsDirectlyFacingBlockDetector( World world, int i, int j, int k )
	{
		int iFacing = GetFacing( world, i, j, k );
		
    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
    	
    	targetPos.AddFacingAsOffset( iFacing );
    	
    	int iTargetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
    	
		if ( iTargetBlockID == FCBetterThanWolves.fcBlockDetector.blockID )
		{
			int iDetectorFacing = ((FCBlockDetectorBlock)(FCBetterThanWolves.fcBlockDetector)).GetFacing( world, targetPos.i, targetPos.j, targetPos.k );
			
			if ( iDetectorFacing == Block.GetOppositeFacing( iFacing ) )
			{
				return true;
			}
		}
		
		return false;
	}
    
	//----------- Client Side Functionality -----------//
}
