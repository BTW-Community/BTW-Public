// FCMOD

package net.minecraft.src;

/**
 * Similar to MatherHelper, fills in a few additional functions
 */
public class FCUtilsMath
{
    public static double ClampDouble( double dValue, double dBottom, double dTop )
    {
    	if ( dValue < dBottom )
    	{
    		return dBottom;
    	}
    	else if ( dValue > dTop )
    	{
    		return dTop;
    	}
    	
    	return dValue;
    }
    
    public static double ClampDoubleTop( double dValue, double dTop )
    {
    	if ( dValue > dTop )
    	{
    		return dTop;
    	}
    	
    	return dValue;
    }
    
    public static double ClampDoubleBottom( double dValue, double dBottom )
    {
    	if ( dValue < dBottom )
    	{
    		return dBottom;
    	}
    	
    	return dValue;
    }
    
    public static double AbsDouble( double dValue )
    {
    	if ( dValue >= 0D )
    	{
    		return dValue;
    	}
    	
    	return -dValue;
    }
}
