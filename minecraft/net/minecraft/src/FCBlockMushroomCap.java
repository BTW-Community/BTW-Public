// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockMushroomCap extends BlockMushroomCap
{
	protected final int m_iMushroomType; // copy of parent variable due to private visibility
	
    public FCBlockMushroomCap( int iBlockID, int iMushroomType )
    {
        super( iBlockID, Material.wood, iMushroomType );
        
        m_iMushroomType = iMushroomType;
        
        setHardness( 0.2F );
        
        setStepSound( soundWoodFootstep );
        
        SetBuoyant();
        
		SetFireProperties( FCEnumFlammability.HIGH );
		
        setUnlocalizedName( "mushroom" );
    }

    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
    	if ( m_iMushroomType != 0 )
    	{
    		return FCBetterThanWolves.fcItemMushroomRed.itemID;
    	}
    	
		return FCBetterThanWolves.fcItemMushroomBrown.itemID;
    }
    
    @Override
    public boolean CanMobsSpawnOn( World world, int i, int j, int k )
    {
    	// so that mobs don't appear uncontrollably in mushroom farms after a large mushroom
    	// pops up
    	
    	return false;
    }
    
	//------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
    
    @Override
    public int idPicked( World world, int i, int j, int k )
    {
        return idDropped( world.getBlockMetadata( i, j, k ), world.rand, 0 );
    }
}