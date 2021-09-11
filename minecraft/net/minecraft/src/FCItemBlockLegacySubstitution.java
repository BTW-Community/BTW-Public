// FCMOD

package net.minecraft.src;

public class FCItemBlockLegacySubstitution extends ItemBlock
{
	protected int m_iSubstituteBlockID;
	
    public FCItemBlockLegacySubstitution( int iItemID, int iSubstituteBlockID )
    {
    	super( iItemID );
    	
    	m_iSubstituteBlockID = iSubstituteBlockID;
    }
    
    @Override
    public int GetBlockIDToPlace( int iItemDamage, int iFacing, float fClickX, float fClickY, float fClickZ )
    {
    	return m_iSubstituteBlockID;
    }
    
    @Override
    public String getItemDisplayName( ItemStack stack )
    {
        return "Old " + super.getItemDisplayName( stack );
    }    
}
