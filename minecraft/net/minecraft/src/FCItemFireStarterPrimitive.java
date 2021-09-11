// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCItemFireStarterPrimitive extends FCItemFireStarter
{
	private final float m_fBaseChance;
    private final float m_fMaxChance;
    private final float m_fChanceIncreasePerUse;
    
    private final float m_fChanceDecayPerTick = 0.00025F;
    private final long m_lDelayBeforeDecay = ( 2 * 20 ); // two seconds
    
    public FCItemFireStarterPrimitive( int iItemID, int iMaxUses, float fExhaustionPerUse, float fBaseChance, float fMaxChance, float fChanceIncreasePerUse )
    {
        super( iItemID, iMaxUses, fExhaustionPerUse );
        
    	m_fBaseChance = fBaseChance;
        m_fMaxChance = fMaxChance;
        m_fChanceIncreasePerUse = fChanceIncreasePerUse;
        
        SetBuoyant();
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.SHAFT );    	
    }
    
    @Override
    protected boolean CheckChanceOfStart( ItemStack stack, Random rand )
    {
    	boolean bReturnValue = false;
    	
        float fChance = stack.GetAccumulatedChance( m_fBaseChance );
        long lCurrentTime = FCUtilsWorld.GetOverworldTimeServerOnly();
        long lLastTime = stack.GetTimeOfLastUse();
        
        if ( lLastTime > 0 )
        {
        	if ( lCurrentTime > lLastTime )
        	{
        		long lDecayTime = ( lCurrentTime - lLastTime ) - m_lDelayBeforeDecay ;
        		
        		if ( lDecayTime > 0 )
        		{
	        		fChance -= (float)lDecayTime * m_fChanceDecayPerTick;
	        		
	        		if ( fChance < m_fBaseChance )
	        		{
	        			fChance = m_fBaseChance;
	        		}
        		}
        	}
        	else if ( lCurrentTime < lLastTime )
        	{
        		// do not reset chance if currentTime is the same as last time, in case use attempts
        		// stack up on the server
        		
        		fChance = m_fBaseChance;
        	}
        }
        
		if ( rand.nextFloat() <= fChance )
		{
			bReturnValue = true;
		}
		
		fChance += m_fChanceIncreasePerUse;
		
		if ( fChance > m_fMaxChance )
		{
			fChance = m_fMaxChance;
		}
		
		stack.SetAccumulatedChance( fChance );
		stack.SetTimeOfLastUse( lCurrentTime );
		
		return bReturnValue;
    }
    
    @Override
    protected void PerformUseEffects( EntityPlayer player )
    {
        player.playSound( "random.eat", 0.5F + 0.5F * (float)player.rand.nextInt(2), ( player.rand.nextFloat() * 0.25F ) + 1.75F );
        
        if ( player.worldObj.isRemote )
        {
	        for (int var3 = 0; var3 < 5; ++var3)
	        {
		        Vec3 var4 = player.worldObj.getWorldVec3Pool().getVecFromPool( ( (double)player.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D );
		        
		        var4.rotateAroundX( -player.rotationPitch * (float)Math.PI / 180.0F );
		        var4.rotateAroundY( -player.rotationYaw * (float)Math.PI / 180.0F );
		        
		        Vec3 var5 = player.worldObj.getWorldVec3Pool().getVecFromPool( ( (double)player.rand.nextFloat() - 0.5D) * 0.3D, (double)(-player.rand.nextFloat()) * 0.6D - 0.3D, 0.6D );
		        
		        var5.rotateAroundX( -player.rotationPitch * (float)Math.PI / 180.0F);
		        var5.rotateAroundY( -player.rotationYaw * (float)Math.PI / 180.0F);
		        
		        var5 = var5.addVector( player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
		        
		        player.worldObj.spawnParticle( "iconcrack_" + this.itemID, var5.xCoord, var5.yCoord, var5.zCoord, var4.xCoord, var4.yCoord + 0.05D, var4.zCoord);
	        }
        }
    }
	
    @Override
    protected boolean AttemptToLightBlock( ItemStack stack, World world, int i, int j, int k, int iFacing )
    {
    	if ( super.AttemptToLightBlock( stack, world, i, j, k, iFacing ) )
    	{
    		stack.SetAccumulatedChance( m_fBaseChance );

    		return true;
    	}
    	
    	return false;
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}
