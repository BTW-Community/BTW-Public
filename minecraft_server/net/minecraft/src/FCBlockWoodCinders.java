// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockWoodCinders extends FCBlockFalling
{
	private static final int m_iChanceOfDisolvingInRain = 5;
	
    public FCBlockWoodCinders( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialLog );
        
        setHardness( 0.25F );
        
		SetAxesEffectiveOn();
		SetChiselsEffectiveOn();
		
        SetBuoyant();        
        
        setTickRandomly( true );
		
        setStepSound( soundGravelFootstep );
        
        setUnlocalizedName( "fcBlockWoodCinders" );        
    }
    
    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return 0;
    }
    
    @Override
    public boolean OnFinishedFalling( EntityFallingSand entity, float fFallDistance )
    {
    	World world = entity.worldObj;
    	
        if ( !world.isRemote )
        {
	        int i = MathHelper.floor_double( entity.posX );
	        int j = MathHelper.floor_double( entity.posY );
	        int k = MathHelper.floor_double( entity.posZ );
	        
	        if ( !FCBlockAshGroundCover.AttemptToPlaceAshAt( world, i, j, k ) &&
	        	!FCBlockAshGroundCover.AttemptToPlaceAshAt( world, i, j + 1, k ) )
	        {
	        	for ( int iTempCount = 0; iTempCount < 16; iTempCount++ )
	        	{
	        		int iTempI = i + world.rand.nextInt( 7 ) - 3 ;
	        		int iTempJ = j + world.rand.nextInt( 5 ) - 2 ;
	        		int iTempK = k + world.rand.nextInt( 7 ) - 3 ;
	        		
	        		if ( FCBlockAshGroundCover.AttemptToPlaceAshAt( world, iTempI, iTempJ, iTempK ) )
	        		{
	        			break;
	        		}
	        	}
	        }
        }
    	
    	// always destroyed on landing
        
    	return false;
    }
    
    @Override
    public void updateTick( World world, int i, int j, int k, Random rand ) 
    {
    	// prevent falling behavior for stumps
    	
    	if ( !GetIsStump( world, i, j, k ) )
    	{
    		super.updateTick( world, i, j, k, rand );
    	}
    }
    
    @Override
    public void RandomUpdateTick( World world, int i, int j, int k, Random rand )
    {
    	if ( rand.nextInt( m_iChanceOfDisolvingInRain ) == 0 )
    	{
        	if ( !GetIsStump( world, i, j, k ) )
        	{
		        if ( world.IsRainingAtPos( i, j + 1, k ) )
		        {
		        	world.setBlockWithNotify( i, j, k, 0 );
		        }
        	}
    	}
    }
    
    @Override
    public boolean CanConvertBlock( ItemStack stack, World world, int i, int j, int k )
    {
    	return GetIsStump( world, i, j, k );
    }
    
    @Override
    public boolean ConvertBlock( ItemStack stack, World world, int i, int j, int k, int iFromSide )
    {
    	if ( GetIsStump( world.getBlockMetadata( i, j, k ) ) )
    	{
	    	world.setBlockWithNotify( i, j, k, FCBetterThanWolves.fcBlockStumpCharred.blockID );
	    	
	    	return true;
    	}   
    	
    	return false;
    }
    
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	if ( GetIsStump( blockAccess.getBlockMetadata( i, j, k ) ) )
    	{
    		return 1000; // always convert, never harvest
    	}
    	
    	return super.GetHarvestToolLevel( blockAccess, i, j, k );
    }
    
    @Override
    public boolean GetDoesStumpRemoverWorkOnBlock( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetIsStump( blockAccess, i, j, k );
    }
    
    //------------- Class Specific Methods ------------//
    
    public boolean GetIsStump( IBlockAccess blockAccess, int i, int j, int k )
    {
		int iMetadata = blockAccess.getBlockMetadata( i, j, k );
		
		return GetIsStump( iMetadata );
    }
    
    public boolean GetIsStump( int iMetadata )
    {
    	return ( iMetadata & 8 ) != 0;
    }
    
    public int SetIsStump( int iMetadata, boolean bStump )
    {
    	if ( bStump )
    	{
    		iMetadata |= 8;
    	}
    	else
    	{
        	iMetadata &= ~8;
    	}
    	
    	return iMetadata;
    }
    
	//----------- Client Side Functionality -----------//    
}
