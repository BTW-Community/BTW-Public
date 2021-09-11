// FCMOD

package net.minecraft.src;

public class FCBlockWorkbench extends BlockWorkbench
{
    protected FCBlockWorkbench( int iBlockID )
    {
        super( iBlockID );
        
    	SetBlockMaterial( FCBetterThanWolves.fcMaterialPlanks );
    	
        setHardness( 1.5F );
        
    	// Note that there is no appropriate tool to harvest this block
    	
        SetBuoyant();
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.WOOD_BASED_BLOCK );    	
        
        setStepSound( soundWoodFootstep );
        
        setUnlocalizedName( "workbench" );
        
        setCreativeTab( null ); 
    }
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fClickX, float fClickY, float fClickZ )
    {
		// prevent access if solid block above
		
		if ( FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j + 1, k, 0 ) )
		{
			return true;				
		}			
		
		return super.onBlockActivated( world, i, j, k, player, iFacing, fClickX, fClickY, fClickZ );
    }
	
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, 3, 0, fChanceOfDrop );
		DropItemsIndividualy( world, i, j, k, Item.stick.itemID, 1, 0, fChanceOfDrop );
		
		return true;
	}
	
    //------------- Class Specific Methods ------------//
	
	//----------- Client Side Functionality -----------//
}
