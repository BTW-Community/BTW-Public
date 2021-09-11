// FCMOD

package net.minecraft.src;

public class FCMaterialSoulforgedSteel extends Material
{
    public FCMaterialSoulforgedSteel( MapColor mapColor )
    {
        super( mapColor );
        setImmovableMobility(); // same as obsidian
        setRequiresTool(); // can only be harvested if the tool in question is specifically coded for it
    }
}
