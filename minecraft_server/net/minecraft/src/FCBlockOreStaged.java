// FCMOD

package net.minecraft.src;

import java.util.Random;

public abstract class FCBlockOreStaged extends FCBlockOre
{
    public FCBlockOreStaged( int iBlockID )
    {
        super( iBlockID );
        
        SetChiselsEffectiveOn();
    }
    
    @Override
    public boolean CanConvertBlock( ItemStack stack, World world, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public boolean ConvertBlock( ItemStack stack, World world, int i, int j, int k, int iFromSide )
    {
    	int iOldMetadata = world.getBlockMetadata( i, j, k );
    	int iStrata = GetStrata( iOldMetadata );
    	
		world.setBlockAndMetadataWithNotify( i, j, k, FCBlockStoneRough.m_startaLevelBlockArray[iStrata].blockID, 4 );
    	
    	if ( !world.isRemote )
    	{
    		int iLevel = GetConversionLevelForTool( stack, world, i, j, k );

    		if ( iLevel > 0 )
    		{
		        world.playAuxSFX( FCBetterThanWolves.m_iStoneRippedOffAuxFXID, i, j, k, 0 );							        
		        
	    		if ( iLevel >= 3 )
				{
	    			EjectItemsOnGoodPickConversion( stack, world, i, j, k, iOldMetadata, iFromSide );
				}
	    		else if ( iLevel == 2 )
	    		{
	    			EjectItemsOnStonePickConversion( stack, world, i, j, k, iOldMetadata, iFromSide );
	    		}
	    		else
	    		{
	    			EjectItemsOnChiselConversion( stack, world, i, j, k, iOldMetadata, iFromSide );
	    		}
    		}
    	}
    	
    	return true;
    }
    
	@Override
    public void dropBlockAsItemWithChance( World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier )
    {
		super.dropBlockAsItemWithChance( world, i, j, k, iMetadata, fChance, iFortuneModifier );
		
        if ( !world.isRemote )
        {
    		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemStone.itemID, 6, 0, 1F );		
        }
    }
    
    @Override
    public int GetEfficientToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetRequiredToolLevelForOre( blockAccess, i, j, k );
    }

    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iLevelForOre = GetRequiredToolLevelForOre( blockAccess, i, j, k );
		int iLevelForStrata = GetRequiredToolLevelForStrata( blockAccess, i, j, k );
		
		if ( iLevelForStrata > iLevelForOre )
		{
			return iLevelForStrata;
		}
		
		return iLevelForOre;		
    }
    
    //------------- Class Specific Methods ------------//	
    
    public abstract int IdDroppedOnConversion( int iMetadata );
    
    public int DamageDroppedOnConversion( int iMetadata )
    {
    	return 0;
    }
    
    public int QuantityDroppedOnConversion( Random rand )
    {
        return 1;
    }

    public int IdDroppedOnStonePickConversion( int iMetadata, Random rand, int iFortuneModifier )
    {
    	return idDropped( iMetadata, rand, iFortuneModifier );
    }
    
    public int DamageDroppedOnStonePickConversion( int iMetadata )
    {
    	return damageDropped( iMetadata );
    }
    
    public int QuantityDroppedOnStonePickConversion( Random rand )
    {
        return quantityDropped( rand );
    }

    protected void EjectItemsOnGoodPickConversion( ItemStack stack, World world, int i, int j, int k, int iOldMetadata, int iFromSide )
    {
        FCUtilsItem.EjectStackFromBlockTowardsFacing( world, i, j, k, 
        	new ItemStack( idDropped( iOldMetadata, world.rand, 0 ), 
    		quantityDropped( world.rand ), 
    		damageDropped( iOldMetadata ) ), iFromSide );
    }
    
    protected void EjectItemsOnStonePickConversion( ItemStack stack, World world, int i, int j, int k, int iOldMetadata, int iFromSide )
    {
        FCUtilsItem.EjectStackFromBlockTowardsFacing( world, i, j, k, 
        	new ItemStack( IdDroppedOnStonePickConversion( iOldMetadata, world.rand, 0 ), 
    		QuantityDroppedOnStonePickConversion( world.rand ), 
    		DamageDroppedOnStonePickConversion( iOldMetadata ) ), iFromSide );
    }
    
    protected void EjectItemsOnChiselConversion( ItemStack stack, World world, int i, int j, int k, int iOldMetadata, int iFromSide )
    {
        FCUtilsItem.EjectStackFromBlockTowardsFacing( world, i, j, k, 
        	new ItemStack( IdDroppedOnConversion( iOldMetadata ), 
    		QuantityDroppedOnConversion( world.rand ), 
    		DamageDroppedOnConversion( iOldMetadata ) ), iFromSide );
    }
    
    public int GetRequiredToolLevelForOre( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 0;
    }

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
        		
        		if ( iToolLevel >= GetRequiredToolLevelForOre( world, i, j, k ) )
        		{
        			if ( iToolLevel > 1 )
        			{
        				return 3;
        			}
        			
        			return 2;        				
        		}
        	}  
        	else if ( stack.getItem() instanceof FCItemChisel )
        	{
        		int iToolLevel = ((FCItemTool)stack.getItem()).toolMaterial.getHarvestLevel();
        		
        		if ( iToolLevel >= GetRequiredToolLevelForOre( world, i, j, k ) )
        		{
        			return 1;
        		}
        	}  
    	}
    	
    	return 0;
    }
    
	//------------ Client Side Functionality ----------//
}
