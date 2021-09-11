// FCMOD: A lot of this is copied over from BlockLeaves to provide control and access to vars

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockBloodWoodLeaves extends FCBlockLeaves
{
    public FCBlockBloodWoodLeaves( int iBlockID )
    {
        super( iBlockID );
        
        setUnlocalizedName( "fcBlockBloodWoodLeaves" );
        
        setHardness( 0.2F );    	
        SetAxesEffectiveOn( true );        
        SetBuoyancy( 1F );
        
        setLightOpacity( 1 );
        
		SetFireProperties( FCEnumFlammability.EXTREME );
		
        setStepSound( soundGrassFootstep );
		
		setCreativeTab( CreativeTabs.tabDecorations );
    }
    
	@Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
        int l = 1;
        int i1 = l + 1;
        
        if(world.checkChunksExist(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1))
        {
            for(int j1 = -l; j1 <= l; j1++)
            {
                for(int k1 = -l; k1 <= l; k1++)
                {
                    for(int l1 = -l; l1 <= l; l1++)
                    {
                        int i2 = world.getBlockId(i + j1, j + k1, k + l1);
                        
                        if ( i2 == blockID )
                        {
                            int j2 = world.getBlockMetadata(i + j1, j + k1, k + l1);
                            world.setBlockMetadata(i + j1, j + k1, k + l1, j2 | 8);
                        }
                    }
                }
            }
        }
        
        super.breakBlock( world, i, j, k, iBlockID, iMetadata );
    }
    
	@Override
    public int idDropped( int iMetadata, Random random, int iFortuneModifier )
    {
    	return FCBetterThanWolves.fcAestheticVegetation.blockID;  // for bloodwood sapling
    }

	@Override
    public void harvestBlock( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
        if( !world.isRemote && player.getCurrentEquippedItem() != null && 
    		player.getCurrentEquippedItem().itemID == Item.shears.itemID )
        {
            dropBlockAsItem_do( world, i, j, k, new ItemStack( FCBetterThanWolves.fcBlockBloodLeaves.blockID, 1, iMetadata & 3 ) );
            
            player.getCurrentEquippedItem().damageItem( 1, player );
        } 
        else
        {
            super.harvestBlock( world, player, i, j, k, iMetadata );
        }
    }
    
	@Override
    public void dropBlockAsItemWithChance(World world, int i, int j, int k, int l, float f, int i1)
    {
		// override for BlockLeaves functionality to avoid dropping apples 
        if (world.isRemote)
        {
            return;
        }
        int j1 = quantityDroppedWithBonus(i1, world.rand);
        for (int k1 = 0; k1 < j1; k1++)
        {
            if (world.rand.nextFloat() > f)
            {
                continue;
            }
            int l1 = idDropped(l, world.rand, i1);
            if (l1 > 0)
            {
                dropBlockAsItem_do(world, i, j, k, new ItemStack(l1, 1, damageDropped(l)));
            }
        }
    }
	
	@Override
    public int damageDropped( int iMetaData )
    {
    	return FCBlockAestheticVegetation.m_iSubtypeBloodWoodSapling;    	
    }
    
	@Override
    protected void dropBlockAsItem_do( World world, int i, int j, int k, ItemStack itemStack )
    {
		if ( itemStack.itemID == FCBetterThanWolves.fcAestheticVegetation.blockID && 
			itemStack.getItemDamage() == FCBlockAestheticVegetation.m_iSubtypeBloodWoodSapling )
		{		
			// special case bloodwood saplings to generate a self-planting EntityItem
			
	        if ( world.isRemote)
	        {
	            return;
	        }
	        else
	        {
	            float f = 0.7F;
	            
	            double d = (double)( world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
	            double d1 = (double)( world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
	            double d2 = (double)( world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
	            
	            EntityItem entityitem = new FCEntityItemBloodWoodSapling( world, (double)i + d, (double)j + d1, (double)k + d2, itemStack );
	            
	            entityitem.delayBeforeCanPickup = 10;
	            world.spawnEntityInWorld(entityitem);
	            
	            return;
	        }
		}
		else
		{
			super.dropBlockAsItem_do( world, i, j, k, itemStack );
		}
    }
    
	@Override
    public boolean isOpaqueCube()
    {
        return !( Block.leaves.graphicsLevel );
    }
    
	@Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if ( world.isRemote )
        {
            return;
        }
        int l = world.getBlockMetadata(i, j, k);
        if((l & 8) != 0 && (l & 4) == 0)
        {
            byte byte0 = 4;
            int i1 = byte0 + 1;
            byte byte1 = 32;
            int j1 = byte1 * byte1;
            int k1 = byte1 / 2;
            if(adjacentTreeBlocks == null)
            {
                adjacentTreeBlocks = new int[byte1 * byte1 * byte1];
            }
            if(world.checkChunksExist(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1))
            {
                for(int l1 = -byte0; l1 <= byte0; l1++)
                {
                    for(int k2 = -byte0; k2 <= byte0; k2++)
                    {
                        for(int i3 = -byte0; i3 <= byte0; i3++)
                        {
                            int k3 = world.getBlockId(i + l1, j + k2, k + i3);
                            if(k3 == FCBetterThanWolves.fcBloodWood.blockID)
                            {
                                adjacentTreeBlocks[(l1 + k1) * j1 + (k2 + k1) * byte1 + (i3 + k1)] = 0;
                                continue;
                            }
                            if ( k3 == FCBetterThanWolves.fcBlockBloodLeaves.blockID )
                            {
                                adjacentTreeBlocks[(l1 + k1) * j1 + (k2 + k1) * byte1 + (i3 + k1)] = -2;
                            } else
                            {
                                adjacentTreeBlocks[(l1 + k1) * j1 + (k2 + k1) * byte1 + (i3 + k1)] = -1;
                            }
                        }

                    }

                }

                for(int i2 = 1; i2 <= 4; i2++)
                {
                    for(int l2 = -byte0; l2 <= byte0; l2++)
                    {
                        for(int j3 = -byte0; j3 <= byte0; j3++)
                        {
                            for(int l3 = -byte0; l3 <= byte0; l3++)
                            {
                                if(adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] != i2 - 1)
                                {
                                    continue;
                                }
                                if(adjacentTreeBlocks[((l2 + k1) - 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] == -2)
                                {
                                    adjacentTreeBlocks[((l2 + k1) - 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] = i2;
                                }
                                if(adjacentTreeBlocks[(l2 + k1 + 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] == -2)
                                {
                                    adjacentTreeBlocks[(l2 + k1 + 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] = i2;
                                }
                                if(adjacentTreeBlocks[(l2 + k1) * j1 + ((j3 + k1) - 1) * byte1 + (l3 + k1)] == -2)
                                {
                                    adjacentTreeBlocks[(l2 + k1) * j1 + ((j3 + k1) - 1) * byte1 + (l3 + k1)] = i2;
                                }
                                if(adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 + 1) * byte1 + (l3 + k1)] == -2)
                                {
                                    adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 + 1) * byte1 + (l3 + k1)] = i2;
                                }
                                if(adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + ((l3 + k1) - 1)] == -2)
                                {
                                    adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + ((l3 + k1) - 1)] = i2;
                                }
                                if(adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1 + 1)] == -2)
                                {
                                    adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1 + 1)] = i2;
                                }
                            }

                        }

                    }

                }

            }
            int j2 = adjacentTreeBlocks[k1 * j1 + k1 * byte1 + k1];
            if(j2 >= 0)
            {
                world.setBlockMetadata(i, j, k, l & -9);
            } else
            {
                dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
                world.setBlockWithNotify(i, j, k, 0);
            }
        }
    }    

	@Override
    public void OnDestroyedByFire( World world, int i, int j, int k, int iFireAge, boolean bForcedFireSpread )
    {
		dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
		
		super.OnDestroyedByFire( world, i, j, k, iFireAge, bForcedFireSpread );
    }
	
	@Override
	protected void GenerateAshOnBurn( World world, int i, int j, int k )
	{
		// no ash from bloodwood
	}
	
	@Override
	public boolean CanRotateOnTurntable( IBlockAccess iBlockAccess, int i, int j, int k )
	{
		return false;
	}
	
	@Override
	public boolean CanTransmitRotationHorizontallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return false;
	}
	
	@Override
	public boolean CanTransmitRotationVerticallyOnTurntable( IBlockAccess blockAccess, int i, int j, int k )
	{
		return false;
	}
	
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}