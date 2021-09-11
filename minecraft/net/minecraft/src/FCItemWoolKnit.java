// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCItemWoolKnit extends Item
{
    public FCItemWoolKnit( int iItemID )
    {
    	super( iItemID );
    	
        setMaxDamage( 0 );
        setHasSubtypes( true );
        
        SetBuoyant();
        SetBellowsBlowDistance( 1 );
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.WOOL_KNIT );
    	SetFilterableProperties( Item.m_iFilterable_Thin );
        
    	setUnlocalizedName( "fcItemWoolKnit" );
    	
        setCreativeTab( CreativeTabs.tabMaterials );
    }
    
    @Override
    public String getItemDisplayName( ItemStack stack )
    {
        int iColor = MathHelper.clamp_int( stack.getItemDamage(), 0, 15 );
    	
        return ( "" + FCItemWool.m_sWoolColorNames[iColor] + " " + StringTranslate.getInstance().translateNamedKey( getLocalizedName( stack ) ) ).trim();
    }
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
    
    @Override
    public void getSubItems( int iItemID, CreativeTabs creativeTabs, List list )
    {
    	for ( int iColor = 0; iColor < 16; iColor++ )
    	{
    		list.add( new ItemStack( iItemID, 1, iColor ) );
    	}
    }
    
    @Override    
    public int getColorFromItemStack( ItemStack stack, int iRenderPass )
    {
    	return FCItemWool.m_iWoolColors[stack.getItemDamage()];
    }
}
