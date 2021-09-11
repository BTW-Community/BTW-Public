// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockBBQ extends Block
{
    private final static int m_iTickRate = 4;
    
    public FCBlockBBQ( int iBlockID )
    {
        super( iBlockID, Material.rock );
        
        setHardness( 3.5F );
        setStepSound( Block.soundStoneFootstep );
        
        setUnlocalizedName( "fcBlockHibachi" );

        setTickRandomly( true );
        
        setCreativeTab( CreativeTabs.tabRedstone );
    }

	@Override
    public int tickRate( World world )
    {
        return m_iTickRate;
    }    

	@Override
    public void onBlockAdded(World world, int i, int j, int k)
    {
    	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
    }    
    
	@Override
    public void updateTick( World world, int i, int j, int k, Random random )
    {
        boolean bPowered = IsGettingPowered( world, i, j, k );
        
        if ( bPowered )
        {
        	if ( !IsLit( world, i, j, k ) )
        	{
        		Ignite( world, i, j, k );
        	}
        	else
            {
                // make sure the fire hasn't gone out from other sources, or that the block above hasn't become vacated,
                // if we're supposed to be lit
        		
        		int iBlockAboveID = world.getBlockId(i, j + 1, k);

            	if ( iBlockAboveID != Block.fire.blockID &&
        			iBlockAboveID != FCBetterThanWolves.fcBlockFireStoked.blockID )
            	{
            		if ( ShouldIgniteAbove( world, i, j, k ) )
            		{
        	            world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "mob.ghast.fireball", 1F, world.rand.nextFloat() * 0.4F + 0.8F );

                        world.setBlockWithNotify( i, j + 1, k, fire.blockID );
            		}
            	}
            }
        }
    	else
    	{
    		if ( IsLit( world, i, j, k ) )
    		{
    			Extinguish( world, i, j, k );    			
    		}
    		else
    		{
        		int iBlockAboveID = world.getBlockId( i, j + 1, k );

            	if ( iBlockAboveID == Block.fire.blockID ||
        			iBlockAboveID == FCBetterThanWolves.fcBlockFireStoked.blockID )
            	{
            		// we've got a fire burning above an unlit, unpowered BBQ.  
            		// It has probably been lit by the player, or spread from elsewhere, so
            		// put it out.

                    world.setBlockWithNotify( i, j + 1, k, 0 );            		
            	}
    		}
    	}
    }
    
	@Override
    public void RandomUpdateTick( World world, int i, int j, int k, Random rand )
    {
		if ( !IsCurrentStateValid( world, i, j, k ) )
		{
			// verify we have a tick already scheduled to prevent jams on chunk load
			
			if ( !world.IsUpdateScheduledForBlock( i, j, k, blockID ) )
			{
		        world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );        
			}
		}
    }
	
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iNeighborBlockID )
    {        
    	if ( !IsCurrentStateValid( world, i, j, k ) && 
    		!world.IsUpdatePendingThisTickForBlock( i, j, k, blockID ) )
		{
        	world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) );
		}
    }    

	@Override
    public boolean DoesExtinguishFireAbove( World world, int i, int j, int k )
    {
    	return !IsLit( world, i, j, k );
    }
    
	@Override
    public boolean DoesInfiniteBurnToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
    {
		if ( iFacing == 1 )
		{
	    	return IsLit( blockAccess, i, j, k );
		}
		
    	return false;
    }
    
    //------------- Class Specific Methods ------------//
    
    public boolean IsLit( IBlockAccess blockAccess, int i, int j, int k )
    {
        int iMetaData = blockAccess.getBlockMetadata(i, j, k);
        
    	return ( iMetaData & 4 ) > 0;
    }
    
    private void SetLitFlag( World world, int i, int j, int k )
    {
        int iMetaData = world.getBlockMetadata(i, j, k);
    	
        world.setBlockMetadataWithNotify( i, j, k, ( iMetaData | 4 ) );
    }

    private void ClearLitFlag( World world, int i, int j, int k )
    {    	
        int iMetaData = world.getBlockMetadata(i, j, k);
    	
        world.setBlockMetadataWithNotify( i, j, k, ( iMetaData & (~4) ) );
    }
    
    private boolean IsGettingPowered( World world, int i, int j, int k )
    {
        boolean bPowered = world.isBlockGettingPowered(i, j, k) || world.isBlockGettingPowered(i, j + 1, k);
        
        return bPowered;
    }
    
    private boolean ShouldIgniteAbove( World world, int i, int j, int k )
    {
    	return world.isAirBlock( i, j + 1, k ) || CanIncinerateBlock( world, i, j + 1, k );
    }
    
    private boolean CanIncinerateBlock( World world, int i, int j, int k )
    {
		Block targetBlock = Block.blocksList[world.getBlockId( i, j, k )];
		
		return targetBlock == null || targetBlock.GetCanBlockBeIncinerated( world, i, j, k );		
    }
    
    private void Ignite( World world, int i, int j, int k )
    {
        SetLitFlag( world, i, j, k );
        
        world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "mob.ghast.fireball", 1F, world.rand.nextFloat() * 0.4F + 1F );

        if ( ShouldIgniteAbove( world, i, j, k ) )
        {
    		world.setBlockWithNotify( i, j + 1, k, fire.blockID );
        }        
    }
    
    private void Extinguish( World world, int i, int j, int k )
    {
        ClearLitFlag( world, i, j, k );
        
        world.playSoundEffect( (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F );
        
        // check if there is an actual fire block above the BBQ
        
        boolean isFireAbove = world.getBlockId( i, j + 1, k ) == fire.blockID ||
        	world.getBlockId(i, j + 1, k) == FCBetterThanWolves.fcBlockFireStoked.blockID;
        
        if ( isFireAbove )
        {
        	// notify the fire to extinguish
        	
            world.setBlockWithNotify( i, j + 1, k, 0 );
        }        
    }
    
	public boolean IsCurrentStateValid( World world, int i, int j, int k )
	{
        boolean bPowered = IsGettingPowered( world, i, j, k );
        
    	if ( IsLit( world, i, j, k ) != bPowered )
    	{
			return false;
    	}
    	else if ( IsLit( world, i, j, k ) )
    	{
    		int iBlockAboveID = world.getBlockId(i, j + 1, k);

        	if ( iBlockAboveID != Block.fire.blockID &&
    			iBlockAboveID != FCBetterThanWolves.fcBlockFireStoked.blockID )
    		{
    			if ( ShouldIgniteAbove( world, i, j, k ) )
    			{
    				// the bbq is lit, there is no fire above, and the block above is flammable (or air)
    				// this requires a state update
    				
    				return false;
    			}
			}
    	}
    	
    	return true;
	}
	
	//----------- Client Side Functionality -----------//
	
    private Icon[] m_IconBySideArray = new Icon[6];

	@Override
    public void registerIcons( IconRegister register )
    {
        blockIcon = register.registerIcon( "stone" ); // for hit effects
        
        m_IconBySideArray[0] = register.registerIcon( "fcBlockHibachi_bottom" );
        m_IconBySideArray[1] = register.registerIcon( "fcBlockHibachi_top" );
        
        Icon sideIcon = register.registerIcon( "fcBlockHibachi_side" );
        
        m_IconBySideArray[2] = sideIcon;
        m_IconBySideArray[3] = sideIcon;
        m_IconBySideArray[4] = sideIcon;
        m_IconBySideArray[5] = sideIcon;
    }
    
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		return m_IconBySideArray[iSide];
    }
}