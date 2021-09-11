//FCMOD 

package net.minecraft.src;

public class FCItemBlockPlanter extends ItemBlock
{
    public FCItemBlockPlanter( int i )
    {
        super( i );
        
        setMaxDamage( 0 );
        setHasSubtypes( true );
        setUnlocalizedName( "fcBlockPlanter" );
    }

    @Override
    public int getMetadata( int i )
    {
    	return i;
    }
    
    @Override
    public String getItemDisplayName( ItemStack stack )
    {
    	String name = super.getItemDisplayName( stack );
    	
    	int iDamage = stack.getItemDamage();
    	
    	if ( iDamage == FCBlockPlanter.m_iTypeSoil || 
    		iDamage == FCBlockPlanter.m_iTypeSoilFertilized )
    	{
    		// deprecated subtypes
    		
    		return "Old " + name;
    	}
    	
    	return name;
    }    
    
    //------------- Class Specific Methods ------------//
    
	//------------ Client Side Functionality ----------//
}
