// FCMOD

package net.minecraft.src;

public class FCItemBlockMouldingAndDecorative extends FCItemBlockMoulding
{	
    public FCItemBlockMouldingAndDecorative( int iItemID )
    {
        super( iItemID );        
    }
    
    @Override
    public int getMetadata( int iItemDamage )
    {
		return iItemDamage;    	
    }
    
    @Override
    public String getUnlocalizedName( ItemStack itemStack )
    {
    	switch( itemStack.getItemDamage() )
    	{
    		case FCBlockMouldingAndDecorative.m_iSubtypeColumn:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("column").toString();
    			
    		case FCBlockMouldingAndDecorative.m_iSubtypePedestalUp:
    		case FCBlockMouldingAndDecorative.m_iSubtypePedestalDown:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("pedestal").toString();
    			
    		case FCBlockMouldingAndDecorative.m_iSubtypeTable:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("table").toString();
    			
			default:
				
				return super.getUnlocalizedName( itemStack );
    	}
    }    
}
