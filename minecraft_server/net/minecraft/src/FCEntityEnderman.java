// FCMOD

package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class FCEntityEnderman extends EntityEnderman
{	
    public static boolean[] m_bCarriableBlocks = new boolean[4096];
    
	// copies of private parent variables
    protected int m_iTeleportDelay = 0;
    protected int m_iAggressionCounter = 0; // field_70826_g in parent
    protected boolean m_bShouldBeScreaming; // field_104003_g in parent
	
    private static int m_iMaxEnstonePlaceWeight;
    private static final int m_iEndstonePlaceWeightPower = 8;
    private static int m_iEndstonePlacementNeighborWeights[];
    
    static
    {
    	m_bCarriableBlocks[Block.grass.blockID] = true;
    	m_bCarriableBlocks[Block.dirt.blockID] = true;
    	m_bCarriableBlocks[Block.sand.blockID] = true;
    	m_bCarriableBlocks[Block.gravel.blockID] = true;
    	m_bCarriableBlocks[Block.tnt.blockID] = true;
    	m_bCarriableBlocks[Block.cactus.blockID] = true;
    	m_bCarriableBlocks[Block.pumpkin.blockID] = true;
    	m_bCarriableBlocks[Block.melon.blockID] = true;
    	m_bCarriableBlocks[Block.mycelium.blockID] = true;
    	m_bCarriableBlocks[Block.wood.blockID] = true;
    	m_bCarriableBlocks[Block.netherrack.blockID] = true;
    }
    
    static
    {
    	m_iEndstonePlacementNeighborWeights = new int[7];
    	
    	for ( int iTemp = 0; iTemp < 7; iTemp++ )
    	{
    		m_iEndstonePlacementNeighborWeights[iTemp] = iTemp;
    		
    		for ( int iPower = 1; iPower < m_iEndstonePlaceWeightPower; iPower++ )
    		{
    			m_iEndstonePlacementNeighborWeights[iTemp] *= iTemp;
    		}
    	}
    	
    	m_iMaxEnstonePlaceWeight = m_iEndstonePlacementNeighborWeights[6];
    }    
    
    public FCEntityEnderman( World world )
    {
        super( world );
    }
    
    @Override
    protected void entityInit()
    {
        EntityCreatureEntityInit(); // skip parent method as we've changed the type of object 16
        
        dataWatcher.addObject( 16, new Integer( 0 ) );
        dataWatcher.addObject( 17, new Byte( (byte)0 ) );
        dataWatcher.addObject( 18, new Byte( (byte)0 ) );
    }
    
    @Override
    protected void dropFewItems( boolean bKilledByPlayer, int iLootingModifier )
    {
    	super.dropFewItems( bKilledByPlayer, iLootingModifier );
        
        DropCarriedBlock();
    }
    
    @Override
    public void setCarried( int iBlockID )
    {
        dataWatcher.updateObject( 16, Integer.valueOf( iBlockID ) );
    }
    
    @Override
    public int getCarried()
    {
        return dataWatcher.getWatchableObjectInt( 16 );
    }
    
    @Override
    protected Entity findPlayerToAttack()
    {
        EntityPlayer target = worldObj.getClosestVulnerablePlayerToEntity( this, 64D );
        
        if ( target != null && IsPlayerStaringAtMe( target ) )
        {
            m_bShouldBeScreaming = true;

            if ( m_iAggressionCounter == 0 )
            {
                worldObj.playSoundAtEntity( target, "mob.endermen.stare", 1F, 1F );
            }
            
            m_iAggressionCounter++;

            if ( m_iAggressionCounter > 5 )
            {
            	m_iAggressionCounter = 0;
                setScreaming( true );
                
                AngerNearbyEndermen( target );
                
                return target;
            }
        }
        else
        {
        	m_iAggressionCounter = 0;
        }

        return null;
    }

    @Override
    public void onLivingUpdate()
    {
    	moveSpeed = 0.3F;
    	
        if ( entityToAttack != null )
        {        	
        	moveSpeed = 6.5F;
        }
        
        if ( isWet() )
        {
            attackEntityFrom( DamageSource.drown, 1 );
        }

        if ( !worldObj.isRemote ) 
        {
        	if ( worldObj.getGameRules().getGameRuleBooleanValue( "mobGriefing" ) && 
        		entityToAttack == null )
        	{
	            if ( getCarried() == 0 )
	            {
	            	if ( !UpdateWithoutCarriedBlock() )
	            	{
	            		return;
	            	}
	            }
	            else if ( !UpdateWithCarriedBlock() )
	            {            	
	        		return;
	            }
        	}
        	
            if ( worldObj.isDaytime() )
            {
                float fBrightness = getBrightness( 1F );

                if ( fBrightness > 0.5F && 
                	worldObj.canBlockSeeTheSky( MathHelper.floor_double( posX ), 
	            		MathHelper.floor_double( posY ), 
	            		MathHelper.floor_double( posZ ) ) &&
        			rand.nextFloat() * 30F < ( fBrightness - 0.4F ) * 2F )
                {
                	Panic();
                }
            }
        }
        else
        {
        	EmitParticles();
        }

        if ( isWet() || isBurning() )
        {
        	Panic();
        }

        if ( isScreaming() && !m_bShouldBeScreaming && rand.nextInt(100) == 0)
        {
            setScreaming( false );
        }

        isJumping = false;

        if ( entityToAttack != null )
        {
            faceEntity( entityToAttack, 100F, 100F );
        }

        if ( !worldObj.isRemote && isEntityAlive() )
        {
            if ( entityToAttack != null )
            {
                if ( entityToAttack instanceof EntityPlayer && 
                	IsPlayerStaringAtMe( (EntityPlayer)entityToAttack ) )
                {
                    moveStrafing = moveForward = 0F;
                    moveSpeed = 0F;

                    if ( entityToAttack.getDistanceSqToEntity( this ) < 16D )
                    {
                        teleportRandomly();
                    }

                    m_iTeleportDelay = 0;
                }
                else if ( entityToAttack.getDistanceSqToEntity( this ) > 256D && 
                	m_iTeleportDelay++ >= 30 && teleportToEntity( entityToAttack ) )
                {
                    m_iTeleportDelay = 0;
                }
            }
            else
            {
                setScreaming( false );
                m_iTeleportDelay = 0;
            }
        }

        EntityMobOnLivingUpdate(); // intentionally skip super method
    }
    
    @Override
    public boolean attackEntityFrom( DamageSource source, int iDamage )
    {
        if ( !isEntityInvulnerable() )
        {
            setScreaming( true );

            if ( source instanceof EntityDamageSource && source.getEntity() instanceof EntityPlayer )
            {
            	m_bShouldBeScreaming = true;
            }

            if ( source instanceof EntityDamageSourceIndirect )
            {
            	m_bShouldBeScreaming = false;

                for ( int iTempCount = 0; iTempCount < 64; ++iTempCount )
                {
                    if ( teleportRandomly() )
                    {
                        return true;
                    }
                }

                return false;
            }
            else
            {
	            if ( source.getEntity() instanceof EntityPlayer )
	            {
	                boolean bResult = EntityMobAttackEntityFrom( source, iDamage );
	                
	                if ( isEntityAlive() )
	                {
	    	            for ( int iTempCount = 0; iTempCount < 64; iTempCount++ )
	    	            {
	    	            	if ( teleportRandomly() )
	    	            	{
	    	            		break;
	    	            	}
	    	            }
	                }
	                
	                AngerNearbyEndermen( (EntityPlayer)source.getEntity() );
	                
	                return bResult;
				}

                return EntityMobAttackEntityFrom(source, iDamage);
            }
        }
        
        return false;
    }

    @Override
    public void initCreature() 
    {
    	// this function handles creature specific initialization upon spawn
    	
        if ( worldObj.provider.dimensionId == 1 && worldObj.rand.nextInt( 5 ) == 0 )
    	{
        	setCarried( Block.whiteStone.blockID );
        	setCarryingData( 0 );
    	}
    }
    
    @Override
    public void CheckForScrollDrop()
    {    	
    	if ( rand.nextInt( 1000 ) == 0 )
    	{
            ItemStack itemstack = new ItemStack( FCBetterThanWolves.fcItemArcaneScroll, 1, Enchantment.silkTouch.effectId );
            
            entityDropItem(itemstack, 0.0F);
    	}
    }
    
    //------------- Class Specific Methods ------------//
    
    protected boolean IsPlayerStaringAtMe( EntityPlayer player )
    {
        ItemStack headStack = player.inventory.armorInventory[3];

        if ( headStack == null || headStack.itemID != 
        	FCBetterThanWolves.fcItemEnderSpectacles.itemID )
        {
            Vec3 vLook = player.getLook( 1F ).normalize();
            
            Vec3 vDelta = worldObj.getWorldVec3Pool().getVecFromPool( posX - player.posX, 
            	boundingBox.minY + ( height / 2F ) - ( player.posY + player.getEyeHeight() ), 
            	posZ - player.posZ );
            
            double dDist = vDelta.lengthVector();
            
            vDelta = vDelta.normalize();
            
            double dotDelta = vLook.dotProduct( vDelta );
            
            if ( dotDelta > 1D - 0.025D / dDist )
            {
            	return player.canEntityBeSeen( this );
            }
        }
        
        return false;
    }

    public void DropCarriedBlock()
    {
        int iCarriedBlockID = getCarried();
        
        if ( iCarriedBlockID != 0 )
        {
        	Block block = Block.blocksList[iCarriedBlockID];
        	
        	if ( block != null )
        	{
        		int iDamageDropped = block.damageDropped( getCarryingData() );
        		
	        	entityDropItem( new ItemStack( iCarriedBlockID, 1, iDamageDropped ), 0F );
	        	
		        setCarried( 0 );
		        setCarryingData( 0 );
        	}
        }
    }
    
    private boolean CanPickUpBlock( int i, int j, int k )
    {    	
    	int iBlockID = worldObj.getBlockId( i, j, k );
    	
        if ( m_bCarriableBlocks[iBlockID] )
        {
        	if ( !worldObj.isBlockNormalCube( i, j, k ) && iBlockID != Block.cactus.blockID )
        	{
        		// blocks like flowers and such can always be picked up regardless of surrounding blocks
        		
        		return true;
        	}
        	else
        	{
        		// check if the block is on a valid "corner" so that Endermen don't grab blocks that create odd deformities in the terrain
        		
        		int iNeighboringNonSolidBlocks = 0;
        		
        		if ( !DoesBlockBlockPickingUp( i, j - 1, k ) )
        		{
        			iNeighboringNonSolidBlocks++;
        		}
        		else
        		{
        			// we have a solid block beneath.  Check for others above (including at angles), so that blocks are removed to form slopes)
        			
        			for ( int iTempI = i - 1; iTempI <= i + 1; iTempI++ )
        			{
            			for ( int iTempK = k - 1; iTempK <= k + 1; iTempK++ )
            			{
            				if ( DoesBlockBlockPickingUp( iTempI, j + 1, iTempK ) )
            				{
            					return false;
            				}
            			}
        			}
        		}
        		
    			for ( int iFacing = 1; iFacing < 6; iFacing++ )
    			{
    				FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
    				
    				targetPos.AddFacingAsOffset( iFacing );
    				
    				if ( !DoesBlockBlockPickingUp( targetPos.i, targetPos.j, targetPos.k ) )
    				{
            			iNeighboringNonSolidBlocks++;        			
    				}
    			}
    			
    			if ( iNeighboringNonSolidBlocks >= 3 )
    			{
    				return true;
    			}
        	}
        }
        
    	return false;
    }
    
    private boolean DoesBlockBlockPickingUp( int i, int j, int k )
    {
    	if ( worldObj.isAirBlock( i, j, k ) )
    	{
    		return false;
    	}
    	else if ( worldObj.isBlockNormalCube( i, j, k ) )
    	{
    		return true;
    	}
    	
    	int iBlockID = worldObj.getBlockId( i, j, k );
    	
    	Block block = Block.blocksList[iBlockID];
    	
        return !( block == null || block == Block.waterMoving || block == Block.waterStill || 
        	block == Block.lavaMoving || block == Block.lavaStill || 
        	block == Block.fire || block == FCBetterThanWolves.fcBlockFireStoked || 
        	block.blockMaterial.isReplaceable() || block.blockMaterial == Material.plants || 
        	block.blockMaterial == Material.leaves );
    }
    
    /*
     * Returns false if the Enderman entity was set to dead during the update
     */
    private boolean UpdateWithCarriedBlock()
    {
    	int iCarriedBlockID = getCarried();
    	
    	if ( worldObj.provider.dimensionId == 1 ) 
    	{
			// we're in the end dimension with a block, and should attempt to place it
			
            int i = MathHelper.floor_double( posX ) + rand.nextInt( 5 ) - 2;
            int j = MathHelper.floor_double( posY ) + rand.nextInt( 7 ) - 3;
            int k = MathHelper.floor_double( posZ ) + rand.nextInt( 5 ) - 2;
            
			int iWeight = GetPlaceEndstoneWeight( i, j, k );
			
            if ( rand.nextInt( m_iMaxEnstonePlaceWeight >> 9 ) < iWeight )
            {
		        worldObj.playAuxSFX( FCBetterThanWolves.m_iEnderBlockPlaceAuxFXID, i, j, k, iCarriedBlockID + ( getCarryingData() << 12 ) );
		        
                worldObj.setBlockAndMetadataWithNotify( i, j, k, getCarried(), getCarryingData());
                
                setCarried(0);
            }
    	}
    	else
    	{
			// 	eventually the enderman should teleport away to the end with his block
			
    		if ( rand.nextInt( 2400 ) == 0 )
    		{
    			// play dimensional travel effects
    			
                int i = MathHelper.floor_double( posX );
                int j = MathHelper.floor_double( posY ) + 1;
                int k = MathHelper.floor_double( posZ );
                
		        worldObj.playAuxSFX( FCBetterThanWolves.m_iEnderChangeDimensionAuxFXID, i, j, k, 0 );
		        
                setDead();
                
                return false;
    		}
    	}
    	
    	return true;
    }
    
    /*
     * Returns false if the Enderman entity was set to dead during the update
     */
    private boolean UpdateWithoutCarriedBlock()
    {
        if ( rand.nextInt(20) == 0 )
        {
            int i = MathHelper.floor_double( ( posX - 3D ) + rand.nextDouble() * 6D );
            int j = MathHelper.floor_double( posY - 1D + rand.nextDouble() * 7D );
            int k = MathHelper.floor_double( ( posZ - 3D ) + rand.nextDouble() * 6D );
            
            int l1 = worldObj.getBlockId( i, j, k );

            if ( CanPickUpBlock( i, j, k ) )
            {
		        worldObj.playAuxSFX( FCBetterThanWolves.m_iEnderBlockCollectAuxFXID, i, j, k, l1 + ( worldObj.getBlockMetadata( i, j, k ) << 12 ) );
		        
                setCarried( worldObj.getBlockId( i, j, k ) );
                setCarryingData( worldObj.getBlockMetadata( i, j, k ) );
                worldObj.setBlockToAir( i, j, k );
            }
        }
        else if ( worldObj.provider.dimensionId == 1  )
        {
        	// Endermen in the end without a block in hand will eventually teleport back to the overworld
        	
    		if ( rand.nextInt( 9600 ) == 0 )
    		{
    			// play dimensional travel effects
    			
                int i = MathHelper.floor_double( posX );
                int j = MathHelper.floor_double( posY ) + 1;
                int k = MathHelper.floor_double( posZ );
                
		        worldObj.playAuxSFX( FCBetterThanWolves.m_iEnderChangeDimensionAuxFXID, i, j, k, 0 );
		        
                setDead();
                
                return false;
    		}
        }
        
        return true;
    }
    
    private int GetPlaceEndstoneWeight( int i, int j, int k )
    {
    	int iNumValidNeighbors = 0;
    	
    	if ( worldObj.isAirBlock( i, j, k ) )
    	{
    		if ( worldObj.doesBlockHaveSolidTopSurface( i, j - 1, k ) )
    		{
    			iNumValidNeighbors++;
    		}
    		
			for ( int iFacing = 1; iFacing < 6; iFacing++ )
			{
				FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );
				
				targetPos.AddFacingAsOffset( iFacing );
				
				int iTargetBlockID = worldObj.getBlockId( targetPos.i, targetPos.j, targetPos.k );
				
				if ( iTargetBlockID == Block.whiteStone.blockID )
				{
					// we can place endstone if there is endstone on any other side
					
					iNumValidNeighbors++;
				}
			}
    	}
    	
    	return m_iEndstonePlacementNeighborWeights[iNumValidNeighbors];
    }
    
    protected void AngerNearbyEndermen( EntityPlayer targetPlayer )
    {
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(32D, 32D, 32D));
        
        Iterator iterator = list.iterator();

        while ( iterator.hasNext() )
        {
            Entity tempEntity = (Entity)iterator.next();

            if ( tempEntity instanceof FCEntityEnderman )
            {
                FCEntityEnderman enderman = (FCEntityEnderman)tempEntity;
                
                if ( enderman.entityToAttack == null )
                {
                	enderman.entityToAttack = targetPlayer;
                	enderman.setScreaming(true);
                }
            }
        }
    }
    
    protected void EmitParticles()
    {
	    for ( int iTempCount = 0; iTempCount < 2; iTempCount++ )
	    {
	        worldObj.spawnParticle( "portal", 
	        	posX + ( rand.nextDouble() - 0.5D ) * width, 
	        	posY + rand.nextDouble() * height - 0.25D, 
	        	posZ + ( rand.nextDouble() - 0.5D ) * width, 
	        	( rand.nextDouble() - 0.5D ) * 2.0D, -rand.nextDouble(), 
	        	( rand.nextDouble() - 0.5D ) * 2.0D );
	    }
    }
    
    protected void Panic()
    {
        entityToAttack = null;
        setScreaming( false );
        m_bShouldBeScreaming = false;
        
        teleportRandomly();
    }
}
