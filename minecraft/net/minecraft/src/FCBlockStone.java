// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockStone extends FCBlockFullBlock
{
    public FCBlockStone( int iBlockID )
    {
        super( iBlockID, Material.rock );
        
        setHardness( 2.25F );
        setResistance( 10F );
        
        SetPicksEffectiveOn();
        SetChiselsEffectiveOn();
        
        setStepSound( soundStoneFootstep );
        
        setUnlocalizedName( "stone" );        
        
        setCreativeTab( CreativeTabs.tabBlock );
    }
    
    @Override
    public float getBlockHardness( World world, int i, int j, int k )
    {
    	int iStrata = GetStrata( world, i, j, k );
    	
    	if ( iStrata != 0 )
    	{
    		// normal stone has a hardness of 2.25
    		
	    	if ( iStrata == 1 )
	    	{
	    		return 3.0F;
	    	}
	    	else
	    	{
	    		return 4.5F; 
	    	}
    	}
    	
        return super.getBlockHardness( world, i, j, k );
    }
    
    @Override
    public float getExplosionResistance( Entity entity, World world, int i, int j, int k )
    {
    	int iStrata = GetStrata( world, i, j, k );
    	
    	if ( iStrata != 0 )
    	{
    		// normal stone has a resistance of 10
    		
	    	if ( iStrata == 1 )
	    	{
	    		return 13F * ( 3.0F / 5.0F );
	    	}
	    	else
	    	{
	    		return  20F * ( 3.0F / 5.0F );
	    	}
    	}
    	
        return super.getExplosionResistance( entity, world, i, j, k );
    }
    
    @Override
    public int idDropped( int iMetaData, Random random, int iFortuneModifier )
    {
        return FCBetterThanWolves.fcBlockCobblestoneLoose.blockID;
    }
    
	@Override
    public void dropBlockAsItemWithChance( World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier )
    {
		super.dropBlockAsItemWithChance( world, i, j, k, iMetadata, fChance, iFortuneModifier );
		
        if ( !world.isRemote )
        {
        	dropBlockAsItem_do( world, i, j, k, new ItemStack( FCBetterThanWolves.fcItemStone ) );
        	
        	if ( !GetIsCracked( iMetadata ) )
        	{
            	dropBlockAsItem_do( world, i, j, k, new ItemStack( FCBetterThanWolves.fcItemPileGravel ) );
        	}
        }
    }
	
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemStone.itemID, 5, 0, fChanceOfDrop );
		
		int iNumGravel = GetIsCracked( iMetadata ) ? 2 : 3;
		
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemPileGravel.itemID, iNumGravel, 
			0, fChanceOfDrop );
		
		return true;
	}
	
    @Override
    public boolean CanConvertBlock( ItemStack stack, World world, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public boolean ConvertBlock( ItemStack stack, World world, int i, int j, int k, int iFromSide )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	int iStrata = GetStrata( iMetadata );
    	
    	int iToolLevel = GetConversionLevelForTool( stack, world, i, j, k );    	
    	
    	if ( GetIsCracked( iMetadata ) )  
    	{
    		world.setBlockAndMetadataWithNotify( i, j, k, FCBlockStoneRough.m_startaLevelBlockArray[iStrata].blockID, 0 );
    		
        	if ( !world.isRemote && iToolLevel > 0 )
        	{
    	        world.playAuxSFX( FCBetterThanWolves.m_iStoneRippedOffAuxFXID, i, j, k, 0 );
    	        
                FCUtilsItem.EjectStackFromBlockTowardsFacing( world, i, j, k, 
                	new ItemStack( FCBetterThanWolves.fcItemStone, 1 ), iFromSide );
        	}
    	}
    	else
    	{
        	if ( iToolLevel == 2 )
        	{
        		// level 2 is stone pick on top strata stone, which has its own thing going on
        		
        		world.setBlockAndMetadataWithNotify( i, j, k, FCBlockStoneRough.m_startaLevelBlockArray[iStrata].blockID, 4 );
    	        
    			if ( !world.isRemote )
    			{
        	        world.playAuxSFX( FCBetterThanWolves.m_iStoneRippedOffAuxFXID, i, j, k, 0 );
	    	        
                    FCUtilsItem.EjectStackFromBlockTowardsFacing( world, i, j, k, 
                    	new ItemStack( FCBetterThanWolves.fcItemStone, 3 ), iFromSide );
                    
	                FCUtilsItem.EjectStackFromBlockTowardsFacing( world, i, j, k, 
	                	new ItemStack( FCBetterThanWolves.fcItemPileGravel, 1 ), iFromSide );
    			}
    			
        	}
        	else if ( iToolLevel == 3 )
    		{
        		// level 3 is iron chisel on first two strata, resulting in stone brick
        		
        		world.setBlockAndMetadataWithNotify( i, j, k, 
        			FCBlockStoneRough.m_startaLevelBlockArray[iStrata].blockID, 2 );
        		
    			if ( !world.isRemote )
    			{
        	        world.playAuxSFX( FCBetterThanWolves.m_iStoneRippedOffAuxFXID, i, j, k, 0 );
	    	        
                    FCUtilsItem.EjectStackFromBlockTowardsFacing( world, i, j, k, 
                    	new ItemStack( FCBetterThanWolves.fcItemStoneBrick ), iFromSide );
                    
	                FCUtilsItem.EjectStackFromBlockTowardsFacing( world, i, j, k, 
	                	new ItemStack( FCBetterThanWolves.fcItemPileGravel, 1 ), iFromSide );
    			}
    		}
    		else
    		{
    			if ( !world.isRemote )
    			{
	    	        world.playAuxSFX( FCBetterThanWolves.m_iGravelRippedOffStoneAuxFXID, i, j, k, 0 );							        
	    	        
	                FCUtilsItem.EjectStackFromBlockTowardsFacing( world, i, j, k, 
	                	new ItemStack( FCBetterThanWolves.fcItemPileGravel, 1 ), iFromSide );
    			}
                
    			SetIsCracked( world, i, j, k, true );
    		}
    	}
    	
    	return true;
    }

    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iStrata = GetStrata( blockAccess, i, j, k );
    	
    	if ( iStrata > 1 )
    	{
    		return iStrata + 1;
    	}
    	
    	return 2;
    }
    
    @Override
    public int GetEfficientToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iStrata = GetStrata( blockAccess, i, j, k );
    	
    	if ( iStrata > 0 )
    	{
    		return iStrata + 1;
    	}
    	
    	return 0;
    }
    
    @Override
    public boolean IsNaturalStone( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return true;
    }
    
    //------------- Class Specific Methods ------------//

    /**
     * Returns 1, 2, or 3 depending on the level of the conversion tool.  0 if it can't convert
     */ 
    private int GetConversionLevelForTool( ItemStack stack, World world, int i, int j, int k )
    {
    	if ( stack != null )
    	{
        	if ( stack.getItem() instanceof FCItemPickaxe )
        	{
        		int iToolLevel = ((FCItemTool)stack.getItem()).toolMaterial.getHarvestLevel();
        		
        		if ( iToolLevel >= GetEfficientToolLevel( world, i, j, k ) )
        		{
        			return 2;        				
        		}
        	}  
        	else if ( stack.getItem() instanceof FCItemChisel )
        	{
        		int iToolLevel = ((FCItemTool)stack.getItem()).toolMaterial.getHarvestLevel();
        		
        		if ( iToolLevel >= GetEfficientToolLevel( world, i, j, k ) )
        		{
            		if ( iToolLevel >= GetUberToolLevel( world, i, j, k ) )
            		{
            			return 3;
            		}
            		
        			return 1;
        		}
        	}  
    	}
    	
    	return 0;
    }
    
    public int GetStrata( IBlockAccess blockAccess, int i, int j, int k )
    {
		return GetStrata( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    public int GetStrata( int iMetadata )
    {
    	return iMetadata & 3;
    }
    
	public void SetIsCracked( World world, int i, int j, int k, boolean bCracked )
	{
		int iMetadata = SetIsCracked( world.getBlockMetadata( i, j, k ), bCracked );
		
		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
	}
	
	public int SetIsCracked( int iMetadata, boolean bIsCracked )
	{
		if ( bIsCracked )
		{
			iMetadata |= 4;
		}
		else
		{
			iMetadata &= (~4);
		}
		
		return iMetadata;
	}
    
	public boolean GetIsCracked( IBlockAccess blockAccess, int i, int j, int k )
	{
		return GetIsCracked( blockAccess.getBlockMetadata( i, j, k ) );
	}
	
	public boolean GetIsCracked( int iMetadata )
	{
		return ( iMetadata & 4 ) != 0;
	}
	
    public int GetUberToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 2;
    }
    
	//----------- Client Side Functionality -----------//

    private Icon m_IconByMetadataArray[] = new Icon[16];
    
    @Override
	public void getSubBlocks( int iBlockID, CreativeTabs creativeTabs, List list )
    {
        list.add( new ItemStack( iBlockID, 1, 0 ) );
        list.add( new ItemStack( iBlockID, 1, 1 ) );
        list.add( new ItemStack( iBlockID, 1, 2 ) );
        //list.add( new ItemStack( iBlockID, 1, 4 ) );
        //list.add( new ItemStack( iBlockID, 1, 5 ) );
        //list.add( new ItemStack( iBlockID, 1, 6 ) );
    }
    
	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
		m_IconByMetadataArray[0] = blockIcon;		
		m_IconByMetadataArray[1] = register.registerIcon( "fcBlockStone_1" );
		m_IconByMetadataArray[2] = register.registerIcon( "fcBlockStone_2" );
		m_IconByMetadataArray[3] = blockIcon;
		
		m_IconByMetadataArray[4] = register.registerIcon( "fcBlockStone_cracked" );;
		m_IconByMetadataArray[5] = register.registerIcon( "fcBlockStone_1_cracked" );;
		m_IconByMetadataArray[6] = register.registerIcon( "fcBlockStone_2_cracked" );;
		m_IconByMetadataArray[7] = blockIcon;
		
		for ( int iTempIndex = 8; iTempIndex < 16; iTempIndex++ )
		{
			m_IconByMetadataArray[iTempIndex] = blockIcon;
		}
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
        return m_IconByMetadataArray[iMetadata];    	
    }
}