// FCMOD

package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;

public class FCEntityFallingBlock extends EntityFallingSand
{
	protected boolean m_bHasBlockBrokenOnLand = false;
	
	// copies of parent class variables due to being private	
	protected boolean m_bHurtsEntities = false;
	protected float m_fHurtsAmount = 2F;
	protected int m_fHurtsMaxDamage = 40;
	
    public FCEntityFallingBlock( World world, double dPosX, double dPosY, double dPosZ, int iBlockID, int iBlockMetadata )
    {
        super( world, dPosX, dPosY, dPosZ, iBlockID, iBlockMetadata );
    }
    
    @Override
    protected void readEntityFromNBT( NBTTagCompound tag )
    {
    	super.readEntityFromNBT( tag );
    	
        if ( tag.hasKey( "HurtEntities" ) )
        {
        	m_bHurtsEntities = tag.getBoolean( "HurtEntities" );
        	m_fHurtsAmount = tag.getFloat( "FallHurtAmount" );
        	m_fHurtsMaxDamage = tag.getInteger( "FallHurtMax" );
        }
        else if (this.blockID == Block.anvil.blockID)
        {
        	m_bHurtsEntities = true;
        }
    }
    
    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        
        fallTime++;
        
        motionY -= 0.03999999910593033D;
        
        moveEntity(this.motionX, this.motionY, this.motionZ);
        
        motionX *= 0.9800000190734863D;
        motionY *= 0.9800000190734863D;
        motionZ *= 0.9800000190734863D;

        if ( !worldObj.isRemote )
        {
            int i = MathHelper.floor_double( posX );
            int j = MathHelper.floor_double( posY );
            int k = MathHelper.floor_double( posZ );

            if ( fallTime == 1 )
            {
                if ( worldObj.getBlockId( i, j, k ) != blockID )
                {
                    setDead();
                    
                    return;
                }

                worldObj.setBlockToAir( i, j, k );
            }

            if ( onGround )
            {
                motionX *= 0.699999988079071D;
                motionZ *= 0.699999988079071D;
                motionY *= -0.5D;

                int iBlockBelowID = worldObj.getBlockId( i, j - 1, k );
                Block blockBelow = Block.blocksList[iBlockBelowID];
                
                if ( blockBelow != null && blockBelow.CanBeCrushedByFallingEntity( worldObj, i, j - 1, k, this ) )
                {
            		blockBelow.OnCrushedByFallingEntity( worldObj, i, j - 1, k, this );
            		
            		worldObj.setBlockToAir( i, j - 1, k );
                }
                else if ( worldObj.getBlockId( i, j, k ) != Block.pistonMoving.blockID )
                {
                    setDead();

                    if ( AttemptToReplaceBlockAtPosition( i, j, k ) )
                    {
                        Block.blocksList[this.blockID].onFinishFalling( worldObj, i, j, k, metadata );
                        
                        // FCNOTE: Tile entity handling has been removed here, since it is unused
                    }
                    else if ( shouldDropItem && !m_bHasBlockBrokenOnLand )
                    {
                    	int iDestinationBlockID = worldObj.getBlockId( i, j, k );
                    	
                    	if ( iDestinationBlockID != 0 )
                    	{
                    		if ( Block.blocksList[iDestinationBlockID].AttemptToCombineWithFallingEntity( worldObj, i, j, k, this ) )
                    		{
                    			return;
                    		}
                    	}
                    	
                    	Block.blocksList[blockID].OnBlockDestroyedLandingFromFall( worldObj, i, j, k, metadata );
                    }
                }
            }
            else if ( fallTime > 100 && ( j < 1 || j > 256 ) || fallTime > 600 )
            {
                if ( shouldDropItem )
                {
                    entityDropItem( new ItemStack( blockID, 1, Block.blocksList[blockID].damageDropped( metadata ) ), 0.0F );
                }

                setDead();
            }
        }
        
        if ( isEntityAlive() )
        {
        	Block.blocksList[this.blockID].OnFallingUpdate( this );
        }
    }
    
    @Override
    protected void fall( float fFallDistance )
    {
        if ( m_bHurtsEntities )
        {
            int iFallDamage = MathHelper.ceiling_float_int( fFallDistance - 1F );

            if ( iFallDamage > 0 )
            {
                ArrayList entityList = new ArrayList( worldObj.getEntitiesWithinAABBExcludingEntity( this, boundingBox ));
                
                DamageSource source = blockID == Block.anvil.blockID ? FCDamageSourceCustom.m_DamageSourceDeadWeight : 
                	DamageSource.fallingBlock;
                
                Iterator entityIterator = entityList.iterator();

                while ( entityIterator.hasNext() )
                {
                    Entity tempEntity = (Entity)entityIterator.next();
                    
                    tempEntity.attackEntityFrom( source, 
                    	Math.min( MathHelper.floor_float( (float)iFallDamage * m_fHurtsAmount ), m_fHurtsMaxDamage ) );
                }
            }
        }
        
    	Block block = Block.blocksList[blockID];
    	
    	if ( block != null )
    	{
    		if ( !block.OnFinishedFalling( this, fFallDistance ) )
    		{
    			m_bHasBlockBrokenOnLand = true;
    		}
    	}
    }
    
    @Override
    public void setIsAnvil( boolean bIsAnvil )
    {
    	super.setIsAnvil( bIsAnvil );
    	
    	m_bHurtsEntities = bIsAnvil;
    }
    
    //------------- Class Specific Methods ------------//
    
    private boolean AttemptToReplaceBlockAtPosition( int i, int j, int k )
    {
    	if ( !m_bHasBlockBrokenOnLand && 
    		CanReplaceBlockAtPosition( i, j, k ) && 
    		!Block.blocksList[this.blockID].CanFallIntoBlockAtPos(this.worldObj, i, j - 1, k ) )
    	{
        	Block destBlock = Block.blocksList[worldObj.getBlockId( i, j, k )];

        	if ( destBlock != null )
        	{
        		destBlock.OnCrushedByFallingEntity( worldObj, i, j, k, this );
        	}
        	
    		return worldObj.setBlock( i, j, k, this.blockID, this.metadata, 3 );
    	}
    	
    	return false;
    }
    
    private boolean CanReplaceBlockAtPosition( int i, int j, int k )
    {
    	if ( worldObj.canPlaceEntityOnSide( this.blockID, i, j, k, true, 1, (Entity)null, (ItemStack)null) )
    	{
    		return true;
    	}
    	
    	Block destBlock = Block.blocksList[worldObj.getBlockId( i, j, k )];
    	
    	if ( destBlock != null && destBlock.CanBeCrushedByFallingEntity( worldObj, i, j, k, this ) )
		{
    		return true;
		}
    	
    	return false;
    }
}
