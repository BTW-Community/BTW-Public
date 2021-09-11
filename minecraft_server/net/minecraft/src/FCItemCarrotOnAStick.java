// FCMOD

package net.minecraft.src;

public class FCItemCarrotOnAStick extends ItemCarrotOnAStick
{
    public FCItemCarrotOnAStick( int iItemID )
    {
        super( iItemID );

		SetBuoyant();
        SetFilterableProperties( m_iFilterable_Narrow );
		
    	SetAsBasicPigFood();
        
        setUnlocalizedName( "carrotOnAStick" );
    }
    
    //------------- Class Specific Methods ------------//	
    
	//----------- Client Side Functionality -----------//
}
