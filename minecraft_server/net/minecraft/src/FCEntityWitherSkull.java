// FCMOD

package net.minecraft.src;

public class FCEntityWitherSkull extends EntityWitherSkull
{
	private static final int m_iBlightSpreadRange = 3;
	
	private static final double m_dBlightSpreadRangeSq = 
		( (double)m_iBlightSpreadRange * (double)m_iBlightSpreadRange );
	
    public FCEntityWitherSkull( World world )
    {
        super( world );
    }

    public FCEntityWitherSkull( World world, EntityLiving shootingEntity, 
    	double dDirX, double dDirY, double dDirZ )
    {
        super( world, shootingEntity, dDirX, dDirY, dDirZ );
    }
    
    @Override
    public float func_82146_a( Explosion explosion, World par2World, int par3, int par4, int par5, Block par6Block)
    {
    	// returns the explosion resistance of a block when hit by this entity
    	
        float fResistance = par6Block.getExplosionResistance( this, par2World, par3, par4, par5 );

        // prevents damage to specific block types
        
        if ( IsHighExplosive() && 
        	par6Block != Block.bedrock && 
        	par6Block != Block.endPortal && 
        	par6Block != Block.endPortalFrame && 
        	par6Block != FCBetterThanWolves.fcSoulforgedSteelBlock )
        {
            fResistance = Math.min( 0.8F, fResistance );
        }

        return fResistance;
    }

    @Override
    protected void onImpact( MovingObjectPosition pos )
    {
        if ( !worldObj.isRemote )
        {
            if ( pos.entityHit != null )
            {
                if ( shootingEntity != null )
                {
                    if ( pos.entityHit.attackEntityFrom( 
                    	DamageSource.causeMobDamage( shootingEntity), 8 ) && 
                    	!pos.entityHit.isEntityAlive() )
                    {
                        shootingEntity.heal( 5 );
                    }
                }
                else
                {
                    pos.entityHit.attackEntityFrom( DamageSource.magic, 5 );
                }

                if ( pos.entityHit instanceof EntityLiving )
                {
                    int iDamage = 5;

                    if (this.worldObj.difficultySetting == 2)
                    {
                        iDamage = 10;
                    }
                    else if (this.worldObj.difficultySetting == 3)
                    {
                        iDamage = 40;
                    }

                    ((EntityLiving)pos.entityHit).addPotionEffect( 
                    	new PotionEffect( Potion.wither.id, 20 * iDamage, 1 ) );
                }
            }

            worldObj.newExplosion( this, posX, posY, posZ, 1F, false, 
            	worldObj.getGameRules().getGameRuleBooleanValue( "mobGriefing" ) );
            
            if ( !IsHighExplosive() )
            {
            	SpreadBlightInArea();
            }
            
            setDead();
        }
    }
    
	//------------- Class Specific Methods ------------//
	
    protected boolean IsHighExplosive()
    {
    	// Wrapper function to make code a little cleaner.
        // isInvulnerable() is wrongly named and instead represents the specific
        // skull type the wither has launched
    	
    	return isInvulnerable();
    }
    
    protected void SpreadBlightInArea()
    {
    	int iCenterI = MathHelper.floor_double( posX );
    	int iCenterJ = MathHelper.floor_double( posY );
    	int iCenterK = MathHelper.floor_double( posZ );

    	for ( int iTempI = iCenterI - m_iBlightSpreadRange; 
    		iTempI <= iCenterI + m_iBlightSpreadRange; iTempI++ )
    	{
        	for ( int iTempJ = iCenterJ - m_iBlightSpreadRange; 
    			iTempJ <= iCenterJ + m_iBlightSpreadRange; iTempJ++ )
	    	{
            	for ( int iTempK = iCenterK - m_iBlightSpreadRange; 
        			iTempK <= iCenterK + m_iBlightSpreadRange; iTempK++ )
	        	{
            		double dDeltaI = iTempI - iCenterI;
            		double dDeltaJ = iTempJ - iCenterJ;
            		double dDeltaK = iTempK - iCenterK;
            		
            		double dDistSq = ( dDeltaI * dDeltaI ) + ( dDeltaJ * dDeltaJ ) +
            			( dDeltaK * dDeltaK );
            		
            		if ( dDistSq <= m_dBlightSpreadRangeSq )
            		{
            			AttemptSpreadBlightToBlock( iTempI, iTempJ, iTempK );
            		}
	        	}
	    	}
    	}
    }
    
    protected void AttemptSpreadBlightToBlock( int i, int j, int k )
    {
        int iTargetBlockID = worldObj.getBlockId( i, j, k );                

        // FCTEST: Release as is.  Convert this and corresponding code in FCBlockAestheticOpaqueEarth
        // to use Block based system similar to grass and Mycellium
        
        if ( iTargetBlockID == Block.grass.blockID )
        {
            int iAboveTargetBlockID = worldObj.getBlockId( i, j + 1, k );
            
        	if ( Block.lightOpacity[iAboveTargetBlockID] <= 2 )
        	{                		
        		worldObj.setBlockAndMetadataWithNotify( i, j, k, 
        			FCBetterThanWolves.fcBlockAestheticOpaqueEarth.blockID, 
        			FCBlockAestheticOpaqueEarth.m_iSubtypeBlightLevel0 );
        	}
        }
    }
    
	//----------- Client Side Functionality -----------//
}