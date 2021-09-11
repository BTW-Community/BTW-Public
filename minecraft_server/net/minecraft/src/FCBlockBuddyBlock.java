// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockBuddyBlock extends Block
{
    private final static int m_iTickRate = 5;
    
    public FCBlockBuddyBlock( int iBlockID )
    {
        super( iBlockID, Material.rock );  
        
        setHardness( 3.5F );
        
        setTickRandomly( true );        
		
        setStepSound(soundStoneFootstep);
        
        setUnlocalizedName( "fcBlockBuddyBlock" );        
        
		setCreativeTab( CreativeTabs.tabRedstone );
    }
    
	@Override
    public int tickRate( World world )
    {
        return m_iTickRate;
    }    

	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        super.onBlockAdded( world, i, j, k );

        // minimal delay same as when the buddy block is changed by a neigbor notification to handle
        // state changes due to being pushed around by a piston
    	world.scheduleBlockUpdate( i, j, k, blockID, 1 ); 
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
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
    	if ( !IsRedstoneOn( world, i, j, k ) )
    	{
    		Block neighborBlock = blocksList[iNeighborBlockID];
        	
	    	if ( neighborBlock != null && neighborBlock.TriggersBuddy() &&
    			!world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )
	    	{
	    		// minimal delay when triggered to avoid notfying neighbors of change in same tick
	    		// that they are notifying of the original change. Not doing so causes problems 
	    		// with some blocks (like ladders) that haven't finished initializing their state 
	    		// on placement when they send out the notification
	    		
	        	world.scheduleBlockUpdate( i, j, k, blockID, 1 ); 
	    	}
    	}
    }
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
    	if ( IsRedstoneOn( world, i, j, k ) )
    	{
    		SetBlockRedstoneOn( world, i, j, k, false );
    	}
    	else
    	{
    		SetBlockRedstoneOn( world, i, j, k, true );

    		// schedule another update to turn the block off
        	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) ); 
    	}
    }
    
	@Override
    public void RandomUpdateTick( World world, int i, int j, int k, Random rand )
    {
		if ( IsRedstoneOn( world, i, j, k ) )
		{
			// verify we have a tick already scheduled to prevent jams on chunk load
			
			if ( !world.IsUpdateScheduledForBlock( i, j, k, blockID ) )
			{
		        world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );        
			}
		}
    }
	
	@Override
    public int isProvidingWeakPower(IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
		return GetPowerProvided( blockAccess, i, j, k, iSide );
    }
    
	@Override
    public int isProvidingStrongPower( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
		return GetPowerProvided( blockAccess, i, j, k, iSide );
    }
    
	@Override
    public boolean canProvidePower()
    {
        return true;
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
	public boolean ToggleFacing( World world, int i, int j, int k, boolean bReverse )
	{		
		int iFacing = GetFacing( world, i, j, k );
		
		iFacing = Block.CycleFacing( iFacing, bReverse );

		SetFacing( world, i, j, k, iFacing );
		
        world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );
        
        return true;
	}
	
	@Override
	public boolean TriggersBuddy()
	{
		return false;
	}
	
	@Override
    public int OnPreBlockPlacedByPiston( World world, int i, int j, int k, int iMetadata, int iDirectionMoved )
	{
		// notify the neigbours of the block we originated at
		
        FCUtilsBlockPos originPos = new FCUtilsBlockPos( i, j, k, GetOppositeFacing( iDirectionMoved ) );
        
        NotifyNeigborsToFacingOfPowerChange( world, originPos.i, originPos.j, originPos.k, GetFacing( iMetadata ) );
		
		return iMetadata;
    }
	
    //------------- Class Specific Methods ------------//
    
    public int GetPowerProvided( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
    	int iFacing = GetFacing( blockAccess, i, j, k );
    	
    	if ( Block.GetOppositeFacing( iSide ) == iFacing && IsRedstoneOn( blockAccess, i, j, k ) )
    	{    		
    		return 15;
    	}
    	
		return 0;
    }
	
    public boolean IsRedstoneOn( IBlockAccess iblockaccess, int i, int j, int k )
    {
    	return ( iblockaccess.getBlockMetadata(i, j, k) & 1 ) > 0;
    }
    
    public void SetBlockRedstoneOn( World world, int i, int j, int k, boolean bOn )
    {
    	if ( bOn != IsRedstoneOn( world, i, j, k ) )
    	{
	    	int iMetaData = world.getBlockMetadata(i, j, k);
	    	
	    	if ( bOn )
	    	{
	    		iMetaData = iMetaData | 1;
	    		
		        world.playAuxSFX( FCBetterThanWolves.m_iRedstonePowerClickSoundAuxFXID, i, j, k, 0 );							        
	    	}
	    	else
	    	{
	    		iMetaData = iMetaData & (~1);
	    	}
	    	
	        world.setBlockMetadataWithClient( i, j, k, iMetaData );
	        
	        // only notify on the output side to prevent weird shit like doors auto-closing when the block
	        // goes off
	        
	        int iFacing = this.GetFacing( world, i, j, k );
	        
	        NotifyNeigborsToFacingOfPowerChange( world, i, j, k, iFacing );
	        
	        // the following forces a re-render (for texture change)
	        
	        world.markBlockRangeForRenderUpdate( i, j, k, i, j, k );    	
        }
    }
    
    public void NotifyNeigborsToFacingOfPowerChange( World world, int i, int j, int k, int iFacing )
    {
        FCUtilsBlockPos outputPos = new FCUtilsBlockPos( i, j, k );
        
        outputPos.AddFacingAsOffset( iFacing );
        
        Block outputBlock = Block.blocksList[world.getBlockId( outputPos.i, outputPos.j, outputPos.k)];
        
        if ( outputBlock != null)
        {
            outputBlock.onNeighborBlockChange( world, outputPos.i, outputPos.j, outputPos.k, blockID );
        }
        
        // we have to notify in a radius as some redstone blocks get their power state from up to two blocks away
        
        world.notifyBlocksOfNeighborChange( outputPos.i, outputPos.j, outputPos.k, blockID );
        
    }
    
	//----------- Client Side Functionality -----------//
}