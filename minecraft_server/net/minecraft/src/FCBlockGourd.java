// FCMOD

package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public abstract class FCBlockGourd extends FCBlockFalling
{
	private static final double m_dArrowSpeedSquaredToExplode = 1.10D;
	
    protected FCBlockGourd( int iBlockID )
    {
        super( iBlockID, Material.pumpkin );
        
        SetAxesEffectiveOn( true );
        SetBuoyant();
        
        setTickRandomly( true );        
        
        setCreativeTab( CreativeTabs.tabBlock );
    }
    
    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
    	super.updateTick( world, i, j, k, rand );
    	
    	// necessary to check blockID because super.updateTick may cause it to fall
    	
        if ( world.getBlockId( i, j, k ) == blockID ) 
        {
        	ValidateConnectionState( world, i, j, k );
        }
    }
    
    @Override
    public int getMobilityFlag()
    {
    	// allow gourds to be pushed by pistons
    	
    	return 0;
    }
    
    @Override
    public void OnArrowImpact( World world, int i, int j, int k, EntityArrow arrow )
    {
    	if ( !world.isRemote )
    	{
    		double dArrowSpeedSq = arrow.motionX * arrow.motionX + arrow.motionY * arrow.motionY + arrow.motionZ * arrow.motionZ;
    		
    		if ( dArrowSpeedSq >= m_dArrowSpeedSquaredToExplode )
    		{
	    		world.setBlockWithNotify( i, j, k, 0 );
	    		
	    		Explode( world, (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D );
    		}
    		else
    		{    		
    			world.playAuxSFX( FCBetterThanWolves.m_iMelonImpactSoundAuxFXID, i, j, k, 0 );
    		}
    	}
    }
    
    @Override
    public void OnBlockDestroyedWithImproperTool( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
    	// there's no improper tool to harvest gourds, but this also happens if the block is deleted after falling due to sitting on an
    	// improper block
    	
        world.playAuxSFX( AuxFXIDOnExplode(), i, j, k, 0 );
    }
    
    @Override
    public boolean OnFinishedFalling( EntityFallingSand entity, float fFallDistance )
    {
    	entity.metadata = 0; // reset stem connection
    	
    	if ( !entity.worldObj.isRemote )
    	{
	        int i = MathHelper.floor_double( entity.posX );
	        int j = MathHelper.floor_double( entity.posY );
	        int k = MathHelper.floor_double( entity.posZ );
	        
	        int iFallDistance = MathHelper.ceiling_float_int( entity.fallDistance - 5.0F );
	        
	    	if ( iFallDistance >= 0 )
	    	{	    		
	    		DamageCollidingEntitiesOnFall( entity, fFallDistance );
	    		
	    		if ( !Material.water.equals( entity.worldObj.getBlockMaterial( i, j, k ) ) )
	    		{	    			
		    		if ( entity.rand.nextInt( 10 ) < iFallDistance )
		    		{
		    			Explode( entity.worldObj, (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D );
		    			
		    			return false;
		    		}
	    		}
	    	}
	    	
			entity.worldObj.playAuxSFX( FCBetterThanWolves.m_iMelonImpactSoundAuxFXID, i, j, k, 0 );
    	}
        
    	return true;
    }    
    
    @Override
    public int AdjustMetadataForPistonMove( int iMetadata )
    {
    	// flag pushed pumpkins as not attached to a stem
    	
    	return iMetadata = 0;
    }
    
    @Override
	public boolean IsBlockAttachedToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
	{
        int iMetadata = blockAccess.getBlockMetadata( i, j, k );
        
        return iMetadata >= 2 && iFacing == iMetadata;
	}
	
    @Override
	public void AttachToFacing( World world, int i, int j, int k, int iFacing )
	{
    	if ( iFacing >= 2 && iFacing <= 5 )
    	{
    		world.setBlockMetadataWithClient( i, j, k, iFacing );
    	}
	}
    
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iBlockID )
    {
		super.onNeighborBlockChange( world, i, j, k, iBlockID );
		
		ValidateConnectionState( world, i, j, k );
    }
	
    @Override
    public boolean CanBeGrazedOn( IBlockAccess access, int i, int j, int k, EntityAnimal animal )
    {
		return animal.CanGrazeOnRoughVegetation();
    }

    //------------- Class Specific Methods ------------//
    
	abstract protected Item ItemToDropOnExplode();
	
	abstract protected int ItemCountToDropOnExplode();
	
	abstract protected int AuxFXIDOnExplode();
	
	abstract protected DamageSource GetFallDamageSource();	
	
    private void Explode( World world, double posX, double posY, double posZ )
    {
    	Item itemToDrop = ItemToDropOnExplode();
    	
    	if ( itemToDrop != null )
    	{
	        for (int iTempCount = 0; iTempCount < ItemCountToDropOnExplode(); iTempCount++)
	        {
	    		ItemStack itemStack = new ItemStack( itemToDrop, 1, 0 );
	
	        	double dItemX = posX + ( world.rand.nextDouble() - 0.5D ) * 2D;
	        	double dItemY = posY + 0.5D;
	        	double dItemZ = posZ + ( world.rand.nextDouble() - 0.5D ) * 2D;
	        	
	            EntityItem entityItem = new EntityItem( world, dItemX, dItemY, dItemZ, itemStack );
	            
	            entityItem.motionX = ( world.rand.nextDouble() - 0.5D ) * 0.5D;
	            entityItem.motionY = 0.2D + world.rand.nextDouble() * 0.3D;
	            entityItem.motionZ = ( world.rand.nextDouble() - 0.5D ) * 0.5D;
	            
	            entityItem.delayBeforeCanPickup = 10;
	            
	            world.spawnEntityInWorld( entityItem );
	        }
    	}
        
    	NotifyNearbyAnimalsFinishedFalling( world, MathHelper.floor_double( posX ), MathHelper.floor_double( posY ), MathHelper.floor_double( posZ ) );
    	
        world.playAuxSFX( AuxFXIDOnExplode(), 
    		MathHelper.floor_double( posX ), MathHelper.floor_double( posY ), MathHelper.floor_double( posZ ), 
    		0 );
    }
    
    private void DamageCollidingEntitiesOnFall( EntityFallingSand entity, float fFallDistance )
    {
        int var2 = MathHelper.ceiling_float_int( fFallDistance - 1.0F );

        if (var2 > 0)
        {
            ArrayList collisionList = new ArrayList( entity.worldObj.getEntitiesWithinAABBExcludingEntity( entity, entity.boundingBox ) );
            
            DamageSource source = GetFallDamageSource();
            
            Iterator iterator = collisionList.iterator();

            while ( iterator.hasNext() )
            {
                Entity tempEntity = (Entity)iterator.next();
                
                tempEntity.attackEntityFrom( source, 1 );
            }

        }
    }
    
    protected void ValidateConnectionState( World world, int i, int j, int k )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k );
    	
    	if ( iMetadata > 0 )
    	{
            FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );

            if ( iMetadata >= 2 && iMetadata <= 5 )
            {
	            targetPos.AddFacingAsOffset( iMetadata );
	            
	            int iTargetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
	            
	            // FCTODO: Hacky
	            if ( Block.blocksList[iTargetBlockID] == null || 
	            	!( Block.blocksList[iTargetBlockID] instanceof FCBlockStem ) ||
	            	world.getBlockMetadata( targetPos.i, targetPos.j, targetPos.k ) != 15 )
	            {            	
	                world.setBlockMetadata( i, j, k, 0 ); // no notify                
	            }
            }
            else
            {
                // There may be old gourds laying about that have invalid metadata
                
                world.setBlockMetadata( i, j, k, 0 ); // no notify                
            }
    	}
    }
    
	//----------- Client Side Functionality -----------//
}