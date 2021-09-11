// FCMOD

package net.minecraft.src;

public class FCMaterialCement extends Material
{
    public FCMaterialCement( MapColor mapcolor )
    {
        super(mapcolor);
        
        setReplaceable();
        setNoPushMobility();
    }
}