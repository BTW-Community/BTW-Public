// FCMOD

package net.minecraft.src;

public class FCUtilsMechPower
{
	static boolean IsBlockPoweredByAxleToSide( World world, int i, int j, int k, int iSide )
	{		
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );    		
		targetPos.AddFacingAsOffset( iSide );
		
		int iTargetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
		
		if ( IsBlockIDAxle( iTargetBlockID ) )
		{
			FCBlockAxle axleBlock = (FCBlockAxle)Block.blocksList[iTargetBlockID];
			
    		if ( axleBlock.IsAxleOrientedTowardsFacing(
    				world, targetPos.i, targetPos.j, targetPos.k, iSide ) )
    		{    				
    			if ( axleBlock.GetPowerLevel( world, targetPos.i, targetPos.j, targetPos.k ) > 0 )
    			{
    				return true;
    			}
    		}
		}
		
		return false;
	}
	
	static public boolean IsBlockIDAxle( int iBlockID )
	{
		return iBlockID == FCBetterThanWolves.fcBlockAxle.blockID || iBlockID == FCBetterThanWolves.fcBlockAxlePowerSource.blockID;
	}
	
	static public boolean DoesBlockHaveFacingAxleToSide( IBlockAccess blockAccess, int i, int j, int k, int iSide )
	{		
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );    		
		targetPos.AddFacingAsOffset( iSide );
		
		int iTargetBlockID = blockAccess.getBlockId( targetPos.i, targetPos.j, targetPos.k );
		
		if ( IsBlockIDAxle( iTargetBlockID ) )
		{
			FCBlockAxle axleBlock = (FCBlockAxle)Block.blocksList[iTargetBlockID];			
			
    		if ( axleBlock.IsAxleOrientedTowardsFacing(
    				blockAccess, targetPos.i, targetPos.j, targetPos.k, iSide ) )
    		{    				
				return true;
    		}
		}
		
		return false;
	}
	
	static public boolean DoesBlockHaveAnyFacingAxles( IBlockAccess blockAccess, int i, int j, int k )
	{
		for ( int iFacing = 0; iFacing <= 5; iFacing++ )
		{
			if ( DoesBlockHaveFacingAxleToSide( blockAccess, i, j, k, iFacing ) )
			{
				return true;
			}			
		}
		
		return false;
	}
	
	static public boolean IsBlockPoweredByHandCrank( World world, int i, int j, int k )
	{		
    	for ( int iFacing = 1; iFacing <= 5; iFacing++ )
    	{
    		if ( IsBlockPoweredByHandCrankToSide( world, i, j, k, iFacing ) )
    		{
    			return true;
    		}
    	}
    	
    	return false;    	
	}
	
	static public boolean IsBlockPoweredByHandCrankToSide( World world, int i, int j, int k, int iSide )
	{
		FCUtilsBlockPos targetPos = new FCUtilsBlockPos( i, j, k );    		
		targetPos.AddFacingAsOffset( iSide );
		
		int iTargetBlockID = world.getBlockId( targetPos.i, targetPos.j, targetPos.k );
		
		if ( iTargetBlockID == FCBetterThanWolves.fcHandCrank.blockID )
		{
    		Block targetBlock = Block.blocksList[iTargetBlockID];
    		
			FCIBlockMechanical device = (FCIBlockMechanical)targetBlock;
			
			if ( device.IsOutputtingMechanicalPower( world, targetPos.i, targetPos.j, targetPos.k ) )
			{
				return true;
			}
		}
		
		return false;
	}
	
	static public boolean IsBlockPoweredByAxle( World world, int i, int j, int k, FCIBlockMechanical block )
	{		
    	for ( int iFacing = 0; iFacing <= 5; iFacing++ )
    	{
    		if ( block.CanInputAxlePowerToFacing( world, i, j, k, iFacing ) )
    		{
        		if ( FCUtilsMechPower.IsBlockPoweredByAxleToSide( world, i, j, k, iFacing ) )
        		{
        			return true;
        		}
    		}
    	}
    	
		return false;
	}
	
	static public void DestroyHorizontallyAttachedAxles( World world, int i, int j, int k )
	{
		for ( int iFacing = 2; iFacing <= 5; iFacing ++ )
		{
			FCUtilsBlockPos tempPos = new FCUtilsBlockPos( i, j, k );
			
			tempPos.AddFacingAsOffset( iFacing );
			
			int iTempBlockID = world.getBlockId( tempPos.i, tempPos.j, tempPos.k );
			
			if ( IsBlockIDAxle( iTempBlockID ) )
			{
				FCBlockAxle axleBlock = (FCBlockAxle)Block.blocksList[iTempBlockID];
				
	    		if ( axleBlock.IsAxleOrientedTowardsFacing( world, tempPos.i, tempPos.j, tempPos.k, iFacing ) )
	    		{
	    			axleBlock.BreakAxle( world, tempPos.i, tempPos.j, tempPos.k );
	    		}
			}
		}
	}
	
	static public boolean IsPoweredGearBox( IBlockAccess blockAccess, int i, int j, int k )
	{
		int iTempBlockID = blockAccess.getBlockId( i, j, k );
		
		if ( iTempBlockID == FCBetterThanWolves.fcBlockRedstoneClutch.blockID ||
			iTempBlockID == FCBetterThanWolves.fcBlockGearBox.blockID )
		{
			FCBlockGearBox gearBlock = (FCBlockGearBox)Block.blocksList[iTempBlockID];
			
			return gearBlock.IsGearBoxOn( blockAccess, i, j, k );
		}
		
		return false;
	}
}
