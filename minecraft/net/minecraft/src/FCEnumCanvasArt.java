// FCMOD

package net.minecraft.src;

public enum FCEnumCanvasArt
{
    Icarus( "Icarus", 64, 64, 0, 0),
    Dagon( "Dagon", 64, 64, 64, 0 ),
    Pentacle( "Pentacle", 64, 64, 128, 0 ),
    Dragon( "Dragon", 64, 64, 192, 0 ),
    TreeOfLife( "TreeOfLife", 64, 96, 0, 64 ),
    Magician( "Magician", 48, 96, 64, 64 ),
    HangedMan( "HangedMan", 48, 96, 112, 64 ),
    Death( "Death", 48, 96, 160, 64 ),
    Fool( "Fool", 48, 96, 208, 64 ),
    IsleOfDead( "IsleOfDead", 96, 48, 0, 160 );

    /** Holds the maximum length of paintings art title. */
    public static final int maxArtTitleLength = "SuperLongTestString".length();

    /** Painting Title. */
    public final String m_sTitle;
    public final int m_iSizeX;
    public final int m_iSizeY;
    public final int m_iOffsetX;
    public final int m_iOffsetY;

    private FCEnumCanvasArt( String sTitle, int iSizeX, int iSizeY, int iOffsetX, int iOffsetY )
    {
    	m_sTitle = sTitle;
        m_iSizeX = iSizeX;
        m_iSizeY = iSizeY;
        m_iOffsetX = iOffsetX;
        m_iOffsetY = iOffsetY;
    }
}
