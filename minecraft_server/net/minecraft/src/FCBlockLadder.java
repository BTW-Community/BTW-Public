// FCMOD

package net.minecraft.src;

public class FCBlockLadder extends FCBlockLadderBase
{
    protected FCBlockLadder( int iBlockID )
    {
        super( iBlockID );
        
        setUnlocalizedName( "fcBlockLadder" );
        
        setCreativeTab( CreativeTabs.tabDecorations );
    }

    @Override
    public boolean GetCanBeSetOnFireDirectly( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public boolean GetCanBeSetOnFireDirectlyByItem( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return false;
    }
    
    @Override
    public boolean SetOnFireDirectly( World world, int i, int j, int k )
    {
    	int iNewMetadata = FCBetterThanWolves.fcBlockLadderOnFire.SetFacing( 0, GetFacing( world, i, j, k ) ); 
		
    	world.setBlockAndMetadataWithNotify( i, j, k, FCBetterThanWolves.fcBlockLadderOnFire.blockID, iNewMetadata );
		
    	return true;
    }
    
    @Override
    public int GetChanceOfFireSpreadingDirectlyTo( IBlockAccess blockAccess, int i, int j, int k )
    {
		return 60; // same chance as leaves and other highly flammable objects
    }
    
    @Override
    public boolean CanBeCrushedByFallingEntity( World world, int i, int j, int k, EntityFallingSand entity )
    {
    	return true;
    }
    
    @Override
    public void OnCrushedByFallingEntity( World world, int i, int j, int k, EntityFallingSand entity )
    {
    	if ( !world.isRemote )
    	{
            dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );    		
    	}
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}
