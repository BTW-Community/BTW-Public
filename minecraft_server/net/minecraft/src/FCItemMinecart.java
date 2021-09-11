// FCMOD

package net.minecraft.src;

public class FCItemMinecart extends ItemMinecart
{
    public FCItemMinecart( int iItemID, int iMinecartType )
    {
    	super( iItemID, iMinecartType );    	
    }
    
    @Override
	public boolean OnItemUsedByBlockDispenser( ItemStack stack, World world, 
		int i, int j, int k, int iFacing )
	{
        FCUtilsBlockPos offsetPos = new FCUtilsBlockPos( 0, 0, 0, iFacing );
        
        double dXPos = i + ( offsetPos.i * 1.35D ) + 0.5D;
        double dYPos = j + offsetPos.j;
        double dZPos = k + ( offsetPos.k * 1.35D ) + 0.5D;
    	
        Entity entity = EntityMinecart.createMinecart( world, 
        	dXPos, dYPos, dZPos, ( (ItemMinecart)stack.getItem() ).minecartType );
        
        world.spawnEntityInWorld( entity );
        
        world.playAuxSFX( 1000, i, j, k, 0 ); // normal pitch click							        
        
		return true;
	}
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
