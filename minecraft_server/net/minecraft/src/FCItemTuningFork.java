// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCItemTuningFork extends Item
{
    public static final String pitchNames[] =
    {
    	"0 - F#",
    	"1 - G",
    	"2 - G#",
    	"3 - A",
    	"4 - A#",
    	"5 - B",
    	"6 - C",
    	"7 - C#",
    	"8 - D",
    	"9 - D#",
    	"10 - E",
    	"11 - F",
    	"12 - F#",
    	"13 - G",
    	"14 - G#",
    	"15 - A",
    	"16 - A#",
    	"17 - B",
    	"18 - C",
    	"19 - C#",
    	"20 - D",
    	"21 - D#",
    	"22 - E",
    	"23 - F",
    	"24 - F#",
    };
    
    public FCItemTuningFork( int iItemID )
    {
    	super( iItemID );
    	
        setMaxDamage( 0 );
        setHasSubtypes( true );        

    	setUnlocalizedName( "fcItemTuningFork" );
    	
    	setCreativeTab( CreativeTabs.tabTools );
    }
    
    @Override
    public boolean onItemUse( ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )    
    {
    	byte note = (byte)itemStack.getItemDamage();
        float f = (float)Math.pow(2D, (double)(note - 12) / 12D);
        
        String s = "harp";

        Material material = world.getBlockMaterial( i, j, k );
        byte byte0 = 0;

        if (material == Material.rock)
        {
            byte0 = 1;
        }

        if (material == Material.sand)
        {
            byte0 = 2;
        }

        if (material == Material.glass)
        {
            byte0 = 3;
        }

        if (material == Material.wood)
        {
            byte0 = 4;
        }
        
        if (byte0 == 1)
        {
            s = "bd";
        }

        if (byte0 == 2)
        {
            s = "snare";
        }

        if (byte0 == 3)
        {
            s = "hat";
        }

        if (byte0 == 4)
        {
            s = "bassattack";
        }
        
        world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, (new StringBuilder()).append("note.").append(s).toString(), 3F, f);
        world.spawnParticle("note", (double)i + 0.5D, (double)j + 1.2D, (double)k + 0.5D, (double)note / 24D, 0.0D, 0.0D);
        
        return true;

    }
    
}  
