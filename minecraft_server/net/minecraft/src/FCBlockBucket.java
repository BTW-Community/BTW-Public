// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockBucket extends FCBlockFalling
{
	protected FCModelBlockBucket m_model;
    
	protected FCModelBlock m_modelTransformed;
    	
    public FCBlockBucket( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialMiscellaneous );
        
        setHardness( 0F );
        setResistance( 0F );
        
    	InitBlockBounds( 
    		0.5D - FCModelBlockBucket.m_dBodyHalfWidth, FCModelBlockBucket.m_dBaseHeight, 
    		0.5D - FCModelBlockBucket.m_dBodyHalfWidth, 
    		0.5D + FCModelBlockBucket.m_dBodyHalfWidth, FCModelBlockBucket.m_dHeight, 
    		0.5D + FCModelBlockBucket.m_dBodyHalfWidth );
    	
        setStepSound( soundMetalFootstep );
        
        setUnlocalizedName( "bucket" );
        
        InitModels();
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
    public int onBlockPlaced( World world, int i, int j, int k, int iFacing, 
    	float fClickX, float fClickY, float fClickZ, int iMetadata )
    {
		iMetadata = SetFacing( iMetadata, iFacing );
		
		return iMetadata;
    }
		
	@Override
    public int idDropped( int iMetadata, Random rand, int iFortuneMod )
    {
		return Item.bucketEmpty.itemID;
    }
	
	@Override
    public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChance )
    {
		// always an empty bucket on bad break, regardless of contents
		
		DropItemsIndividualy( world, i, j, k, Item.bucketEmpty.itemID, 1, 0, fChance );		
		
    	return true;
	}
    
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k )
    {
        return null;
    }

    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	m_modelTransformed = m_model.MakeTemporaryCopy();
    	
    	int iFacing = GetFacing( world, i, j, k );
    	
    	m_modelTransformed.TiltToFacingAlongJ( iFacing );
    	
    	FCModelBlockBucket.OffsetModelForFacing( m_modelTransformed, iFacing );
    	
    	return m_modelTransformed.CollisionRayTrace( world, i, j, k, startRay, endRay );    	
    }
    
	@Override
	public int GetFacing( int iMetadata )
	{		
    	return iMetadata & 7;
	}
	
	@Override
	public int SetFacing( int iMetadata, int iFacing )
	{
		iMetadata &= ~7;
		
        return iMetadata | iFacing;
	}

	@Override
    public int OnPreBlockPlacedByPiston( World world, int i, int j, int k, int iMetadata, int iDirectionMoved )
    {
		if ( !FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, i, j - 1, k, 
			1, true ) )
		{
			// handle bucket being pushed over ledge, and tilting in the appropriate direction
			
			if ( iDirectionMoved >= 2 )
			{ 
				int iFacing = GetFacing( iMetadata );
				
				if ( iFacing == 0 )
				{
					iFacing = Block.GetOppositeFacing( iDirectionMoved );
				}
				else if ( iFacing == 1 )
				{				
					iFacing = iDirectionMoved;
				}
				else if ( iFacing == iDirectionMoved )
				{
					iFacing = 0;
				}
				else if ( iFacing == Block.GetOppositeFacing( iDirectionMoved ) )
				{
					iFacing = 1;
				}
				else
				{
					// orientations perpendicular to direction of travel do not change facing
				}
				
				iMetadata = SetFacing( iMetadata, iFacing );
			}
		}
		
    	return iMetadata;
    }
	
	@Override
	public boolean GetPreventsFluidFlow( World world, int i, int j, int k, Block fluidBlock )
	{
        return false;
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
	
	protected void InitModels()
	{
		m_model = new FCModelBlockBucket();
	    
		// must initialize transformed model due to weird vanilla getIcon() calls that 
		// occur outside of regular rendering
		
		m_modelTransformed = m_model; 
	}
	
	//----------- Client Side Functionality -----------//
}
