// FCMOD

package net.minecraft.src;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class FCItemWool extends Item
{
    public static final int m_iWoolColors[] =
    {
    	0x101010, 0xb3312c, 0x3b511a, 0x51301a, 0x253192, 0x7b2fbe, 0x287697, 0x838383, 0x434343, 0xd88198,
        0x41cd34, 0xdecf2a, 0x6689d3, 0xc354cd, 0xeb8844, 0xffffff
    };
    
    public static final String[] m_sWoolColorNames = new String[] {
    	"Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "Light Gray", "Gray", "Pink", 
    	"Lime", "Yellow", "Light Blue", "Magenta", "Orange", "White"
	};
    
    static private List<List<Integer>> m_colorConversionArray = null;
    
    public FCItemWool( int iItemID )
    {
    	super( iItemID );
    	
        setMaxDamage( 0 );
        setHasSubtypes( true );
        
        SetBuoyant();
        SetBellowsBlowDistance( 1 );
        SetFurnaceBurnTime( FCEnumFurnaceBurnTime.KINDLING );
		SetFilterableProperties( m_iFilterable_Small | m_iFilterable_Thin );
        
    	setUnlocalizedName( "fcItemWool" );
    	
        setCreativeTab( CreativeTabs.tabMaterials );
    }
    
    @Override
    public String getItemDisplayName( ItemStack stack )
    {
        int iColor = MathHelper.clamp_int( stack.getItemDamage(), 0, 15 );
    	
        return ( "" + m_sWoolColorNames[iColor] + " " + StringTranslate.getInstance().translateNamedKey( getLocalizedName( stack ) ) ).trim();
    }
    
    //------------- Class Specific Methods ------------//
    
    static public int AverageWoolColorsInGrid( InventoryCrafting inventory )
    {
    	int iAverageColor = 0;
    	int iSumRed = 0;
    	int iSumGreen = 0;
    	int iSumBlue = 0;
    	int iWoolCount = 0;
    	
        for ( int iTempSlot = 0; iTempSlot < inventory.getSizeInventory(); ++iTempSlot )
        {
            ItemStack tempStack = inventory.getStackInSlot( iTempSlot );

            if ( tempStack != null )
            {
            	if ( tempStack.itemID == FCBetterThanWolves.fcItemWool.itemID ||
            		tempStack.itemID == FCBetterThanWolves.fcItemWoolKnit.itemID ) 
            	{
            		int iWoolColorIndex = MathHelper.clamp_int( tempStack.getItemDamage(), 0, 15 );
            		
            		int iWoolColor = FCItemWool.m_iWoolColors[iWoolColorIndex];
            		
            		iWoolCount++;
            		
            		iSumRed += ( iWoolColor >> 16 ) & 255;
            		iSumGreen += ( iWoolColor >> 8 ) & 255;
            		iSumBlue += iWoolColor & 255;
            		
            	}
            }
        }
        
        if ( iWoolCount > 0 )
        {
        	int iAverageRed = iSumRed / iWoolCount;
        	int iAverageGreen = iSumGreen / iWoolCount;
        	int iAverageBlue = iSumBlue / iWoolCount;
        	
        	iAverageColor = ( iAverageRed << 16 ) | ( iAverageGreen << 8 ) | iAverageBlue;
        	
        }
        
    	return iAverageColor;
    }
    
    static private void InitColorConversionArray()
    {
        m_colorConversionArray = new ArrayList<List<Integer>>( 16 );
        
        for ( int iTempIndex = 0; iTempIndex < 16; iTempIndex++ )
        {
        	List<Integer> tempColorList =  new LinkedList<Integer>();
        	
        	m_colorConversionArray.add( iTempIndex, tempColorList );
        	
        	tempColorList.add( m_iWoolColors[iTempIndex] );        	
        }
        
        // additional points to aid in coming up with reasonable conversions
        // these are the same colors that are hardcoded in recipes to result from blending dyes
        
        SetHardColorConversionPoint( 8, 0, 15 );
        
        SetHardColorConversionPoint( 9, 1, 15 );
        SetHardColorConversionPoint( 14, 1, 11 );
        
        SetHardColorConversionPoint( 9, 2, 10 );
        
        SetHardColorConversionPoint( 5, 4, 1 );
        SetHardColorConversionPoint( 6, 4, 2 );
        SetHardColorConversionPoint( 12, 4, 15 );
        
        SetHardColorConversionPoint( 13, 5, 9 );
        
        SetHardColorConversionPoint( 7, 8, 15 );        
    }
    
    static private void SetHardColorConversionPoint( int iToColorIndex, int iFromColorIndex1, int iFromColorIndex2 )
    {
		int iFromColor1 = FCItemWool.m_iWoolColors[iFromColorIndex1];
		int iFromColor2 = FCItemWool.m_iWoolColors[iFromColorIndex2];
		
		int iBlendedRed = ( ( ( iFromColor1 >> 16 ) & 255 ) + ( ( iFromColor2 >> 16 ) & 255 ) ) / 2;
		int iBlendedGreen = ( ( ( iFromColor1 >> 8 ) & 255 ) + ( ( iFromColor2 >> 8 ) & 255 ) ) / 2;
		int iBlendedBlue = ( ( iFromColor1 & 255 ) + ( iFromColor2 & 255 ) ) / 2;
		
    	int iBlendedColor = ( iBlendedRed << 16 ) | ( iBlendedGreen << 8 ) | iBlendedBlue;
		
		m_colorConversionArray.get( iToColorIndex ).add( iBlendedColor );
    }
    
    static public int GetClosestColorIndex( int iColor )
    {
    	int iClosestIndex = -1;
    	int iClosestColorDistanceSq = 0;
    	
    	int iColorRed = ( iColor >> 16 ) & 255;
    	int iColorGreen = ( iColor >> 8 ) & 255;
    	int iColorBlue = iColor & 255;
    	
    	if ( m_colorConversionArray == null )
    	{
    		InitColorConversionArray();
    	}
    	
    	if ( MathHelper.abs_int( iColorRed - iColorGreen ) > 5 || MathHelper.abs_int( iColorRed - iColorBlue ) > 5 ) // skip straight to grey scale if there isn't much difference between colors
    	{    		
	    	for ( int iTempIndex = 0; iTempIndex < 16; iTempIndex++ )
	    	{
	    		List<Integer> tempColorList = m_colorConversionArray.get( iTempIndex );
	    		
	            Iterator tempIterator = tempColorList.iterator();
	            
	            while ( tempIterator.hasNext() )
	            {
		    		int iTempColor = (Integer)tempIterator.next();;
		    		
		    		int iTempColorRed = ( iTempColor >> 16 ) & 255;
		    		int iTempColorGreen = ( iTempColor >> 8 ) & 255;
		    		int iTempColorBlue = iTempColor & 255;
		    		
		    		int iTempRedDelta = iTempColorRed - iColorRed;
		    		int iTempGreenDelta = iTempColorGreen - iColorGreen;
		    		int iTempBlueDelta = iTempColorBlue - iColorBlue;
		    		
		    		// weighting for better aproximation based on wikipedia article on color difference
		    		
		    		int iTempColorDistanceSq = 2 * iTempRedDelta * iTempRedDelta + 
		    			4 * iTempGreenDelta * iTempGreenDelta + 
		    			3 * iTempBlueDelta * iTempBlueDelta; 
		    		
		    		if ( iClosestIndex == -1 || iTempColorDistanceSq < iClosestColorDistanceSq )
		    		{
		    			iClosestIndex = iTempIndex;
		    			iClosestColorDistanceSq = iTempColorDistanceSq;
		    		}
	            }
	    	}
    	}
    	
    	if ( iClosestIndex == -1 || iClosestColorDistanceSq > 15000 )
    	{
    		// go gray scale if no match was found or if the distance to the closest match is too large
    		
    		int iColorTotal = iColorRed + iColorGreen + iColorBlue;
    		
    		if ( iColorTotal < 125 )
    		{
    			iClosestIndex = 0;
    		}
    		else if ( iColorTotal < 297 )
    		{
    			iClosestIndex = 8;
    		}
    		else if ( iColorTotal < 579 )
    		{
    			iClosestIndex = 7;
    		}
    		else
    		{
    			iClosestIndex = 15;
    		}
    	}
    	
    	return iClosestIndex;
    }
    
	//------------ Client Side Functionality ----------//
}