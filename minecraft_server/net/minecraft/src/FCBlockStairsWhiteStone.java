// FCMOD

package net.minecraft.src;

public class FCBlockStairsWhiteStone extends FCBlockStairs
{	
	protected FCBlockStairsWhiteStone( int iBlockID )
    {
    	super( iBlockID, Block.stone, 0 );
    	
        setHardness( 1.5F );
        setResistance( 10F );
        SetPicksEffectiveOn();        
        
        setUnlocalizedName( "fcBlockWhiteStoneStairs" );        
	}

	@Override
    public int damageDropped( int iMetadata )
    {
		return iMetadata & 8;
    }
	
	//------------- Class Specific Methods ------------//
	
	public boolean GetIsCobbleFromMetadata( int iMetadata )
	{
		return ( iMetadata & 8 ) > 0;
	}	
}
