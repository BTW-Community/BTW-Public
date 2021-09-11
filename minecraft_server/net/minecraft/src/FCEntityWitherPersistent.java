// FCMOD

package net.minecraft.src;

public class FCEntityWitherPersistent extends FCEntityWither
{
    public FCEntityWitherPersistent( World world )
    {
        super( world );
    }
    
    //------------- Class Specific Methods ------------//
    
    static public void SummonWitherAtLocation( World world, int i, int j, int k )
    {
    	// FCTEST: Change this to create new FCEntityWithPersistent. Release as is
        FCEntityWither wither = new FCEntityWither( world );
        
        wither.setLocationAndAngles( (double)i + 0.5D, (double)j - 1.45D, (double)k + 0.5D, 
        	0F, 0F );
        	
        wither.func_82206_m();
        
        world.spawnEntityInWorld( wither );

        world.playAuxSFX( FCBetterThanWolves.m_iWitherCreatedAuxFXID, i, j, k, 0 );
        
        FCUtilsWorld.GameProgressSetWitherHasBeenSummonedServerOnly();
    }    
}