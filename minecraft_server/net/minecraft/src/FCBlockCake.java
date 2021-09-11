// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockCake extends BlockCake
{
	static final float m_fBorderWidth = 0.0625F;
	static final float m_fHeight = 0.5F;
	
    protected FCBlockCake( int iBlockID )
    {
    	super( iBlockID );
    	
    	SetBuoyant();
    }
    
	@Override
    public void setBlockBoundsBasedOnState( IBlockAccess blockAccess, int i, int j, int k )
    {
    	// override to deprecate parent
    }
    
	@Override
    public void setBlockBoundsForItemRender()
    {
    	// override to deprecate parent
    }
    
    @Override
    public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState( 
    	IBlockAccess blockAccess, int i, int j, int k )
    {
        int iEatState = GetEatState( blockAccess, i, j, k );
        
        float fWidth = (float)( 1 + iEatState * 2 ) / 16.0F;
        
        return AxisAlignedBB.getAABBPool().getAABB(         
        	fWidth, 0.0F, m_fBorderWidth, 
        	1.0F - m_fBorderWidth, m_fHeight, 1.0F - m_fBorderWidth );
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
    	return GetBlockBoundsFromPoolBasedOnState( world, i, j, k ).offset( i, j, k );
    }
    
    @Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick )
    {
        EatCakeSliceLocal( world, i, j, k, player );
        
        return true;
    }
    
    @Override
    public void onBlockClicked( World world, int i, int j, int k, EntityPlayer player ) 
    {    	
    	// override left-click behavior to match other vanilla blocks
    }
    
    @Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeigborBlockID )
    {
        if ( !canBlockStay( world, i, j, k ) )
        {
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
            world.setBlockWithNotify( i, j, k, 0 );
        }
        else
        {
        	boolean bOn = IsRedstoneOn( world, i, j, k );
        	boolean bReceivingRedstone = world.isBlockGettingPowered( i, j, k ); 
        	
        	if ( bOn != bReceivingRedstone )
        	{
        		SetRedstoneOn( world, i, j, k, bReceivingRedstone );
        		
        		if ( bReceivingRedstone )
        		{
	                world.playAuxSFX( FCBetterThanWolves.m_iGhastScreamSoundAuxFXID, i, j, k, 0 );            
        		}
        	}
        }
    }
    
    @Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        super.onBlockAdded( world, i, j, k );
        
    	boolean bReceivingRedstone = world.isBlockGettingPowered( i, j, k ); 
    	
		if ( bReceivingRedstone )
		{
			SetRedstoneOn( world, i, j, k, true );
			
            world.playAuxSFX( FCBetterThanWolves.m_iGhastScreamSoundAuxFXID, i, j, k, 0 );            
		}
    }
    
    @Override
    public ItemStack GetStackRetrievedByBlockDispenser( World world, int i, int j, int k )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	
		if ( ( iMetadata & (~8) ) == 0 ) // strips out power state
		{
			// only allow the cake to be swallowed if none of it has been eaten
			
			return new ItemStack( Item.cake.itemID, 1, 0 );			
		}
    	
    	return null;
    }
    
    //------------- Class Specific Methods ------------//
    
    private void EatCakeSliceLocal( World world, int i, int j, int k, EntityPlayer player )
    {
    	// this function is necessary due to eatCakeSlice() in parent being private
    	
        if ( player.canEat( true ) )
        {
        	// food value adjusted for increased hunger meter resolution
        	
            player.getFoodStats().addStats( 4, 4F );
            
            int iEatState = GetEatState( world, i, j, k ) + 1; 

            if ( iEatState >= 6 )
            {
                world.setBlockWithNotify( i, j, k, 0 );
            }
            else
            {
                SetEatState( world, i, j, k, iEatState );                
            }
        }
    	else
    	{
    		player.OnCantConsume();
    	}
    }
    
    public boolean IsRedstoneOn( IBlockAccess iBlockAccess, int i, int j, int k )
    {
    	return ( iBlockAccess.getBlockMetadata( i, j, k ) & 8 ) > 0;
    }
    
    public void SetRedstoneOn( World world, int i, int j, int k, boolean bOn )
    {
    	int iMetaData = world.getBlockMetadata( i, j, k ) & (~8); // filter out any old on state
    	
    	if ( bOn )
    	{
    		iMetaData |= 8;
    	}
    	
        world.setBlockMetadataWithNotify( i, j, k, iMetaData );
    }
    
    public int GetEatState( IBlockAccess iBlockAccess, int i, int j, int k )
    {
    	return ( iBlockAccess.getBlockMetadata( i, j, k ) & 7 );
    }
    
    public void SetEatState( World world, int i, int j, int k, int state )
    {
    	int iMetaData = world.getBlockMetadata( i, j, k ) & 8; // filter out any old on state
    	
		iMetaData |= state;
		
        world.setBlockMetadataWithNotify( i, j, k, iMetaData );
    }
    
	//----------- Client Side Functionality -----------//
}
