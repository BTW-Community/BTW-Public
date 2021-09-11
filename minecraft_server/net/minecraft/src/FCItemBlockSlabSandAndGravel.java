// FCMOD

package net.minecraft.src;

public class FCItemBlockSlabSandAndGravel extends FCItemBlockSlab
{
    public FCItemBlockSlabSandAndGravel( int iItemID )
    {
        super( iItemID );        
    }

    @Override
    public int getMetadata( int iItemDamage )
    {
		return iItemDamage;    	
    }
    
    @Override
    public String getUnlocalizedName( ItemStack itemstack )
    {
    	switch( itemstack.getItemDamage() )
    	{
    		case FCBlockSlabSandAndGravel.m_iSubtypeSand:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("sand").toString();
                
			default:
				
				return super.getUnlocalizedName();
    	}
    }
    
    //------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
}
