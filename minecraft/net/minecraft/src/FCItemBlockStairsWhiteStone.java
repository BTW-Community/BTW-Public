// FCMOD

package net.minecraft.src;

public class FCItemBlockStairsWhiteStone extends ItemBlock
{
    public FCItemBlockStairsWhiteStone( int iItemID )
    {
        super( iItemID );
        
        setMaxDamage( 0 );
        setHasSubtypes( true );
        
        setUnlocalizedName( "fcBlockWhiteStoneStairs" );
    }

    @Override
    public int getMetadata( int iItemDamage )
    {
		return iItemDamage;    	
    }
    
    @Override
    public String getUnlocalizedName( ItemStack itemstack )
    {
    	int iDamage = itemstack.getItemDamage();
    	
    	if ( ( iDamage & 8 ) > 0 )
    	{
            return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("cobble").toString();
    	}
    	else
    	{
            return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("smooth").toString();
    	}
    }
}