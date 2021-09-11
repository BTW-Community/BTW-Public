// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockSilverfish extends BlockSilverfish
{
	private static final int m_iHatchFrequency = 1200;
	
    public FCBlockSilverfish( int iBlockID )
    {
    	super( iBlockID );
    	
    	setHardness( 1.5F );
    	
    	SetPicksEffectiveOn();
        SetChiselsEffectiveOn();
    	
        setTickRandomly( true );
        
        setUnlocalizedName( "monsterStoneEgg" );
    }
    
	@Override
    public void RandomUpdateTick( World world, int i, int j, int k, Random rand )
    {
		if ( world.provider.dimensionId == 1 )
		{
			if ( rand.nextInt( m_iHatchFrequency ) == 0 )
			{
				// block destroy FX
				
				int iMetadata = world.getBlockMetadata( i, j, k );
				
		        world.playAuxSFX( FCBetterThanWolves.m_iBlockDestroyRespectParticleSettingsAuxFXID, i, j, k, blockID + ( iMetadata << 12 ) );
		        
				world.setBlockWithNotify( i, j, k, 0 );
				
				int iNumSilverfish = 1;
				
				if ( rand.nextInt( 2 ) == 0 )
				{
					iNumSilverfish++;
				}
				
				for ( int iTempCount = 0; iTempCount < iNumSilverfish; iTempCount++ )
				{
		            EntitySilverfish silverfish = new EntitySilverfish( world );
		            
		            silverfish.setLocationAndAngles( (double)i + 0.5D, (double)j, (double)k + 0.5D, 0.0F, 0.0F);
		            
		            world.spawnEntityInWorld( silverfish );
				}
				
				FCUtilsItem.DropSingleItemAsIfBlockHarvested( world, i, j, k, Block.gravel.blockID, 0 );
				
				FCUtilsItem.DropSingleItemAsIfBlockHarvested( world, i, j, k, Item.clay.itemID, 0 );				
			}
		}
    }
    
    @Override
    protected ItemStack createStackedBlock( int iMetadata )
    {
        Block block = Block.stone;
        int iItemDamage = 0;

        if (iMetadata == 1)
        {
            block = Block.cobblestone;
        }
        else if (iMetadata == 2)
        {
            block = Block.stoneBrick;
        }
        else if ( iMetadata == 14 )
        {
        	iItemDamage = 1;
        }
        else if ( iMetadata == 15 )
        {
        	iItemDamage = 2;
        }

        return new ItemStack( block, 1, iItemDamage );
    }
    
    @Override
    public boolean HasStrata()
    {
    	return true;
    }
    
    @Override
    public int GetMetadataConversionForStrataLevel( int iLevel, int iMetadata )
    {
    	if ( iMetadata == 0 ) // regular stone block
    	{
    		if ( iLevel == 1 )
    		{
    			iMetadata = 14;
    		}
    		else if ( iLevel == 2 )
    		{
    			iMetadata = 15;
    		}
    	}
    	
    	return iMetadata;
    }
    
    public static int GetMetadataConversionOnInfest( int iBlockID, int iMetadata )
    {
    	int iNewMetadata = 0;
    	
    	if ( iBlockID == Block.cobblestone.blockID )
    	{
    		iNewMetadata = 1;
    	}
    	else if ( iBlockID == Block.stoneBrick.blockID )
    	{
    		iNewMetadata = 2;
    	}
    	else if ( iMetadata == 1 )
    	{
    		iNewMetadata = 14;    		
    	}
    	else if ( iMetadata == 2 )
    	{
    		iNewMetadata = 15;    		
    	}
    	
        return iNewMetadata;
    }
    
	//----------- Client Side Functionality -----------//
    
	@Override
    public void randomDisplayTick( World world, int i, int j, int k, Random rand )
    {
        if ( rand.nextInt( 32 ) == 0 )
        {
            world.playSound( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "mob.silverfish.step", 
            	rand.nextFloat() * 0.05F + 0.2F, rand.nextFloat() * 1.0F + 0.5F, false );
        }
    }
	
    @Override
    public Icon getIcon( int iSide, int iMetadata )
    {
    	if ( iMetadata == 1 )
    	{
    		return Block.cobblestone.getBlockTextureFromSide( iSide );
    	}
    	else if ( iMetadata == 2 )
    	{
    		return Block.stoneBrick.getBlockTextureFromSide( iSide );
    	}
    	else if ( iMetadata == 14 )
		{
			return Block.stone.getIcon( iSide, 1 );
		}
		else if ( iMetadata == 15 )
		{
			return Block.stone.getIcon( iSide, 2 );
		}
    	
        return Block.stone.getBlockTextureFromSide( iSide );
    }
    
    @Override
    public boolean RenderBlock( RenderBlocks renderer, int i, int j, int k )
    {
    	return renderer.RenderStandardFullBlock( this, i, j, k );
    }
    
    @Override
    public boolean DoesItemRenderAsBlock( int iItemDamage )
    {
    	return true;
    }    
    
    @Override
    public void RenderBlockMovedByPiston( RenderBlocks renderBlocks, int i, int j, int k )
    {
    	renderBlocks.RenderStandardFullBlockMovedByPiston( this, i, j, k );
    }    
}
