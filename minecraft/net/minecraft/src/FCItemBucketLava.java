// FCMOD

package net.minecraft.src;

public class FCItemBucketLava extends Item
{
	// deprecated item.  Only legacy support for any lingering items.
	
    public FCItemBucketLava( int iItemID )
    {
    	super( iItemID );
    	
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.LAVA_BUCKET );
    	
    	setUnlocalizedName( "bucketLava" );
	}
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
