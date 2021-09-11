// FCMOD: Copied over from WorldGenBigTree.  Changes marked with FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCUtilsGenBigTree extends WorldGenBigTree
{
    public FCUtilsGenBigTree( boolean bDoBlockNotify )
    {
        super( bDoBlockNotify );
    }

    @Override
    void genTreeLayer(int par1, int par2, int par3, float par4, byte par5, int par6)
    {
        int i = (int)((double)par4 + 0.61799999999999999D);
        byte byte0 = otherCoordPairs[par5];
        byte byte1 = otherCoordPairs[par5 + 3];
        int ai[] =
        {
            par1, par2, par3
        };
        int ai1[] =
        {
            0, 0, 0
        };
        int j = -i;
        int k = -i;
        ai1[par5] = ai[par5];

        for (; j <= i; j++)
        {
            ai1[byte0] = ai[byte0] + j;

            for (int l = -i; l <= i;)
            {
                double d = Math.sqrt(Math.pow((double)Math.abs(j) + 0.5D, 2D) + Math.pow((double)Math.abs(l) + 0.5D, 2D));

                if (d > (double)par4)
                {
                    l++;
                }
                else
                {
                    ai1[byte1] = ai[byte1] + l;
                    int i1 = worldObj.getBlockId(ai1[0], ai1[1], ai1[2]);

                    // FCMOD: Line changed
                    //if (i1 != 0 && i1 != 18)
                    if(!worldObj.isAirBlock(ai1[0], ai1[1], ai1[2]) && i1 != 18)
                	// END FCMOD
                    {
                        l++;
                    }
                    else
                    {
                        setBlockAndMetadata(worldObj, ai1[0], ai1[1], ai1[2], par6, 0);
                        l++;
                    }
                }
            }
        }
    }

    /**
     * Checks a line of blocks in the world from the first coordinate to triplet to the second, returning the distance
     * (in blocks) before a non-air, non-leaf block is encountered and/or the end is encountered.
     */
    @Override
    int checkBlockLine(int par1ArrayOfInteger[], int par2ArrayOfInteger[])
    {
        int ai[] =
        {
            0, 0, 0
        };
        byte byte0 = 0;
        int i = 0;

        for (; byte0 < 3; byte0++)
        {
            ai[byte0] = par2ArrayOfInteger[byte0] - par1ArrayOfInteger[byte0];

            if (Math.abs(ai[byte0]) > Math.abs(ai[i]))
            {
                i = byte0;
            }
        }

        if (ai[i] == 0)
        {
            return -1;
        }

        byte byte1 = otherCoordPairs[i];
        byte byte2 = otherCoordPairs[i + 3];
        byte byte3;

        if (ai[i] > 0)
        {
            byte3 = 1;
        }
        else
        {
            byte3 = -1;
        }

        double d = (double)ai[byte1] / (double)ai[i];
        double d1 = (double)ai[byte2] / (double)ai[i];
        int ai1[] =
        {
            0, 0, 0
        };
        int j = 0;
        int k = ai[i] + byte3;

        do
        {
            if (j == k)
            {
                break;
            }

            ai1[i] = par1ArrayOfInteger[i] + j;
            ai1[byte1] = MathHelper.floor_double((double)par1ArrayOfInteger[byte1] + (double)j * d);
            ai1[byte2] = MathHelper.floor_double((double)par1ArrayOfInteger[byte2] + (double)j * d1);
            int l = worldObj.getBlockId(ai1[0], ai1[1], ai1[2]);

            // FCMOD: Line changed
            // if (l != 0 && l != 18)
            if(!worldObj.isAirBlock(ai1[0], ai1[1], ai1[2]) && l != 18)
        	// END FCMOD
            {
                break;
            }

            j += byte3;
        }
        while (true);

        if (j == k)
        {
            return -1;
        }
        else
        {
            return Math.abs(j);
        }
    }

    /**
     * Returns a boolean indicating whether or not the current location for the tree, spanning basePos to to the height
     * limit, is valid.
     */
    @Override
    boolean validTreeLocation()
    {
        int ai[] =
        {
            basePos[0], basePos[1], basePos[2]
        };
        int ai1[] =
        {
            basePos[0], (basePos[1] + heightLimit) - 1, basePos[2]
        };
        int i = worldObj.getBlockId(basePos[0], basePos[1] - 1, basePos[2]);

        // FCMOD: Changed
        //if (i != 2 && i != 3)
        if ( !FCUtilsTrees.CanSaplingGrowOnBlock( worldObj, basePos[0], basePos[1] - 1, basePos[2] ) )
		// END FCMOD
        {
            return false;
        }

        int j = checkBlockLine(ai, ai1);

        if (j == -1)
        {
            return true;
        }

        if (j < 6)
        {
            return false;
        }
        else
        {
            heightLimit = j;
            return true;
        }
    }
}

