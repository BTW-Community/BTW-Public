// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockWoodSlab extends BlockHalfSlab
{
    public static final String[] m_sWoodType = new String[] { "oak", "spruce", "birch", "jungle", "blood" };

    public FCBlockWoodSlab( int iBlockID, boolean bDoubleSlab )
    {
        super( iBlockID, bDoubleSlab, FCBetterThanWolves.fcMaterialPlanks );
        
        setHardness( 1F );
        setResistance( 5F );
        SetAxesEffectiveOn();
        
        SetBuoyant();
        
		SetFireProperties( FCEnumFlammability.PLANKS );
		
        setStepSound( soundWoodFootstep );
        
        setUnlocalizedName( "woodSlab" );
        
        setCreativeTab( CreativeTabs.tabBlock );
    }

    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return Block.woodSingleSlab.blockID;
    }
    
    @Override
    public String getFullSlabName( int iMetadata )
    {
        return super.getUnlocalizedName() + "." + m_sWoodType[iMetadata];
    }    
    
    @Override
    protected ItemStack createStackedBlock( int iMetadata )
    {
        return new ItemStack( Block.woodSingleSlab.blockID, 2, iMetadata & 7 );
    }

    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 2; // iron or better
    }
    
    @Override
    public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
    	int iNumItems = isDoubleSlab ? 2 : 1;
    	
    	for ( int iTempCount = 0; iTempCount < iNumItems; iTempCount++ )
    	{
			dropBlockAsItem_do( world, i, j, k, new ItemStack( FCBetterThanWolves.fcItemSawDust ) );
    	}
    }
    
    @Override
    public int GetFurnaceBurnTime( int iItemDamage )
    {
    	int iBurnTime = FCBlockPlanks.GetFurnaceBurnTimeByWoodType( iItemDamage );
    	
    	if ( !isDoubleSlab )
    	{
    		iBurnTime >>= 1;
    	}
    	
    	return iBurnTime;
    }    
		
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
    
    @Override
	public void getSubBlocks( int iBlockID, CreativeTabs creativeTabs, List list )
    {
        if ( iBlockID != Block.woodDoubleSlab.blockID )
        {
            for ( int iTempDamage = 0; iTempDamage <= 4; ++iTempDamage)
            {
                list.add( new ItemStack( iBlockID, 1, iTempDamage) );
            }
        }
    }
    
    @Override
    public void registerIcons( IconRegister register ) {}
    
    @Override
    public Icon getIcon( int iSide, int iMetadata )
    {
        return Block.planks.getIcon( iSide, iMetadata & 7 );
    }
}
