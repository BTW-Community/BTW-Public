// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCItemCandle extends FCItemPlacesAsBlock
{
    public static final int m_iCandeColors[] =
    {
    	0x101010, 0xb3312c, 0x3b511a, 0x51301a, 0x253192, 0x7b2fbe, 0x287697, 0x838383, 0x434343, 0xd88198,
        0x41cd34, 0xdecf2a, 0x6689d3, 0xc354cd, 0xeb8844, 0xffffff
    };
    
    public static final String[] m_sCandleColorNames = new String[] {
    	"Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "Light Gray", "Gray", "Pink", 
    	"Lime", "Yellow", "Light Blue", "Magenta", "Orange", "White"
	};
    
    public FCItemCandle( int iItemID )
    {
    	super( iItemID, FCBetterThanWolves.fcBlockCandle.blockID, 0, "fcItemCandle" );
    	
        setMaxDamage( 0 );
        setHasSubtypes( true );
        
        SetBuoyant();
    	
    	setCreativeTab( CreativeTabs.tabMaterials );
    }

    @Override
    public int getMetadata( int iItemDamage )
    {
    	return iItemDamage;
    }
    
    @Override
    public String getItemDisplayName( ItemStack stack )
    {
        int iColor = MathHelper.clamp_int( stack.getItemDamage(), 0, 15 );
    	
        return ( "" + m_sCandleColorNames[iColor] + " " + StringTranslate.getInstance().translateNamedKey( getLocalizedName( stack ) ) ).trim();
    }
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public void getSubItems( int iItemID, CreativeTabs creativeTabs, List list )
    {
    	for ( int iColor = 0; iColor < 16; iColor++ )
    	{
    		list.add( new ItemStack( iItemID, 1, iColor ) );
    	}
    }
    
    @Override    
    public int getColorFromItemStack( ItemStack itemStack, int iLayer )
    {
    	return m_iCandeColors[itemStack.getItemDamage()];
    }
}