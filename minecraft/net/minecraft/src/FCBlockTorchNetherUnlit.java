// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockTorchNetherUnlit extends FCBlockTorchBaseUnlit
{
    protected FCBlockTorchNetherUnlit( int iBlockID )
    {
    	super( iBlockID );    	
    	
    	setUnlocalizedName( "fcBlockTorchIdle" );
    }
    
	@Override
	protected int GetLitBlockID()
	{
		return FCBetterThanWolves.fcBlockTorchNetherBurning.blockID;
	}
	
    //------------- Class Specific Methods ------------//

	//----------- Client Side Functionality -----------//
}
    
