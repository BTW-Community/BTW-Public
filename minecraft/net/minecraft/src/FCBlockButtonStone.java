// FCMOD

package net.minecraft.src;

public class FCBlockButtonStone extends FCBlockButton
{
    protected FCBlockButtonStone( int iBlockID )
    {
        super( iBlockID, false );
        
        SetPicksEffectiveOn( true );
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public Icon getIcon( int iSide, int iMetadata )
    {
        return Block.stone.getBlockTextureFromSide( 1 );
    }
}
