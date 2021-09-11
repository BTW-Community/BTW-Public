// FCMOD

package net.minecraft.src;

public class FCBlockDeadBush extends BlockDeadBush
{
    protected static final double m_dWidth = 0.8D;
    protected static final double m_dHalfWidth = ( m_dWidth / 2D );
    
    protected FCBlockDeadBush( int iBlockID )
    {
    	super( iBlockID );
    	
    	setHardness( 0F );
    	
    	SetBuoyant();
    	
        InitBlockBounds( 
        	0.5D - m_dHalfWidth, 0D, 0.5D - m_dHalfWidth, 
        	0.5D + m_dHalfWidth, 0.8D, 0.5D + m_dHalfWidth);
        
    	setStepSound( soundGrassFootstep );
    	
    	setUnlocalizedName("deadbush");    	
    }
    
    @Override
    public boolean CanSpitWebReplaceBlock( World world, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public boolean IsReplaceableVegetation( World world, int i, int j, int k )
    {
    	return true;
    }
	
    @Override
    public boolean CanBeGrazedOn( IBlockAccess blockAccess, int i, int j, int k, 
    	EntityAnimal animal )
    {
		return animal.CanGrazeOnRoughVegetation();
    }
    
    @Override
    protected boolean CanGrowOnBlock( World world, int i, int j, int k )
    {
    	return world.getBlockId( i, j, k ) == Block.sand.blockID;
    }
    
    //------------- Class Specific Methods ------------//

	//----------- Client Side Functionality -----------//    
}
    
