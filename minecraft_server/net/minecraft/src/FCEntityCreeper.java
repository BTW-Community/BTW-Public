// FCMOD

package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class FCEntityCreeper extends EntityCreeper
{
    private static final int m_iNeuteredStateDataWatcherID = 25;

    private boolean m_bDeterminedToExplode = false;

    public FCEntityCreeper( World world )
    {
    	super( world );
    	
        tasks.RemoveAllTasksOfClass( EntityAICreeperSwell.class );
        tasks.RemoveAllTasksOfClass( EntityAIWander.class );
        
        tasks.addTask( 2, new FCEntityAICreeperSwell( this ) );
        tasks.addTask( 5, new FCEntityAIWanderSimple( this, 0.2F ) );
    }
    
    @Override
    protected void fall(float par1)
    {
    	// Parent function skipped to get rid of decreased fuse time when falling on the player
    	
        EntityLivingFall(par1);
    }
    
    @Override
    protected void entityInit()
    {
        super.entityInit();
        
        dataWatcher.addObject( m_iNeuteredStateDataWatcherID, new Byte( (byte)0 ) );
    }
    
    @Override
    public void writeEntityToNBT( NBTTagCompound tag )
    {
        super.writeEntityToNBT( tag );

        tag.setByte( "fcNeuteredState", (byte)GetNeuteredState() );
    }

    @Override
    public void readEntityFromNBT( NBTTagCompound tag )
    {
        super.readEntityFromNBT(tag);
        
        if ( tag.hasKey( "fcNeuteredState" ) )
        {
        	SetNeuteredState( (int)tag.getByte( "fcNeuteredState" ) );
        }        
    }
    
    @Override
    public void onDeath( DamageSource source )
    {
    	if ( GetNeuteredState() == 0 )
    	{
    		super.onDeath(source);
    	}
    	else
    	{
    		// neutered creepers skip over parent method so as not to drop records
    		
    		EntityLivingOnDeath( source );
    	}
    }

    @Override
    protected int getDropItemId()
    {
        return FCBetterThanWolves.fcItemNitre.itemID;
    }
    
    @Override
    protected void dropHead()
    {
        entityDropItem(new ItemStack( Item.skull.itemID, 1, 4 ), 0.0F);
    }
    
    @Override
    protected void dropFewItems( boolean bKilledByPlayer, int iFortuneModifier )
    {
        super.dropFewItems( bKilledByPlayer, iFortuneModifier );

        if ( GetNeuteredState() == 0 )
        {
	        if ( ( rand.nextInt( 3 ) == 0 || rand.nextInt( 1 + iFortuneModifier ) > 0 ) )
	        {
	            dropItem( FCBetterThanWolves.fcItemCreeperOysters.itemID, 1 );
	        }
        }
    }
    
    @Override
    public boolean interact( EntityPlayer player )
    {
        ItemStack playersCurrentItem = player.inventory.getCurrentItem();

        if ( playersCurrentItem != null && playersCurrentItem.itemID == Item.shears.itemID && GetNeuteredState() == 0 )
        {
            if ( !worldObj.isRemote )
            {
                SetNeuteredState( 1 );
                
                EntityItem oysterItem = entityDropItem( new ItemStack( FCBetterThanWolves.fcItemCreeperOysters, 1 ), 0.25F);
                
                oysterItem.motionY += (double)(rand.nextFloat() * 0.025F);
                oysterItem.motionX += (double)((rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                oysterItem.motionZ += (double)((rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                
                int i = MathHelper.floor_double( posX );
                int j = MathHelper.floor_double( posY );
                int k = MathHelper.floor_double( posZ );
                
		        worldObj.playAuxSFX( FCBetterThanWolves.m_iCreeperNeuteredAuxFXID, i, j, k, 0 );
            }

            playersCurrentItem.damageItem( 10, player );

            if ( playersCurrentItem.stackSize <= 0 )
            {
            	player.inventory.mainInventory[player.inventory.currentItem] = null;
            }            
            
            return true;
        }

        return super.interact(player);
    }

    @Override
    public int getTalkInterval()
    {
        return 120;
    }
    
    @Override
    public void playLivingSound()
    {
		if ( GetNeuteredState() > 0 )
		{
	        String var1 = this.getLivingSound();
	
	        if (var1 != null)
	        {
	            this.playSound(var1, 0.25F, this.getSoundPitch() + 0.25F);
	        }
		}
		else
		{
			super.playLivingSound();
		}
    }
    
    @Override
    protected String getLivingSound()
    {
		if ( GetNeuteredState() > 0 )
		{
			return "mob.creeper.say";
		}
		
		return super.getLivingSound();
    }
    
    @Override
    public void OnKickedByCow( FCEntityCow cow )
	{
    	m_bDeterminedToExplode = true;
	}
    
    @Override
    public void CheckForScrollDrop()
    {    	
    	if ( rand.nextInt( 1000 ) == 0 )
    	{
            ItemStack itemstack = new ItemStack( FCBetterThanWolves.fcItemArcaneScroll, 1, Enchantment.blastProtection.effectId );
            
            entityDropItem(itemstack, 0.0F);
    	}
    }
    
    @Override
    public void OnStruckByLightning( FCEntityLightningBolt entityBolt )
    {
        // create charged creeper
    	// intentionally not calling super method so that creeper isn't damaged by strike.
        
        dataWatcher.updateObject( 17, Byte.valueOf( (byte)1 ) );
    }
    
    //------------- Class Specific Methods ------------//
	
    public boolean GetIsDeterminedToExplode()
    {
    	return m_bDeterminedToExplode;
    }
    
    public int GetNeuteredState()
    {
        return (int)( dataWatcher.getWatchableObjectByte( m_iNeuteredStateDataWatcherID ) );
    }
    
    public void SetNeuteredState( int iNeuteredState )
    {
        dataWatcher.updateObject( m_iNeuteredStateDataWatcherID, Byte.valueOf( (byte)iNeuteredState ) );
    }
    
	//----------- Client Side Functionality -----------//
}