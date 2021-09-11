// FCMOD

package net.minecraft.src;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public abstract class FCItemFireStarter extends Item
{
	private final float m_fExhaustionPerUse;
	
    public FCItemFireStarter( int iItemID, int iMaxUses, float fExhaustionPerUse )
    {
        super( iItemID );
        
        maxStackSize = 1;
        setMaxDamage( iMaxUses );        
    	m_fExhaustionPerUse = fExhaustionPerUse;
    	
        setCreativeTab(CreativeTabs.tabTools);
    }
    
    @Override
    public boolean onItemUse( ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )
    {
        if ( player.canPlayerEdit( i, j, k, iFacing, stack ) )
        {
    		PerformUseEffects( player );
    		
        	if ( !world.isRemote )
        	{        	    
                NotifyNearbyAnimalsOfAttempt( player );
                
                if ( CheckChanceOfStart( stack, world.rand ) )
                {
        			AttemptToLightBlock( stack, world, i, j, k, iFacing );
        		}        		
        	}

            player.addExhaustion( m_fExhaustionPerUse );
            
            stack.damageItem( 1, player );
            
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean GetCanItemStartFireOnUse( int iItemDamage )
    {
    	return true;
    }    
    
    //------------- Class Specific Methods ------------//
    
    protected abstract boolean CheckChanceOfStart( ItemStack stack, Random rand );
    
    protected void PerformUseEffects( EntityPlayer player )
    {
    }
    
    protected boolean AttemptToLightBlock( ItemStack stack, World world, int i, int j, int k, int iFacing )
    {
    	int iTargetBlockID = world.getBlockId( i, j, k );
    	Block targetBlock = Block.blocksList[iTargetBlockID];

    	if ( targetBlock != null && targetBlock.GetCanBeSetOnFireDirectlyByItem( world, i, j, k ) )
		{
    		if ( targetBlock.SetOnFireDirectly( world, i, j, k ) )
    		{            	            
    			return true;
    		}
		}
    	
    	return false;
    }
    
    protected boolean AttemptToLightBlockRegardlessOfFlamability( ItemStack stack, World world, int i, int j, int k, int iFacing )
    {
    	// old way of setting absolutely anything on fire with a fire block.  Not used at present, but here for reference
    	
    	int iTargetBlockID = world.getBlockId( i, j, k );
    	Block targetBlock = Block.blocksList[iTargetBlockID];

    	FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
    	
    	if ( targetBlock == null || !targetBlock.GetCanBeSetOnFireDirectlyByItem( world, targetPos.i, targetPos.j, targetPos.k ) )
    	{
    		targetPos.AddFacingAsOffset( iFacing );
        	targetBlock = Block.blocksList[world.getBlockId( targetPos.i, targetPos.j, targetPos.k )];
        	iTargetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
    	}
    	
    	if ( targetBlock != null && targetBlock.GetCanBeSetOnFireDirectlyByItem( world, targetPos.i, targetPos.j, targetPos.k ) )
		{
    		if ( targetBlock.SetOnFireDirectly( world, targetPos.i, targetPos.j, targetPos.k ) )
    		{            	            
    			return true;
    		}
		}
    	else if ( world.isAirBlock( targetPos.i, targetPos.j, targetPos.k ) ) 
        {
            world.setBlockWithNotify( targetPos.i, targetPos.j, targetPos.k, Block.fire.blockID );
            
            world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "mob.ghast.fireball", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F );
            
			return true;
        }
    	
    	return false;
    }
    
	public void NotifyNearbyAnimalsOfAttempt( EntityPlayer player )
	{
        List animalList = player.worldObj.getEntitiesWithinAABB( EntityAnimal.class, player.boundingBox.expand( 6, 6, 6 ) );
	        
        Iterator itemIterator = animalList.iterator();

        while ( itemIterator.hasNext())
        {
    		EntityAnimal tempAnimal = (EntityAnimal)itemIterator.next();
    		
	        if ( !tempAnimal.isDead )
	        {
	        	tempAnimal.OnNearbyFireStartAttempt( player ); 
	        }	        
        }
	}
    
	//----------- Client Side Functionality -----------//
}
