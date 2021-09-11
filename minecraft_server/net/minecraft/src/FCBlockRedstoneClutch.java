// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockRedstoneClutch extends FCBlockGearBox
{
	protected FCBlockRedstoneClutch( int iBlockID )
	{
        super( iBlockID );
        
        setUnlocalizedName( "fcBlockRedstoneClutch" );        
	}
	
	@Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
    	boolean bMechPowered = IsInputtingMechanicalPower( world, i, j, k );
    	
    	if ( bMechPowered )
    	{
    		// a Redstone powered gearbox outputs no mechanical power
    		
        	if ( world.isBlockGettingPowered( i, j, k ) || 
    			world.isBlockGettingPowered( i, j + 1, k ) )
        	{    	
        		bMechPowered = false;
        	}
    	}
    	
    	UpdateMechPoweredState( world, i, j, k, bMechPowered );
    }
    
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		super.DropComponentItemsOnBadBreak( world, i, j, k, iMetadata, fChanceOfDrop );
		
		DropItemsIndividualy( world, i, j, k, Item.goldNugget.itemID, 2, 0, fChanceOfDrop );
		
		return true;
	}
	
    @Override
    public boolean IsIncineratedInCrucible()
    {
    	return false;
    }
	
	//------------- Class Specific Methods ------------//
	
	protected boolean IsCurrentStateValid( World world, int i, int j, int k )
	{
    	boolean bMechPowered = IsInputtingMechanicalPower( world, i, j, k );
    	
    	if ( bMechPowered )
    	{
    		// a Redstone powered gearbox outputs no mechanical power
    		
        	if ( world.isBlockGettingPowered( i, j, k ) || 
        		world.isBlockGettingPowered( i, j + 1, k ) )
        	{    	
        		bMechPowered = false;
        	}
    	}
    	
    	return IsGearBoxOn( world, i, j, k ) == bMechPowered;
	}
	
	//----------- Client Side Functionality -----------//
}
