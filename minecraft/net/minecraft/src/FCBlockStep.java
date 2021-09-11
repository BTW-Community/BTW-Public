// FCMOD

package net.minecraft.src;

public class FCBlockStep extends BlockStep
{
	private static final int m_iStepTypeStone = 0;
	private static final int m_iStepTypeSandstone = 1;
	private static final int m_iStepTypeWood = 2;
	private static final int m_iStepTypeCobble = 3;
	private static final int m_iStepTypeBrick = 4;	
	private static final int m_iStepTypeStoneBrick = 5;
	private static final int m_iStepTypeNetherBrick = 6;
	private static final int m_iStepTypeNetherQuartz = 7;
	
    public FCBlockStep( int iBlockID, boolean bIsDoubleSlab )
    {
        super( iBlockID, bIsDoubleSlab );
    }
	
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iType = GetBlockType( blockAccess, i, j, k ); 
    		
		if ( iType == m_iStepTypeSandstone )
		{
			return 3; // diamond or better
		}
		else if ( iType == m_iStepTypeCobble || iType == m_iStepTypeBrick ||
			iType == m_iStepTypeStoneBrick || iType == m_iStepTypeNetherBrick )
		{
    		return 1000; // always convert, never harvest
		}
		
		return super.GetHarvestToolLevel( blockAccess, i, j, k );
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		int iType = GetBlockType( iMetadata );
		
		if ( iType == m_iStepTypeSandstone )
		{
			if ( isDoubleSlab )
			{
				DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemPileSand.itemID, 16, 0, fChanceOfDrop );
			}
			else
			{
				DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemPileSand.itemID, 8, 0, fChanceOfDrop );
			}
			
			return true;
		}
		else if ( iType == m_iStepTypeCobble )
		{
			if ( isDoubleSlab )
			{
				DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcBlockCobblestoneLoose.blockID, 1, 0, fChanceOfDrop );
			}
			else
			{
				DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcBlockCobblestoneLooseSlab.blockID, 1, 0, fChanceOfDrop );
			}
			
			return true;
		}
		else if ( iType == m_iStepTypeBrick )
		{
			if ( isDoubleSlab )
			{
				DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcBlockBrickLoose.blockID, 1, 0, fChanceOfDrop );
			}
			else
			{
				DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcBlockBrickLooseSlab.blockID, 1, 0, fChanceOfDrop );
			}
			
			return true;
		}		
		else if ( iType == m_iStepTypeStoneBrick )
		{
			if ( isDoubleSlab )
			{
				DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcBlockStoneBrickLoose.blockID, 1, 0, fChanceOfDrop );
			}
			else
			{
				DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcBlockStoneBrickLooseSlab.blockID, 1, 0, fChanceOfDrop );
			}
			
			return true;
		}
		else if ( iType == m_iStepTypeNetherBrick )
		{
			if ( isDoubleSlab )
			{
				DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcBlockNetherBrickLoose.blockID, 1, 0, fChanceOfDrop );
			}
			else
			{
				DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcBlockNetherBrickLooseSlab.blockID, 1, 0, fChanceOfDrop );
			}
			
			return true;
		}
		
		return false;
	}
	
    @Override
	public boolean HasContactPointToFullFace( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
	{
    	if ( !isDoubleSlab  && iFacing < 2 )
    	{
        	boolean bIsUpsideDown = GetIsUpsideDown( blockAccess, i, j, k );
        	
        	return bIsUpsideDown == ( iFacing == 1 );
    	}    	
    		
		return true;
	}
	
    @Override
	public boolean HasContactPointToSlabSideFace( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIsSlabUpsideDown )
	{
		return isDoubleSlab || bIsSlabUpsideDown == GetIsUpsideDown( blockAccess, i, j, k );
	}
	
	@Override
	public boolean HasMortar( IBlockAccess blockAccess, int i, int j, int k )
	{
		int iType = GetBlockType( blockAccess, i, j, k );
		
		return iType == m_iStepTypeCobble || iType == m_iStepTypeBrick ||
			iType == m_iStepTypeStoneBrick || iType == m_iStepTypeNetherBrick;
	}
	
    @Override
    public boolean CanMobsSpawnOn( World world, int i, int j, int k )
    {
    	int iType = GetBlockType( world, i, j, k ); 
		
		if ( iType == m_iStepTypeWood )
		{
			return false;
		}
		else if ( iType == m_iStepTypeNetherBrick )
		{
			return true;
		}
		
		return super.CanMobsSpawnOn( world, i, j, k );
    }

	//------------- Class Specific Methods ------------//    

	public int GetBlockType( int iMetadata )
	{
		return iMetadata & 7;
	}
	
	public int GetBlockType( IBlockAccess blockAccess, int i, int j, int k )
	{
		return GetBlockType( blockAccess.getBlockMetadata( i, j, k ) );
	}
	
	//----------- Client Side Functionality -----------//
}