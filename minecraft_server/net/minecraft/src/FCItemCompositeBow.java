// FCMOD

package net.minecraft.src;

public class FCItemCompositeBow extends FCItemBow
{
	private static int m_iMaxDamage =  576;
	
    public FCItemCompositeBow( int iItemID )
    {
        super( iItemID );
        
        setMaxDamage( m_iMaxDamage );
        
        setUnlocalizedName( "fcItemBowComposite" );
    }

    @Override
    public boolean CanItemBeFiredAsArrow( int iItemID )
    {    	
    	return iItemID == Item.arrow.itemID || iItemID == FCBetterThanWolves.fcItemRottenArrow.itemID || iItemID == FCBetterThanWolves.fcItemBroadheadArrow.itemID;
    }

    @Override
    public float GetPullStrengthToArrowVelocityMultiplier()
    {
    	return 3.0F;
    }
    
    @Override
    protected EntityArrow CreateArrowEntityForItem( World world, EntityPlayer player, int iItemID, float fPullStrength )
	{
		if ( iItemID == FCBetterThanWolves.fcItemBroadheadArrow.itemID )
		{
			return new FCEntityBroadheadArrow( world, player, fPullStrength * GetPullStrengthToArrowVelocityMultiplier() );
		}
		else if ( iItemID == FCBetterThanWolves.fcItemRottenArrow.itemID )
		{
	        world.playSoundAtEntity( player, "random.break", 0.8F, 0.8F + world.rand.nextFloat() * 0.4F);
	        
			if ( world.isRemote )
			{
		        float motionX = -MathHelper.sin((player.rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((player.rotationPitch / 180F) * (float)Math.PI) * fPullStrength;
		        float motionZ = MathHelper.cos((player.rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((player.rotationPitch / 180F) * (float)Math.PI) * fPullStrength;
		        float motionY = -MathHelper.sin((player.rotationPitch / 180F) * (float)Math.PI) * fPullStrength;
		        
		        for (int i = 0; i < 32; i++)
		        {
		        	// spew boat particles
		        	
		            world.spawnParticle( "iconcrack_333", player.posX, player.posY + player.getEyeHeight(), player.posZ, 
		        		motionX + (double)((float)(Math.random() * 2D - 1.0D) * 0.4F), 
		        		motionY + (double)((float)(Math.random() * 2D - 1.0D) * 0.4F), 
		        		motionZ + (double)((float)(Math.random() * 2D - 1.0D) * 0.4F) );
		        }
			}
			
			return null;
		}
		else
		{
			return super.CreateArrowEntityForItem( world, player, iItemID, fPullStrength );
		}
		
	}

    @Override
    protected void PlayerBowSound( World world, EntityPlayer player, float fPullStrength )
    {
    	world.playSoundAtEntity( player, "random.bow", 1.0F, 1.5F / (itemRand.nextFloat() * 0.4F + 1.2F) + fPullStrength * 0.5F );
    }
    
    //------------- Class Specific Methods ------------//
}