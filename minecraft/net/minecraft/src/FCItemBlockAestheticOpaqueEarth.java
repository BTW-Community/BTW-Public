// FCMOD 

package net.minecraft.src;

public class FCItemBlockAestheticOpaqueEarth extends ItemBlock
{
    public FCItemBlockAestheticOpaqueEarth( int iItemID )
    {
        super( iItemID );
        
        setMaxDamage( 0 );
        setHasSubtypes(true);
        
        setUnlocalizedName( "fcAestheticOpaqueEarth" );
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
    		case FCBlockAestheticOpaqueEarth.m_iSubtypeBlightLevel0:
    		case FCBlockAestheticOpaqueEarth.m_iSubtypeBlightLevel1:
    		case FCBlockAestheticOpaqueEarth.m_iSubtypeBlightLevel2:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("blight").toString();
    			
    		case FCBlockAestheticOpaqueEarth.m_iSubtypeBlightLevel3:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("blight3").toString();
    			
    		case FCBlockAestheticOpaqueEarth.m_iSubtypePackedEarth:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("packed").toString();
    			
    		case FCBlockAestheticOpaqueEarth.m_iSubtypeDung:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("dung").toString();
    			
			default:
				
				return super.getUnlocalizedName();
    	}
    }
}