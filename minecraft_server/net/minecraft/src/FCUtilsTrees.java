// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCUtilsTrees
{
    static public boolean GenerateTaiga2( World world, Random random, int i, int j, int k )
    {
    	// FCMOD: Copies over from WorldGenTaiga2.java, generate().  Changes marked with FCMOD
        int l;
        int i1;
        int j1;
        int k1;
        boolean flag;
label0:
        {
            l = random.nextInt(4) + 6;
            i1 = 1 + random.nextInt(2);
            j1 = l - i1;
            k1 = 2 + random.nextInt(2);
            flag = true;
            if(j >= 1)
            {
                world.getClass();
                if(j + l + 1 <= 128)
                {
                    break label0;
                }
            }
            return false;
        }
label1:
        {
            for(int l1 = j; l1 <= j + 1 + l && flag; l1++)
            {
                int j2 = 1;
                if(l1 - j < i1)
                {
                    j2 = 0;
                } else
                {
                    j2 = k1;
                }
                for(int l2 = i - j2; l2 <= i + j2 && flag; l2++)
                {
                    for(int j3 = k - j2; j3 <= k + j2 && flag; j3++)
                    {
                        if(l1 >= 0)
                        {
                            world.getClass();
                            if(l1 < 128)
                            {
                                int k3 = world.getBlockId(l2, l1, j3);
                                // FCMOD
                                if( !world.isAirBlock(l2, l1, j3) && k3 != Block.leaves.blockID && k3 != Block.snow.blockID )
                            	// END FCMOD
                                {
                                    flag = false;
                                }
                                continue;
                            }
                        }
                        flag = false;
                    }

                }

            }

            if(!flag)
            {
                return false;
            }
            int i2 = world.getBlockId(i, j - 1, k);
            // FCMOD: Changed
            if ( FCUtilsTrees.CanSaplingGrowOnBlock( world, i, j - 1, k ) )
        	// END FCMOD
            {
                world.getClass();
                if(j < 128 - l - 1)
                {
                    break label1;
                }
            }
            return false;
        }
        // FCMOD: Changed the following
        if ( world.getBlockId(i, j - 1, k) == Block.grass.blockID )
        {
        	world.setBlockWithNotify(i, j - 1, k, Block.dirt.blockID);
        }
    	// END FCMOD
        int k2 = random.nextInt(2);
        int i3 = 1;
        boolean flag1 = false;
        for(int l3 = 0; l3 <= j1; l3++)
        {
            int j4 = (j + l) - l3;
            for(int l4 = i - k2; l4 <= i + k2; l4++)
            {
                int j5 = l4 - i;
                for(int k5 = k - k2; k5 <= k + k2; k5++)
                {
                    int l5 = k5 - k;
                    if((Math.abs(j5) != k2 || Math.abs(l5) != k2 || k2 <= 0) && !Block.opaqueCubeLookup[world.getBlockId(l4, j4, k5)])
                    {
                    	// FCMOD
                        world.setBlockAndMetadataWithNotify(l4, j4, k5, Block.leaves.blockID, 1);
                    	// END FCMOD
                    }
                }

            }

            if(k2 >= i3)
            {
                k2 = ((flag1) ? 1 : 0);
                flag1 = true;
                if(++i3 > k1)
                {
                    i3 = k1;
                }
            } else
            {
                k2++;
            }
        }

        int i4 = random.nextInt(3);
        for(int k4 = 0; k4 < l - i4; k4++)
        {
            int i5 = world.getBlockId(i, j + k4, k);
            // FCMOD
            if( world.isAirBlock(i, j + k4, k) || i5 == Block.leaves.blockID || i5 == Block.snow.blockID )
            {
                world.setBlockAndMetadataWithNotify(i, j + k4, k, Block.wood.blockID, 1);
            }
        	// END FCMOD
        }

        // FCMOD: Added                    
        if ( l > 2 ) // only tree with trunks 3 blocks or higher generate trunks
        {
            int iTrunkBlockId = world.getBlockId( i, j, k );
            
            if ( iTrunkBlockId == Block.wood.blockID )
            {
            	int iTrunkMetadata = world.getBlockMetadata( i, j, k );
            	
            	if ( iTrunkMetadata == 1 )
            	{
            		world.setBlockMetadataWithClient( i, j, k, 13 );
            	}
            }
        }                    
        // END FCMOD
        
        return true;
    }
    
    static public boolean GenerateForest(World world, Random random, int i, int j, int k)
    {
        int l;
        boolean flag;
label0:
        {
            l = random.nextInt(3) + 5;
            flag = true;
            if(j >= 1)
            {
                world.getClass();
                if(j + l + 1 <= 128)
                {
                    break label0;
                }
            }
            return false;
        }
label1:
        {
            for(int i1 = j; i1 <= j + 1 + l; i1++)
            {
                byte byte0 = 1;
                if(i1 == j)
                {
                    byte0 = 0;
                }
                if(i1 >= (j + 1 + l) - 2)
                {
                    byte0 = 2;
                }
                for(int i2 = i - byte0; i2 <= i + byte0 && flag; i2++)
                {
                    for(int l2 = k - byte0; l2 <= k + byte0 && flag; l2++)
                    {
                        if(i1 >= 0)
                        {
                            world.getClass();
                            if(i1 < 128)
                            {
                                int j3 = world.getBlockId(i2, i1, l2);
                                //FCMOD
                                if( !world.isAirBlock(i2, i1, l2) && j3 != Block.leaves.blockID)
                            	// END FCMOD
                                {
                                    flag = false;
                                }
                                continue;
                            }
                        }
                        flag = false;
                    }

                }

            }

            if(!flag)
            {
                return false;
            }
            int j1 = world.getBlockId(i, j - 1, k);
            // FCMOD: Changed
            if ( FCUtilsTrees.CanSaplingGrowOnBlock( world, i, j - 1, k ) )
        	// END FCMOD
            {
                world.getClass();
                if(j < 128 - l - 1)
                {
                    break label1;
                }
            }
            return false;
        }
        // FCMOD: Changed the following
        if ( world.getBlockId(i, j - 1, k) == Block.grass.blockID )
        {
        	world.setBlockWithNotify(i, j - 1, k, Block.dirt.blockID);
        }
        // END FCMOD
        for(int k1 = (j - 3) + l; k1 <= j + l; k1++)
        {
            int j2 = k1 - (j + l);
            int i3 = 1 - j2 / 2;
            for(int k3 = i - i3; k3 <= i + i3; k3++)
            {
                int l3 = k3 - i;
                for(int i4 = k - i3; i4 <= k + i3; i4++)
                {
                    int j4 = i4 - k;
                    if((Math.abs(l3) != i3 || Math.abs(j4) != i3 || random.nextInt(2) != 0 && j2 != 0) && !Block.opaqueCubeLookup[world.getBlockId(k3, k1, i4)])
                    {
                    	// FCMOD
                        world.setBlockAndMetadataWithNotify(k3, k1, i4, Block.leaves.blockID, 2);
                    	// END FCMOD
                    }
                }

            }

        }

        for(int l1 = 0; l1 < l; l1++)
        {
            int k2 = world.getBlockId(i, j + l1, k);
            // FCMOD
            if( world.isAirBlock(i, j + l1, k) || k2 == Block.leaves.blockID)
            {
                world.setBlockAndMetadataWithNotify(i, j + l1, k, Block.wood.blockID, 2);
            }
            // END FCMOD
        }

        // FCMOD: Added                    
        if ( l > 2 ) // only tree with trunks 3 blocks or higher generate trunks
        {
            int iTrunkBlockId = world.getBlockId( i, j, k );
            
            if ( iTrunkBlockId == Block.wood.blockID )
            {
            	int iTrunkMetadata = world.getBlockMetadata( i, j, k );
            	
            	if ( iTrunkMetadata == 2 )
            	{
            		world.setBlockMetadataWithClient( i, j, k, 14 );
            	}
            }
        }                    
        // END FCMOD
        
        return true;
    }
    
    static public boolean GenerateTrees(World world, Random random, int iBase, int jBase, int kBase, int iBaseHeight, int iWoodMetadata, int iLeafMetadata, boolean bGenerateVines )
    {
        int iTreeHeight = random.nextInt(3) + iBaseHeight;
        boolean flag = true;

        if (jBase < 1 || jBase + iTreeHeight + 1 > 256)
        {
            return false;
        }

        for (int j = jBase; j <= jBase + 1 + iTreeHeight; j++)
        {
            byte byte0 = 1;

            if (j == jBase)
            {
                byte0 = 0;
            }

            if (j >= (jBase + 1 + iTreeHeight) - 2)
            {
                byte0 = 2;
            }

            for (int l = iBase - byte0; l <= iBase + byte0 && flag; l++)
            {
                for (int j1 = kBase - byte0; j1 <= kBase + byte0 && flag; j1++)
                {
                    if (j >= 0 && j < 256)
                    {
                        int j2 = world.getBlockId(l, j, j1);

                        if (!world.isAirBlock( l, j, j1 ) && j2 != Block.leaves.blockID && j2 != Block.grass.blockID && j2 != Block.dirt.blockID && j2 != Block.wood.blockID)
                        {
                            flag = false;
                        }
                    }
                    else
                    {
                        flag = false;
                    }
                }
            }
        }

        if (!flag)
        {
            return false;
        }

        int k = world.getBlockId(iBase, jBase - 1, kBase);

        // FCMOD
        if ( !FCUtilsTrees.CanSaplingGrowOnBlock( world, iBase, jBase - 1, kBase ) || 
        	jBase >= 256 - iTreeHeight - 1)
    	// END FCMOD
        {
            return false;
        }

        // FCMOD
        if ( k == Block.grass.blockID )
    	// END FCMOD
        {
        	world.setBlockWithNotify( iBase, jBase - 1, kBase, Block.dirt.blockID);
        }
        
        byte byte1 = 3;
        int i1 = 0;

        for (int k1 = (jBase - byte1) + iTreeHeight; k1 <= jBase + iTreeHeight; k1++)
        {
            int k2 = k1 - (jBase + iTreeHeight);
            int j3 = (i1 + 1) - k2 / 2;

            for (int l3 = iBase - j3; l3 <= iBase + j3; l3++)
            {
                int j4 = l3 - iBase;

                for (int l4 = kBase - j3; l4 <= kBase + j3; l4++)
                {
                    int i5 = l4 - kBase;

                    if ((Math.abs(j4) != j3 || Math.abs(i5) != j3 || random.nextInt(2) != 0 && k2 != 0) && !Block.opaqueCubeLookup[world.getBlockId(l3, k1, l4)])
                    {
                        world.setBlockAndMetadataWithNotify(l3, k1, l4, Block.leaves.blockID, iLeafMetadata);
                    }
                }
            }
        }

        for (int l1 = 0; l1 < iTreeHeight; l1++)
        {
            int l2 = world.getBlockId(iBase, jBase + l1, kBase);

            // FCMOD
            if (!world.isAirBlock(iBase, jBase + l1, kBase) && l2 != Block.leaves.blockID)
        	// END FCMOD
            {
                continue;
            }

            world.setBlockAndMetadataWithNotify(iBase, jBase + l1, kBase, Block.wood.blockID, iWoodMetadata);

            if (!bGenerateVines || l1 <= 0)
            {
                continue;
            }

            if (random.nextInt(3) > 0 && world.isAirBlock(iBase - 1, jBase + l1, kBase))
            {
            	world.setBlockAndMetadataWithNotify(iBase - 1, jBase + l1, kBase, Block.vine.blockID, 8);
            }

            if (random.nextInt(3) > 0 && world.isAirBlock(iBase + 1, jBase + l1, kBase))
            {
            	world.setBlockAndMetadataWithNotify(iBase + 1, jBase + l1, kBase, Block.vine.blockID, 2);
            }

            if (random.nextInt(3) > 0 && world.isAirBlock(iBase, jBase + l1, kBase - 1))
            {
            	world.setBlockAndMetadataWithNotify(iBase, jBase + l1, kBase - 1, Block.vine.blockID, 1);
            }

            if (random.nextInt(3) > 0 && world.isAirBlock(iBase, jBase + l1, kBase + 1))
            {
            	world.setBlockAndMetadataWithNotify(iBase, jBase + l1, kBase + 1, Block.vine.blockID, 4);
            }
        }

        if (bGenerateVines)
        {
            for (int i2 = (jBase - 3) + iTreeHeight; i2 <= jBase + iTreeHeight; i2++)
            {
                int i3 = i2 - (jBase + iTreeHeight);
                int k3 = 2 - i3 / 2;

                for (int i4 = iBase - k3; i4 <= iBase + k3; i4++)
                {
                    for (int k4 = kBase - k3; k4 <= kBase + k3; k4++)
                    {
                        if (world.getBlockId(i4, i2, k4) != Block.leaves.blockID)
                        {
                            continue;
                        }

                        if (random.nextInt(4) == 0 && world.isAirBlock(i4 - 1, i2, k4))
                        {
                        	PlaceVine(world, i4 - 1, i2, k4, 8);
                        }

                        if (random.nextInt(4) == 0 && world.isAirBlock(i4 + 1, i2, k4))
                        {
                        	PlaceVine(world, i4 + 1, i2, k4, 2);
                        }

                        if (random.nextInt(4) == 0 && world.isAirBlock(i4, i2, k4 - 1))
                        {
                        	PlaceVine(world, i4, i2, k4 - 1, 1);
                        }

                        if (random.nextInt(4) == 0 && world.isAirBlock(i4, i2, k4 + 1))
                        {
                        	PlaceVine(world, i4, i2, k4 + 1, 4);
                        }
                    }
                }
            }
        }

        // FCMOD: Added                    
        if ( iTreeHeight > 2 ) // only tree with trunks 3 blocks or higher generate trunks
        {
            int iTrunkBlockId = world.getBlockId( iBase, jBase, kBase );
            
            if ( iTrunkBlockId == Block.wood.blockID )
            {
            	int iTrunkMetadata = world.getBlockMetadata( iBase, jBase, kBase );
            	
            	if ( iTrunkMetadata == iWoodMetadata )
            	{
            		world.setBlockMetadataWithClient( iBase, jBase, kBase, iWoodMetadata | 12 );
            	}
            }
        }                    
        // END FCMOD
        
        return true;
    }
    
    static private void PlaceVine(World par1World, int par2, int par3, int par4, int par5)
    {
    	par1World.setBlockAndMetadataWithNotify(par2, par3, par4, Block.vine.blockID, par5);

        for (int i = 4; par1World.getBlockId(par2, --par3, par4) == 0 && i > 0; i--)
        {
        	par1World.setBlockAndMetadataWithNotify(par2, par3, par4, Block.vine.blockID, par5);
        }
    }
    
    static public boolean GenerateTrees(World world, Random random, int i, int j, int k)
    {
    	return GenerateTrees( world, random, i, j, k, 4, 0, 0, false );
    }
    
    static public boolean CanSaplingGrowOnBlock( World world, int i, int j, int k )
    {
        Block blockBelow = Block.blocksList[world.getBlockId( i, j, k )];
        
        return blockBelow != null && blockBelow.CanSaplingsGrowOnBlock( world, i, j, k );
    }
}
