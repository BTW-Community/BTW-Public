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
    			
                return super.getUnlocalizedName() + "." + "blight";
    			
    		case FCBlockAestheticOpaqueEarth.m_iSubtypeBlightLevel3:

                return super.getUnlocalizedName() + "." + "blight3";
                
    		case FCBlockAestheticOpaqueEarth.m_iSubtypeBlightRootsLevel2:
    			
                return super.getUnlocalizedName() + "." + "blightRoots";
    			
    		case FCBlockAestheticOpaqueEarth.m_iSubtypeBlightRootsLevel3:

                return super.getUnlocalizedName() + "." + "blightRoots3";
    			
    		case FCBlockAestheticOpaqueEarth.m_iSubtypePackedEarth:

                return super.getUnlocalizedName() + "." + "packed";
    			
    		case FCBlockAestheticOpaqueEarth.m_iSubtypeDung:

                return super.getUnlocalizedName() + "." + "dung";
    			
			default:
				
				return super.getUnlocalizedName();
    	}
    }
}