// FCMOD

package net.minecraft.src;

import java.util.Random;

public abstract class FCBlockTorchBaseUnlit extends FCBlockTorchBase
{
    protected FCBlockTorchBaseUnlit( int iBlockID )
    {
    	super( iBlockID );
    	
        setCreativeTab( null );
    }
    
	@Override
    public boolean GetCanBeSetOnFireDirectly( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return true;
    }
    
	@Override
    public boolean CanBeCrushedByFallingEntity( World world, int i, int j, int k, EntityFallingSand entity )
    {
    	return true;
    }
    
	@Override
    public int GetChanceOfFireSpreadingDirectlyTo( IBlockAccess blockAccess, int i, int j, int k )
    {
		return 60; // same chance as leaves and other highly flammable objects
    }
    
	@Override
    public boolean SetOnFireDirectly( World world, int i, int j, int k )
    {
		world.setBlockAndMetadataWithNotify( i, j, k, GetLitBlockID(), world.getBlockMetadata( i, j, k ) );
		
        world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
        	"mob.ghast.fireball", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F );
        
        return true;
    }
	
    @Override
    public boolean CanGroundCoverRestOnBlock( World world, int i, int j, int k )
    {
    	return world.doesBlockHaveSolidTopSurface( i, j - 1, k );
    }
    
    @Override
    public float GroundCoverRestingOnVisualOffset( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return -1F;        
    }
    
    //------------- Class Specific Methods ------------//    

	protected abstract int GetLitBlockID();
	
	//----------- Client Side Functionality -----------//
}
