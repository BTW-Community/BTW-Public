// FCMOD

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class FCClientGuiInfernalEnchanter extends GuiContainer
{
	private static final int m_iGuiHeight = 210;
	
    private static final int m_iScrollIconScreenPosX = 17;
    private static final int m_iScrollIconScreenPosY = 37;
    
    private static final int m_iScrollIconBitmapPosX = 176;
    private static final int m_iScrollIconBitmapPosY = 0;
    
    private static final int m_iItemIconScreenPosX = 17;
    private static final int m_iItemIconScreenPosY = 75;
    
    private static final int m_iItemIconBitmapPosX = 192;
    private static final int m_iItemIconBitmapPosY = 0;
    
    private static final int m_iEnchantmentButtonsPosX = 60;
    private static final int m_iEnchantmentButtonsPosY = 17;
    private static final int m_iEnchantmentButtonsHeight = 19;
    private static final int m_iEnchantmentButtonsWidth = 108;
    
    private static final int m_iEnchantmentButtonNormalPosX = 0;
    private static final int m_iEnchantmentButtonNormalPosY = 211;
    
    private static final int m_iEnchantmentButtonInactivePosX = 0;
    private static final int m_iEnchantmentButtonInactivePosY = 230;
    
    private static final int m_iEnchantmentButtonHighlightedPosX = 108;
    private static final int m_iEnchantmentButtonHighlightedPosY = 211;
    
	private FCContainerInfernalEnchanter m_container;
	
    /*
     * world, i, j, & k are only relevant on the server
     */
    public FCClientGuiInfernalEnchanter( InventoryPlayer playerInventory, World world, int i, int j, int k )
    {
        super( new FCContainerInfernalEnchanter( playerInventory, world, i, j, k ) );
        
        ySize = m_iGuiHeight;
        
        m_container = (FCContainerInfernalEnchanter)inventorySlots;        
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }

    @Override
    protected void mouseClicked( int iXClick, int iYClick, int par3)
    {
        super.mouseClicked( iXClick, iYClick, par3 );
        
        int iBitmapXOffset = (width - xSize) / 2;
        int iBitmapYOffset = (height - ySize) / 2;

        for ( int iButtonIndex = 0; iButtonIndex < FCContainerInfernalEnchanter.m_iMaxEnchantmentPowerLevel; iButtonIndex++)
        {
            int iRelativeXClick = iXClick - ( iBitmapXOffset + m_iEnchantmentButtonsPosX );
            int iRelativeYClick = iYClick - ( iBitmapYOffset + m_iEnchantmentButtonsPosY + m_iEnchantmentButtonsHeight * iButtonIndex );

            if ( iRelativeXClick >= 0 && iRelativeYClick >= 0 && iRelativeXClick < m_iEnchantmentButtonsWidth && iRelativeYClick < m_iEnchantmentButtonsHeight )
            {
            	// Relay the click to the server, which will then handle the actual enchanting
        		mc.playerController.sendEnchantPacket( m_container.windowId, iButtonIndex );
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer( int i, int j )
    {
        fontRenderer.drawString( "Infernal Enchanter", 40, 5, 0x404040);
        
        fontRenderer.drawString( "Inventory", 8, ( ySize - 96 ) + 2, 0x404040 );
        
        // debug display for bookshelves

        /*
    	String bookshelfString = (new StringBuilder()).append( m_container.m_iMaxSurroundingBookshelfLevel).toString();

        fontRenderer.drawString( bookshelfString, 10, 10, 0xFF0000 );
        */
        
        // draw level numbers to the sides of the buttons
        
        for ( int iTemp = 0; iTemp < m_container.m_iMaxEnchantmentPowerLevel; iTemp++ )
        {
        	// get roman numeral equivalent of level
        	
        	String levelString = (new StringBuilder()).append(
        			StatCollector.translateToLocal((new StringBuilder()).append("enchantment.level.").append(iTemp + 1).toString())).toString();

            fontRenderer.drawString( levelString, m_iEnchantmentButtonsPosX - 15, m_iEnchantmentButtonsPosY + (iTemp * m_iEnchantmentButtonsHeight ) + 6, 0x404040 );
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer( float par1, int iMouseX, int iMouseY )
    {
        RenderHelper.disableStandardItemLighting();
        
    	// draw background image
    	
        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
        mc.renderEngine.bindTexture( "/btwmodtex/fcguiinfernal.png" );
        
        int xPos = ( width - xSize ) / 2;
        int yPos = ( height - ySize ) / 2;
        
        drawTexturedModalRect( xPos, yPos, 0, 0, xSize, ySize );
        
        mc.renderEngine.bindTexture( "/btwmodtex/fcguiinfernal.png" );
        
        // draw icons to indicate empty slot for scroll
        
        ItemStack scrollStack = m_container.m_tableInventory.getStackInSlot( 0 );
        
        if ( scrollStack == null)
        {
            drawTexturedModalRect( xPos + m_iScrollIconScreenPosX,				// screen x pos 
            		yPos + m_iScrollIconScreenPosY, 							// screen y pos
            		m_iScrollIconBitmapPosX, 									// bitmap source x
            		m_iScrollIconBitmapPosY, 									// bitmap source y
            		16, 														// width
            		16 );														// height            
        }
        
        // draw icons to indicate empty slot for item
        
        ItemStack itemStack = m_container.m_tableInventory.getStackInSlot( 1 );
        
        if ( itemStack == null)
        {
            drawTexturedModalRect( xPos + m_iItemIconScreenPosX,				// screen x pos 
            		yPos + m_iItemIconScreenPosY, 								// screen y pos
            		m_iItemIconBitmapPosX, 										// bitmap source x
            		m_iItemIconBitmapPosY, 										// bitmap source y
            		16, 														// width
            		16 );														// height            
        }
        
        // draw enchantment buttons
        
        EnchantmentNameParts.instance.setRandSeed( m_container.m_lNameSeed );
        
        for ( int iTempButton = 0; iTempButton < m_container.m_iMaxEnchantmentPowerLevel; iTempButton ++ )
        {
            String enchantmentName = EnchantmentNameParts.instance.generateRandomEnchantName();
            
        	int iButtonEnchantmentLevel = m_container.m_CurrentEnchantmentLevels[iTempButton];
        	
        	boolean bPlayerCapable = iButtonEnchantmentLevel <= mc.thePlayer.experienceLevel && iButtonEnchantmentLevel <= m_container.m_iMaxSurroundingBookshelfLevel;
        	
            int iNameColor = 0x685e4a;
            int iLevelNumberColor = 0x80ff20;

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture( "/btwmodtex/fcguiinfernal.png" );            
            
        	if ( iButtonEnchantmentLevel <= 0 || !bPlayerCapable  )
        	{
        		// disable the button
        		
	            drawTexturedModalRect( xPos + m_iEnchantmentButtonsPosX, yPos + m_iEnchantmentButtonsPosY + ( m_iEnchantmentButtonsHeight * iTempButton ), 								
	            		m_iEnchantmentButtonInactivePosX, m_iEnchantmentButtonInactivePosY, 										
	            		m_iEnchantmentButtonsWidth, m_iEnchantmentButtonsHeight );
	            
	            iNameColor = (iNameColor & 0xfefefe) >> 1;
        		iLevelNumberColor = 0x407f10;
        	}
        	else
        	{
        		if ( IsMouseOverEnchantmentButton( iTempButton, iMouseX, iMouseY ) )
        		{
    	            drawTexturedModalRect( xPos + m_iEnchantmentButtonsPosX, yPos + m_iEnchantmentButtonsPosY + ( m_iEnchantmentButtonsHeight * iTempButton ), 								
    	            		m_iEnchantmentButtonHighlightedPosX, m_iEnchantmentButtonHighlightedPosY, 										
    	            		m_iEnchantmentButtonsWidth, m_iEnchantmentButtonsHeight );
    	            
    	            iNameColor = 0xffff80;
        		}
        	}
        	
        	if ( iButtonEnchantmentLevel > 0 )
        	{
        		// fill in the enchantment details
        		
        		FontRenderer tempFontRenderer = mc.standardGalacticFontRenderer;
        		
        		tempFontRenderer.drawSplitString( enchantmentName, xPos + m_iEnchantmentButtonsPosX + 2, 
    				yPos + 2 + m_iEnchantmentButtonsPosY + ( m_iEnchantmentButtonsHeight * iTempButton ), 
    				104, iNameColor );
        		
        		tempFontRenderer = mc.fontRenderer;
        		
        		String enchantmentLevelString = ( new StringBuilder() ).append( "" ).append( iButtonEnchantmentLevel ).toString();
        		
        		tempFontRenderer.drawStringWithShadow( enchantmentLevelString, 
    				xPos + m_iEnchantmentButtonsPosX + m_iEnchantmentButtonsWidth - 2 - tempFontRenderer.getStringWidth(enchantmentLevelString), 
    				yPos + 9 + m_iEnchantmentButtonsPosY + ( m_iEnchantmentButtonsHeight * iTempButton ), iLevelNumberColor );
        	}
        }
    }
    
    private boolean IsMouseOverEnchantmentButton( int iButtonIndex, int iMouseX, int iMouseY )
    {
        int iBackgroundXPos = ( width - xSize ) / 2;
        int iBackgroundYPos = ( height - ySize ) / 2;
        
        int iRelativeMouseX = iMouseX - iBackgroundXPos;
    	int iRelativeMouseY = iMouseY - iBackgroundYPos;
    	
    	int iButtonYPos = m_iEnchantmentButtonsPosY + ( m_iEnchantmentButtonsHeight * iButtonIndex );
    	
    	if ( iRelativeMouseX >= m_iEnchantmentButtonsPosX && iRelativeMouseX <= m_iEnchantmentButtonsPosX + m_iEnchantmentButtonsWidth &&
			iRelativeMouseY >= iButtonYPos && iRelativeMouseY <= iButtonYPos + m_iEnchantmentButtonsHeight )
    	{
    		return true;
    	}	
    	
    	return false;
    }
}