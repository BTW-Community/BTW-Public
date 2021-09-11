// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockDirtSlab extends FCBlockSlabAttached
{
	public static final int m_iSubtypeDirt = 0;
	public static final int m_iSubtypeGrass = 1;
	public static final int m_iSubtypeMycelium = 2; // Not used.  Implemented within its own block
	public static final int m_iSubtypePackedEarth = 3;
	
    public final static int m_iNumSubtypes = 4;
    
    public FCBlockDirtSlab( int iBlockID )
    {
        super( iBlockID, Material.ground );

        setHardness( 0.5F );
        SetShovelsEffectiveOn( true );
        
        setStepSound( soundGrassFootstep );
        
        setUnlocalizedName( "fcBlockSlabDirt" );
        
        setTickRandomly( true );        
        
        setCreativeTab( CreativeTabs.tabBlock );        
    }

    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
        int iSubType = GetSubtype( world, i, j, k );
        
        if ( iSubType == m_iSubtypeGrass )
        {
        	int iBlockAboveMaxNaturalLight = world.GetBlockNaturalLightValueMaximum( i, j + 1, k );
        	int iBlockAboveCurrentNaturalLight = iBlockAboveMaxNaturalLight - world.skylightSubtracted;
        	
            boolean bIsUpsideDown = GetIsUpsideDown( world, i, j, k );
            int iBlockAboveID = world.getBlockId( i, j + 1, k );
            Block blockAbove = Block.blocksList[iBlockAboveID];            
            
	        if ( ( blockAbove != null && !blockAbove.GetCanGrassGrowUnderBlock( world, i, j + 1, k, !bIsUpsideDown ) ) || 
	        	iBlockAboveMaxNaturalLight < FCBlockGrass.m_iGrassSurviveMinimumLightLevel || Block.lightOpacity[world.getBlockId( i, j + 1, k )] > 2 )
	        {
	        	SetSubtype( world, i, j, k, m_iSubtypeDirt );
	        }
	        else if ( iBlockAboveCurrentNaturalLight >= FCBlockGrass.m_iGrassSpreadFromLightLevel )
	        {
	        	FCBlockGrass.CheckForGrassSpreadFromLocation( world, i, j, k );
	        }
        }
    }
    
	@Override
	public int damageDropped( int iMetadata )
    {
		int iSubtype = GetSubtype( iMetadata );
		            		
		if ( iSubtype == m_iSubtypePackedEarth )
		{
			return iSubtype;
		}
		
		return 0;
    }
	
    @Override
    public int idDropped( int iMetadata, Random random, int iFortuneModifier )
    {
		int iSubtype = GetSubtype( iMetadata );
		
		if ( iSubtype == m_iSubtypePackedEarth )
		{
			return super.idDropped( iMetadata, random, iFortuneModifier );
		}
		
        return FCBetterThanWolves.fcBlockDirtLooseSlab.blockID;
    }
    
    @Override
    public float GetMovementModifier( World world, int i, int j, int k )
    {
    	float fModifier = 1.0F;
        	
    	int iSubtype = GetSubtype( world, i, j, k );
    	
		if ( iSubtype == m_iSubtypePackedEarth )
		{
			fModifier = 1.2F;
		}
    	
    	return fModifier;
    }

    @Override
    public StepSound GetStepSound( World world, int i, int j, int k )
    {
    	int iSubtype = GetSubtype( world, i, j, k );
    	
    	if ( iSubtype == m_iSubtypeDirt || iSubtype == m_iSubtypePackedEarth )
    	{
    		return soundGravelFootstep;
    	}
    	
    	return stepSound;
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		int iNumDropped = 3;
		
    	if ( GetSubtype( iMetadata ) == m_iSubtypePackedEarth )
    	{
    		iNumDropped = 6;
    	}
    	
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemPileDirt.itemID, 
			iNumDropped, 0, fChanceOfDrop );
		
		return true;
	}
	
	@Override
    public boolean GetCanGrassSpreadToBlock( World world, int i, int j, int k )
    {
        int iSubType = GetSubtype( world, i, j, k );
        
        if ( iSubType == m_iSubtypeDirt )
        {        
            Block blockAbove = Block.blocksList[world.getBlockId( i, j + 1, k )];
            boolean bIsUpsideDown = GetIsUpsideDown( world, i, j, k );
            
	        if ( blockAbove == null || blockAbove.GetCanGrassGrowUnderBlock( 
	        	world, i, j + 1, k, !bIsUpsideDown ) ) 
            {            
            	return true;
            }
        }
    	
    	return false;
    }
    
	@Override
    public boolean SpreadGrassToBlock( World world, int i, int j, int k )
    {
		SetSubtype( world, i, j, k, m_iSubtypeGrass );
        
    	return true;
    }
    
	@Override
    public boolean GetCanMyceliumSpreadToBlock( World world, int i, int j, int k )
    {
        int iSubType = GetSubtype( world, i, j, k );
        
        if ( iSubType == m_iSubtypeDirt )
        {        
            return !GetIsUpsideDown( world, i, j, k ) ||
            	!FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j + 1, k, 0 );
        }
    	
    	return false;
    }
    
	@Override
    public boolean SpreadMyceliumToBlock( World world, int i, int j, int k )
    {
    	int iNewMetadata = ((FCBlockMyceliumSlab)FCBetterThanWolves.fcBlockMyceliumSlab).SetIsUpsideDown( 0, GetIsUpsideDown( world, i, j, k ) );
    	
        world.setBlockAndMetadataWithNotify( i, j, k, FCBetterThanWolves.fcBlockMyceliumSlab.blockID, iNewMetadata );
        
    	return true;
    }
    
	@Override
    public boolean AttemptToCombineWithFallingEntity( World world, int i, int j, int k, EntityFallingSand entity )
	{
		if ( entity.blockID == FCBetterThanWolves.fcBlockDirtLooseSlab.blockID )
		{
			int iMetadata = world.getBlockMetadata( i, j, k );
			
			if ( GetSubtype( iMetadata ) != m_iSubtypePackedEarth && !GetIsUpsideDown( iMetadata ) )
			{			
				world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLoose.blockID );					
						
				return true;
			}
		}
		
		return super.AttemptToCombineWithFallingEntity( world, i, j, k, entity );
	}

	@Override
	protected void OnAnchorBlockLost( World world, int i, int j, int k )
	{
		if (this.GetSubtype(world.getBlockId(i, j, k)) != m_iSubtypePackedEarth) {
			world.setBlock(i, j, k, FCBetterThanWolves.fcBlockDirtLooseSlab.blockID, world.getBlockMetadata(i, j, k) & 8, 2);
		}
		else {
			DropComponentItemsOnBadBreak( world, i, j, k, world.getBlockMetadata( i, j, k ), 1F );
		}
	}
	
	@Override
	public int GetCombinedBlockID( int iMetadata )
	{
		int iSubtype = GetSubtype( iMetadata );
		
		if ( iSubtype == m_iSubtypePackedEarth )
		{
			return FCBetterThanWolves.fcBlockAestheticOpaqueEarth.blockID;
		}
		
		return Block.dirt.blockID;
	}
	
	@Override
	public int GetCombinedMetadata( int iMetadata )
	{
		int iSubtype = GetSubtype( iMetadata );
		
		if ( iSubtype == m_iSubtypePackedEarth )
		{
			return FCBlockAestheticOpaqueEarth.m_iSubtypePackedEarth;
		}
		
		return 0;
	}
	
    @Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
    
	@Override
    protected boolean canSilkHarvest()
    {
        return true;
    }

    @Override
    protected ItemStack createStackedBlock( int iMetadata )
    {
        return new ItemStack( blockID, 1, GetSubtype( iMetadata ) );
    }
    
    @Override
    public boolean CanBeGrazedOn( IBlockAccess blockAccess, int i, int j, int k, 
    	EntityAnimal byAnimal )
    {
        return GetSubtype( blockAccess, i, j, k ) == m_iSubtypeGrass;
    }
    
    @Override
    public void OnGrazed( World world, int i, int j, int k, EntityAnimal animal )
    {
        if ( !animal.GetDisruptsEarthOnGraze() )
        {
        	SetSubtype( world, i, j, k, m_iSubtypeDirt );
        }
        else
        {
        	world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLooseSlab.blockID );
        	
        	NotifyNeighborsBlockDisrupted( world, i, j, k );
        }
    }
    
    @Override
	public void OnVegetationAboveGrazed( World world, int i, int j, int k, EntityAnimal animal )
	{
        if ( animal.GetDisruptsEarthOnGraze() )
        {
        	world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockDirtLooseSlab.blockID );
        	
        	NotifyNeighborsBlockDisrupted( world, i, j, k );
        }
	}
    
    @Override
    public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
    	super.OnBlockDestroyedWithImproperTool( world, player, i, j, k, iMetadata );
    	
		if ( GetSubtype( iMetadata ) != m_iSubtypePackedEarth )
		{
			OnDirtSlabDugWithImproperTool( world, i, j, k, GetIsUpsideDown( iMetadata ) );
		}
    }
    
	@Override
    public void onBlockDestroyedByExplosion( World world, int i, int j, int k, Explosion explosion )
    {
		super.onBlockDestroyedByExplosion( world, i, j, k, explosion );
    	
		if ( GetSubtype( world, i, j, k ) != m_iSubtypePackedEarth )
		{
			OnDirtSlabDugWithImproperTool( world, i, j, k, GetIsUpsideDown( world, i, j, k ) );
		}
    }
	
	@Override
    protected void OnNeighborDirtDugWithImproperTool( World world, int i, int j, int k, 
    	int iToFacing )
    {
		int iSubtype = GetSubtype( world, i, j, k );
		
		if ( iSubtype != m_iSubtypePackedEarth )
		{
			// only disrupt grass/mycelium when block below is dug out
    	
			if ( iSubtype != m_iSubtypeGrass || iToFacing == 0 )
			{			
		        boolean bIsUpsideDown = GetIsUpsideDown( world, i, j, k );
	
		        // only disrupt the block if it's touching the dug neighbor
		        
		        if ( !( bIsUpsideDown && iToFacing == 0 ) && !( !bIsUpsideDown && iToFacing == 1 ) )
		        {
		        	world.setBlockWithNotify( i, j, k, 
		        		FCBetterThanWolves.fcBlockDirtLooseSlab.blockID );
	        	}
			}
		}
    }
    
    //------------- Class Specific Methods ------------//    
    
    public int GetSubtype( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetSubtype( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    public int GetSubtype( int iMetadata )
    {
    	return ( iMetadata & (~1) ) >> 1;
    }
    
    public void SetSubtype( World world, int i, int j, int k, int iSubtype )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k ) & 1; // clear old value
    	
		iMetadata |= ( iSubtype << 1 );
    	
    	world.setBlockMetadataWithNotify( i, j, k, iMetadata );
    }
}