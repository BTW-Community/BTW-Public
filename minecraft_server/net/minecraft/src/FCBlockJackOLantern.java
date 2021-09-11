// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockJackOLantern extends Block
{
    protected FCBlockJackOLantern( int iBlockID )
    {
    	super( iBlockID, Material.pumpkin );
    	
        setTickRandomly(true);
        
        setHardness(1.0F);
        SetBuoyant();
        
        setStepSound(soundWoodFootstep);
        setLightValue(1.0F);
        setUnlocalizedName("litpumpkin");
        
        setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public void onBlockPlacedBy( World world, int i, int j, int k, EntityLiving placingEntity, ItemStack itemStack )
    {
        int iFacing = MathHelper.floor_double( ( (double)placingEntity.rotationYaw * 4D / 360D ) + 2.5D ) & 3;
        
        int iMetadata = ( world.getBlockMetadata( i, j, k ) ) & (~3);
        
        iMetadata |= iFacing;
        
        world.SetBlockMetadataWithNotify( i, j, k, iMetadata, 2 );
    }

	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        super.onBlockAdded( world, i, j, k );

        int iMetadata = ( world.getBlockMetadata( i, j, k ) ) | 8; // set 4th bit to indicate block is player placed
        
        world.SetBlockMetadataWithNotify( i, j, k, iMetadata, 2 );
        
    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    }
	
	@Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
		CheckForExtinguish( world, i, j, k );
    }
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {
		CheckForExtinguish( world, i, j, k );
    }	
	
    @Override
    public boolean GetCanBlockLightItemOnFire( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
	public int RotateMetadataAroundJAxis( int iMetadata, boolean bReverse )
	{
		int iDirection = iMetadata & 3;
		
		if ( bReverse )
		{
			iDirection++;
			
			if ( iDirection > 3 )
			{
				iDirection = 0;
			}
		}
		else
		{
			iDirection--;
			
			if ( iDirection < 0 )
			{
				iDirection = 3;
			}
		}		
		
		return ( iMetadata & (~3) ) | iDirection;
	}
    
    @Override
    public boolean CanBeGrazedOn( IBlockAccess blockAccess, int i, int j, int k, 
    	EntityAnimal animal )
    {
		return animal.CanGrazeOnRoughVegetation();
    }

    //------------- Class Specific Methods ------------//
    
	private void CheckForExtinguish( World world, int i, int j, int k )
	{
		int iMetadata = world.getBlockMetadata( i, j, k );
		
		if ( ( iMetadata & 8 ) != 0 )
		{
			if ( HasWaterToSidesOrTop( world, i, j, k ) )
			{
				ExtinguishLantern( world, i, j, k );
			}
		}
	}
	
	private void ExtinguishLantern( World world, int i, int j, int k )
	{
		int iMetadata = world.getBlockMetadata( i, j, k );
		
		world.setBlockAndMetadataWithNotify( i, j, k, Block.pumpkin.blockID, iMetadata & 3 );
		
        world.playAuxSFX( FCBetterThanWolves.m_iFireFizzSoundAuxFXID, i, j, k, 0 );							        
	}
	
	//----------- Client Side Functionality -----------//
}
