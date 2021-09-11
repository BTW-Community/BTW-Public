// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCWorldGeneratorBonusBasket extends WorldGenerator
{
    public FCWorldGeneratorBonusBasket()
    {
    }

    public boolean generate( World world, Random rand, int i, int j, int k )
    {
        int iTempBlockID = world.getBlockId( i, j, k );
        
        while ( j > 1 && ( iTempBlockID == 0 || iTempBlockID == Block.leaves.blockID ) )
        {
        	j --;
        	iTempBlockID = world.getBlockId( i, j, k );
        }
        
        ++j;

        for ( int iTempCount = 0; iTempCount < 4; iTempCount++ )
        {
            int iTempI = i + rand.nextInt( 4 ) - rand.nextInt( 4 );
            int iTempJ = j + rand.nextInt( 3 ) - rand.nextInt( 3 );
            int iTempK = k + rand.nextInt( 4 ) - rand.nextInt( 4 );

            if ( world.isAirBlock( iTempI, iTempJ, iTempK ) && world.doesBlockHaveSolidTopSurface( iTempI, iTempJ - 1, iTempK ) )
            {
    	    	world.setBlock( iTempI, iTempJ, iTempK, FCBetterThanWolves.fcBlockBasketWicker.blockID, world.rand.nextInt( 4 ) | 4, 2 );
    	    	
    	    	FCTileEntityBasketWicker tileEntity = (FCTileEntityBasketWicker)world.getBlockTileEntity( iTempI, iTempJ, iTempK );
    	    	
    	    	if ( tileEntity != null )
    	    	{
    	    		tileEntity.SetStorageStack( new ItemStack( FCBetterThanWolves.fcItemGoldenDung ) );
    	    	}

                return true;
            }
        }

        return false;
    }
}