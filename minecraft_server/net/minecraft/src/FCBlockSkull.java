// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockSkull extends BlockSkull
{
    protected FCBlockSkull( int iBlockID )
    {
        super( iBlockID );
        
        SetAxesEffectiveOn( true );
        
        setHardness( 1F );
        
        InitBlockBounds( 0.25D, 0D, 0.25D, 0.75D, 0.5D, 0.75D );
        
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "skull" );
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
        int iFacing = blockAccess.getBlockMetadata(i, j, k) & 7;

        switch ( iFacing )
        {
            case 2:
            	
            	return AxisAlignedBB.getAABBPool().getAABB(         	
            		0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 1.0F );

            case 3:
            	
            	return AxisAlignedBB.getAABBPool().getAABB(         	
            		0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.5F);

            case 4:
            	
            	return AxisAlignedBB.getAABBPool().getAABB(         	
            		0.5F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);

            case 5:
            	
            	return AxisAlignedBB.getAABBPool().getAABB(         	
            		0.0F, 0.25F, 0.25F, 0.5F, 0.75F, 0.75F);
                
            default:
            	
            	return AxisAlignedBB.getAABBPool().getAABB(         	
            		0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
        }
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
    	return GetBlockBoundsFromPoolBasedOnState( world, i, j, k ).offset( i, j, k );
    }
    
    @Override
    public void makeWither( World world, int i, int j, int k, TileEntitySkull tileEntity )
    {
    	// wither is summoned through soul urns now, override to prevent vanilla method
    }
    
    public boolean IsBlockSummoningBase( World world, int i, int j, int k )
    {
    	if ( world.getBlockId( i, j, k ) == FCBetterThanWolves.fcAestheticOpaque.blockID )
    	{
    		int iSubtype = world.getBlockMetadata( i, j, k );
    		
    		if ( iSubtype == FCBlockAestheticOpaque.m_iSubtypeBone )
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    @Override
	public boolean IsBlockRestingOnThatBelow( IBlockAccess blockAccess, int i, int j, int k )
	{
        int iMetadata = blockAccess.getBlockMetadata( i, j, k );
        
        return iMetadata == 1;
	}
	
    @Override
	public boolean CanRotateOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return true;
	}
	
    @Override
	public boolean RotateAroundJAxis( World world, int i, int j, int k, boolean bReverse )
	{
        TileEntity tileEnt = world.getBlockTileEntity( i, j, k );
    	
        if ( tileEnt != null && tileEnt instanceof TileEntitySkull)
        {        	
        	TileEntitySkull skullEnt = (TileEntitySkull)tileEnt;
        	
        	int iSkullFacing = skullEnt.GetSkullRotationServerSafe();
        	
        	if ( bReverse )
        	{
        		iSkullFacing += 4;
        		
        		if ( iSkullFacing > 15 )
        		{
        			iSkullFacing -= 16;
        		}
        	}
        	else
        	{
        		iSkullFacing -= 4;
        		
        		if ( iSkullFacing < 0 )
        		{
        			iSkullFacing += 16;
        		}
        	}
        	
        	skullEnt.setSkullRotation( iSkullFacing );
        	
	        world.markBlockForUpdate( i, j, k );
	        
	        return true;
        }
        
        return false;
	}
	
    @Override
    public ItemStack GetStackRetrievedByBlockDispenser( World world, int i, int j, int k )
    {
		// don't allow skulls to be retrieved because their code is a mess
		
    	return null;
    }
    
	//----------- Client Side Functionality -----------//
}
