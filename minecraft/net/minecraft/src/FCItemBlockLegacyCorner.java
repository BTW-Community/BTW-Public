// FCMOD 

package net.minecraft.src;

public class FCItemBlockLegacyCorner extends ItemBlock
{
    public FCItemBlockLegacyCorner( int iItemID )
    {
        super( iItemID );
        
        setMaxDamage( 0 );
        setHasSubtypes(true);
        
        setUnlocalizedName( "fcCorner" );
    }

    @Override
    public int getMetadata( int iDamage )
    {
		return iDamage;
    }
    
    @Override    
    public float GetBuoyancy( int iItemDamage )
    {
    	if ( iItemDamage > 0 ) // stone corner
    	{
    		return -1.0F;
    	}
    	else
    	{
    		return super.GetBuoyancy( iItemDamage );
    	}
    }
    
	//----------- Client Side Functionality -----------//

    @Override
    public Icon getIconFromDamage( int iDamage ) 
    {
    	if ( iDamage > 0 )
    	{
    		return FCBetterThanWolves.fcBlockLegacySmoothstoneAndOakCorner.blockIcon;
    	}
    	else
    	{
    		return ((FCBlockLegacyCorner)FCBetterThanWolves.fcBlockLegacySmoothstoneAndOakCorner).m_IconWood;
    	}
    }
}