// FCMOD

package net.minecraft.src;

public class FCBlockCloth extends BlockCloth
{
    public FCBlockCloth()
    {
        super();
        
        setHardness( 0.8F );
        
        SetBuoyant();
        
		SetFireProperties( FCEnumFlammability.CLOTH );
		
		setStepSound( soundClothFootstep );
		
		setUnlocalizedName( "cloth" );		
    }
}
