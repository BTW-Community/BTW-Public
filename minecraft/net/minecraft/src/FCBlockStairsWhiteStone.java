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
	
	//----------- Client Side Functionality -----------//
    
    private Icon m_IconWhiteCobble;
    
	@Override
	public void registerIcons( IconRegister register )
	{
		blockIcon = register.registerIcon( "fcBlockWhiteStone" );
		
		m_IconWhiteCobble = register.registerIcon( "fcBlockWhiteCobble" );		
    }
	
    @Override
	public Icon getIcon( int iSide, int iMetadata )
	{
		if ( GetIsCobbleFromMetadata( iMetadata ) )
		{
			return m_IconWhiteCobble;
		}
		
		return blockIcon;
	}
}
