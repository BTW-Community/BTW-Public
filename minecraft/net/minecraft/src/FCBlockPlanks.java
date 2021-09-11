// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCBlockPlanks extends Block
{
    public static final String[] m_sWoodTypes = new String[] { "oak", "spruce", "birch", "jungle", "blood" };
    public static final String[] m_sWoodTextureTypes = new String[] { "wood", "wood_spruce", "wood_birch", "wood_jungle", "fcBlockPlanks_blood" };

    public FCBlockPlanks( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialPlanks );
        
        SetAxesEffectiveOn();
        
        setHardness( 1F );
        setResistance( 5F );
        
		SetFireProperties( FCEnumFlammability.PLANKS );
		
        SetBuoyant();
        
        setStepSound( soundWoodFootstep );
        
        setUnlocalizedName("wood");        
        
        setCreativeTab( CreativeTabs.tabBlock );
    }
    
    @Override
    public int damageDropped( int iMetadata )
    {
        return iMetadata;
    }

	@Override
    public int GetItemIDDroppedOnSaw( World world, int i, int j, int k )
    {
    	return FCBetterThanWolves.fcBlockWoodSidingItemStubID;
    }

	@Override
    public int GetItemCountDroppedOnSaw( World world, int i, int j, int k )
    {
    	return 2;
    }
    
	@Override
    public int GetItemDamageDroppedOnSaw( World world, int i, int j, int k )
    {
		world.getBlockMetadata( i, j, k );
		
		return world.getBlockMetadata( i, j, k );
    }
    
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 2; // iron or better
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, 2, 0, fChanceOfDrop );
		
		return true;
	}
    
    @Override
    public int GetFurnaceBurnTime( int iItemDamage )
    {
    	return GetFurnaceBurnTimeByWoodType( iItemDamage );
    }
    
    //------------- Class Specific Methods ------------//
	
	public static int GetFurnaceBurnTimeByWoodType( int iWoodType )
	{
		if ( iWoodType == 0 ) // oak
		{
			return FCEnumFurnaceBurnTime.PLANKS_OAK.m_iBurnTime;
		}
		else if ( iWoodType == 1 ) // spruce
		{
			return FCEnumFurnaceBurnTime.PLANKS_SPRUCE.m_iBurnTime;
		}
		else if ( iWoodType == 2 ) // birch
		{
			return FCEnumFurnaceBurnTime.PLANKS_BIRCH.m_iBurnTime;
		}
		else if ( iWoodType == 3 ) // jungle
		{
			return FCEnumFurnaceBurnTime.PLANKS_JUNGLE.m_iBurnTime;
		}
		else // blood == 4
		{
			return FCEnumFurnaceBurnTime.PLANKS_BLOOD.m_iBurnTime;
		}
	}
	
	//----------- Client Side Functionality -----------//
    
    private Icon[] m_iconArray;
    
    @Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		return m_iconArray[iMetadata];
    }

    @Override
    public void getSubBlocks( int iBlockID, CreativeTabs creativeTabs, List list )
    {
    	for ( int iTempType = 0; iTempType < m_sWoodTypes.length; iTempType++ )
    	{
    		list.add( new ItemStack(iBlockID, 1, iTempType ) );
    	}
    }

    @Override
    public void registerIcons( IconRegister register )
    {
        m_iconArray = new Icon[m_sWoodTextureTypes.length];

        for ( int iTempIndex = 0; iTempIndex < m_iconArray.length; ++iTempIndex )
        {
            m_iconArray[iTempIndex] = register.registerIcon( m_sWoodTextureTypes[iTempIndex] );
        }
    }
}
