// FCMOD

package net.minecraft.src;

public class FCBlockSign extends BlockSign
{
	protected final boolean m_bFreeStanding;
	
    protected FCBlockSign( int iBlockID, boolean bFreeStanding )
    {
    	super( iBlockID, TileEntitySign.class, bFreeStanding );
    	
    	m_bFreeStanding = bFreeStanding;
    	
    	setHardness( 1F );
    	
    	SetBuoyant();
    	
        InitBlockBounds( 0.25F, 0.0F, 0.25, 0.75F, 1F, 0.75F );
        
        setStepSound( soundWoodFootstep );
        
        setUnlocalizedName( "sign" );
        
        disableStats();
    }
    
    @Override
    public boolean DoesBlockHopperEject( World world, int i, int j, int k )
    {
    	return false;
    }
    
    @Override
	public boolean GetPreventsFluidFlow( World world, int i, int j, int k, Block fluidBlock )
	{
    	return true;
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
        if ( !m_bFreeStanding )
        {
            int var5 = blockAccess.getBlockMetadata( i, j, k );
            
            float var6 = 0.28125F;
            float var7 = 0.78125F;
            float var8 = 0.0F;
            float var9 = 1.0F;
            float var10 = 0.125F;
            
            if ( var5 == 2 )
            {
            	return AxisAlignedBB.getAABBPool().getAABB(         	
            		var8, var6, 1.0F - var10, var9, var7, 1.0F );
            }
            else if ( var5 == 3 )
            {
            	return AxisAlignedBB.getAABBPool().getAABB(         	
            		var8, var6, 0.0F, var9, var7, var10 );
            }
            else if ( var5 == 4 )
            {
            	return AxisAlignedBB.getAABBPool().getAABB(         	
            		1.0F - var10, var6, var8, 1.0F, var7, var9 );
            }
            else if ( var5 == 5 )
            {
            	return AxisAlignedBB.getAABBPool().getAABB(         	
            		0.0F, var6, var8, var10, var7, var9 );
            }
            
        	return AxisAlignedBB.getAABBPool().getAABB(         	
        		0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F );
        }
        
        return super.GetBlockBoundsFromPoolBasedOnState( blockAccess, i, j, k );
    }
    
	//----------- Client Side Functionality -----------//
}
