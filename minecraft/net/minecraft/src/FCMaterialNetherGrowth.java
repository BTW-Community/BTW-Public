// FCMOD

package net.minecraft.src;

public class FCMaterialNetherGrowth extends Material
{
    public FCMaterialNetherGrowth( MapColor mapColor )
    {
        super( mapColor );
        setNoPushMobility(); // breaks on push by piston
    }
    
    public boolean blocksMovement()
    {
    	// so that fluid can overwrite block
    	
        return false;
    }
}
