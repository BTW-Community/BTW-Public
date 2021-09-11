// FCMOD

package net.minecraft.src;

public class FCBlockButtonWood extends FCBlockButton
{
    protected FCBlockButtonWood( int iBlockID )
    {
        super( iBlockID, true );
        
        SetAxesEffectiveOn( true );        
        SetBuoyant();
    }

    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
    
    @Override
    public Icon getIcon( int iSide, int iMetadata )
    {
        return Block.planks.getBlockTextureFromSide(1);
    }
}
