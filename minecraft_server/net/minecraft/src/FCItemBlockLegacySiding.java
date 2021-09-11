// FCMOD 

package net.minecraft.src;

public class FCItemBlockLegacySiding extends ItemBlock
{
    public FCItemBlockLegacySiding(int i)
    {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
        setUnlocalizedName("fcBlockOmniSlab");
    }

    @Override
    public int getMetadata( int i )
    {
        return i;
    }

    @Override
    public float GetBuoyancy( int iItemDamage )
    {
    	if ( iItemDamage == 0 ) // stone siding
    	{
    		return -1.0F;
    	}
    	else
    	{
    		return super.GetBuoyancy( iItemDamage );
    	}
    }

}
