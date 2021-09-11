// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockTallGrass extends BlockTallGrass
{
    private static final double m_dHalfWidth = 0.4F;
    
    protected FCBlockTallGrass( int iBlockID )
    {
    	super( iBlockID );
    	
    	setHardness( 0F );
    	
    	SetBuoyant();
    	
		SetFireProperties( FCEnumFlammability.GRASS );
		
        InitBlockBounds( 0.5D - m_dHalfWidth, 0D, 0.5D - m_dHalfWidth, 
        	0.5D + m_dHalfWidth, 0.8D, 0.5D + m_dHalfWidth);
        
		setStepSound( soundGrassFootstep );
		
		setUnlocalizedName( "tallgrass" );
    }
    
    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return -1;
    }
    
    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
    	int iBlockAboveMaxNaturalLight = world.GetBlockNaturalLightValueMaximum( i, j + 1, k );
    	int iBlockAboveCurrentNaturalLight = iBlockAboveMaxNaturalLight - world.skylightSubtracted;
    	
        if ( iBlockAboveCurrentNaturalLight >= FCBlockGrass.m_iGrassSpreadFromLightLevel && world.provider.dimensionId != 1 )
        {
        	int iMetadata = world.getBlockMetadata( i, j, k );
        	
        	if ( iMetadata == 1 ) // actual tall grass as opposed to ferns, dead shrubs, etc
        	{
            	// check for grass spread onto tilled earth
            	
            	if ( rand.nextInt( 3 ) == 0 )
            	{
                    int iTargetI = i + rand.nextInt(3) - 1;
                    int iTargetJ = j - 1 + rand.nextInt(5) - 3;
                    int iTargetK = k + rand.nextInt(3) - 1;
                    
                    int iTargetBlockID = world.getBlockId( iTargetI, iTargetJ + 1, iTargetK );

                    // FCNOTE: This is legacy support, so don't worry about new tilled block types
                    if ( world.getBlockId( iTargetI, iTargetJ, iTargetK ) == 
                    	Block.tilledField.blockID ) 
                    {
                    	if ( world.isAirBlock( iTargetI, iTargetJ + 1, iTargetK ) )
                    	{
                        	int iTargetBlockMaxNaturalLight = world.GetBlockNaturalLightValueMaximum( iTargetI, iTargetJ + 1, iTargetK );
                        	
                        	if ( iTargetBlockMaxNaturalLight >= FCBlockGrass.m_iGrassSpreadToLightLevel )
                        	{                        	
                        		world.setBlockAndMetadataWithNotify( iTargetI, iTargetJ + 1, iTargetK, Block.tallGrass.blockID, 1 );
                        	}
                    	}
                	}
                }
        	}
        }
        
        super.updateTick( world, i, j, k, rand );
    }
    
    @Override
    public boolean CanSpitWebReplaceBlock( World world, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public boolean IsReplaceableVegetation( World world, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public ItemStack GetStackRetrievedByBlockDispenser( World world, int i, int j, int k )
    {
    	return createStackedBlock( world.getBlockMetadata( i, j, k ) );
    }
	
    @Override
    public boolean CanBeGrazedOn( IBlockAccess blockAccess, int i, int j, int k, 
    	EntityAnimal animal )
    {
		return blockAccess.getBlockMetadata( i, j, k ) == 1 || animal.CanGrazeOnRoughVegetation();
    }
    
    @Override
    public int GetHerbivoreItemFoodValue( int iItemDamage )
    {
    	if ( iItemDamage == 1 )
    	{
    		return EntityAnimal.m_iBaseGrazeFoodValue;
    	}
    	
    	return super.GetHerbivoreItemFoodValue( iItemDamage );
    }
    
	//------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}