// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCItemFlintAndSteel extends FCItemFireStarter
{
	private static final float m_fFlintAndSteelExhaustionPerUse = 0.01F;
	
    public FCItemFlintAndSteel( int iItemID )
    {
    	super( iItemID, 64, m_fFlintAndSteelExhaustionPerUse );
    }
    
    @Override
    protected boolean CheckChanceOfStart( ItemStack stack, Random rand )
    {
		return rand.nextInt( 4 ) == 0;
    }
    
    @Override
    protected void PerformUseEffects( EntityPlayer player )
    {
        player.playSound( "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F );
    }
	
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}
