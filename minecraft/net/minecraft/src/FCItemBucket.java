// FCMOD

package net.minecraft.src;

public abstract class FCItemBucket extends FCItemPlacesAsBlock
{
    public FCItemBucket( int iItemID )
    {
    	super( iItemID );
        
    	setMaxStackSize( 1 );
    	
        setContainerItem( Item.bucketEmpty );
        
        setCreativeTab( CreativeTabs.tabMisc );    	
    }
    
    @Override
    public abstract int getBlockID(); // child classes must provide a block ID to place

    @Override
    public boolean onItemUse( ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )
    {
    	if ( DoesContextOverridePlacingAsBlock( stack, player, world, i, j, k, iFacing, 
    		fClickX, fClickY, fClickZ ) )
		{
    		// override normal block place behavior so it goes to onItemRightClick()
    		
    		return false;
		}
        
        return super.onItemUse(  stack, player, world, i, j, k, iFacing, fClickX, fClickY, fClickZ );
    }
    
    @Override
    public boolean IsMultiUsePerClick()
    {
    	// prevents stuff like milk buckets being automatically placed after being consumed
    	// by holding down right mouse button
    	
    	return false;
    }
    
	//------------- Class Specific Methods ------------//
	
    public boolean DoesContextOverridePlacingAsBlock( ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )
    {
    	// disabling manual placement of buckets for the time being as it was leading
    	// to people sneak-clicking them into lava.  Need a better system for handling this.
    	
    	return true;
    }
	
	//----------- Client Side Functionality -----------//
}
