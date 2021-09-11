//FCMOD

package net.minecraft.src;

public class FCBlockSandstoneSidingAndCornerAndDecorative extends FCBlockSidingAndCornerAndDecorative
{
	protected FCBlockSandstoneSidingAndCornerAndDecorative( int iBlockID )
	{
		super( iBlockID, Material.rock, "fcBlockDecorativeSandstone_top", 0.8F, 1.34F, Block.soundStoneFootstep, "fcSandstoneSiding" );
	}
	
	//----------- Client Side Functionality -----------//
    
    private Icon[] m_IconBySideArray = new Icon[6];
    
	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
        m_IconBySideArray[0] = register.registerIcon( "fcBlockDecorativeSandstone_bottom" );
        m_IconBySideArray[1] = register.registerIcon( "fcBlockDecorativeSandstone_top" );
        
        Icon sideIcon = register.registerIcon( "fcBlockDecorativeSandstone_side" );
        
        m_IconBySideArray[2] = sideIcon;
        m_IconBySideArray[3] = sideIcon;
        m_IconBySideArray[4] = sideIcon;
        m_IconBySideArray[5] = sideIcon;
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		return m_IconBySideArray[iSide];
    }
}