// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockLavaPillow extends FCBlockFullBlock
{
    public FCBlockLavaPillow( int iBlockID )
    {
        super( iBlockID, Material.rock );
        
        setHardness( 0.8F );
        
        SetPicksEffectiveOn();
        SetChiselsEffectiveOn();
        
        setStepSound( soundGlassFootstep );
        
        setUnlocalizedName( "fcBlockLavaPillow" );        
    }
    
    @Override
    public int idDropped( int iMetaData, Random random, int iFortuneModifier )
    {
        return 0;
    }
    
    @Override
    public void breakBlock( World world, int i, int j, int k, int iBlockID, int iMetadata )
    {
    	super.breakBlock( world, i, j, k, iBlockID, iMetadata );
    	
    	if ( world.isAirBlock( i, j, k ) )
    	{
            world.playAuxSFX( FCBetterThanWolves.m_iFireFizzSoundAuxFXID, i, j, k, 0 );
            
            if ( !HasWaterToSidesOrTop( world, i, j, k ) )
            {
	    		int iDecayLevel = 1;
	    		
	    		world.setBlockAndMetadataWithNotify( i, j, k, Block.lavaMoving.blockID, iDecayLevel );
	    		
	    		iDecayLevel++;
	    		
	    		for ( int iFacing = 2; iFacing <= 5; iFacing++ )
	    		{
	    			FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k, iFacing );
	    			
	    			if ( world.isAirBlock( tempPos.i, tempPos.j, tempPos.k ) )
					{
	    	    		world.setBlockAndMetadataWithNotify( tempPos.i, tempPos.j, tempPos.k, 
	    	    			Block.lavaMoving.blockID, iDecayLevel );
					}
	    		}
            }
    	}
    }
    
    @Override
    protected boolean canSilkHarvest()
    {
    	return false;
    }
    
    @Override
    public boolean IsBlockDestroyedByBlockDispenser( int iMetadata )
    {
    	return true;
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}