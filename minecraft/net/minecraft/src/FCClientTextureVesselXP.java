// FCMOD

package net.minecraft.src;

import net.minecraft.client.Minecraft;

import java.nio.ByteBuffer;
import java.util.List;

public class FCClientTextureVesselXP extends TextureStitched
{
    ByteBuffer m_frameBuffer;
    
    float m_fColorMultiplierRed;
    float m_fColorMultiplierGreen;
    float m_fColorMultiplierBlue;
    
    int m_iBufferWidth;
    int m_iBufferHeight;
    int m_iBufferPixelSize;
    
    public FCClientTextureVesselXP( String sName )
    {
        super( sName );
    }
    
    @Override
    public void init(Texture par1Texture, List par2List, int par3, int par4, int par5, int par6, boolean par7)
    {
    	super.init(par1Texture, par2List, par3, par4, par5, par6, par7);
    	
        m_fColorMultiplierRed = 0F;
        m_fColorMultiplierGreen = 0F;
        m_fColorMultiplierBlue = 0F;
        
        m_iBufferWidth = ((Texture)this.textureList.get(0)).getWidth();
        m_iBufferHeight = ((Texture)this.textureList.get(0)).getHeight();
        m_iBufferPixelSize = m_iBufferWidth * m_iBufferHeight;
        
        m_frameBuffer = ByteBuffer.allocateDirect( m_iBufferPixelSize * 4 );        
    }

    @Override
    public void updateAnimation()
    {
    	frameCounter++;
    	
    	float fRedAngle = (float)( frameCounter % 360 ) * (float)Math.PI / 180F; 
    	
    	m_fColorMultiplierRed = ( MathHelper.sin( fRedAngle ) * 0.5F + 0.5F ) * 0.75F + 0.25F;
    	
    	CopyFrameToBufferWithColorMultiplier( m_frameBuffer, m_iBufferPixelSize );

    	textureSheet.UploadByteBufferToGPU( originX, originY, m_frameBuffer, m_iBufferWidth, m_iBufferHeight );
    }
    
    private void CopyFrameToBufferWithColorMultiplier( ByteBuffer m_frameBuffer, int m_iBufferPixelSize )
    {
    	ByteBuffer sourceBuffer = ((Texture)this.textureList.get(0)).getTextureData();
    	
        for ( int iPixelIndex = 0; iPixelIndex < m_iBufferPixelSize; iPixelIndex++ )
        {
        	int iSourceRed = sourceBuffer.get( iPixelIndex * 4 + 0 ) & 0xff;
        	int iSourceGreen = sourceBuffer.get( iPixelIndex * 4 + 1 ) & 0xff;
        	int iSourceBlue = sourceBuffer.get( iPixelIndex * 4 + 2 ) & 0xff;
        	
        	int iPixelRed = (int)( iSourceRed * m_fColorMultiplierRed );
        	int iPixelGreen = (int)( iSourceGreen * m_fColorMultiplierGreen );
        	int iPixelBlue = (int)( iSourceBlue * m_fColorMultiplierBlue );
        	
        	int iPixelAlpha =  (int)sourceBuffer.get( iPixelIndex * 4 + 3 );
        	
        	if ( iPixelRed > 255 || iPixelGreen > 255 || iPixelBlue > 255 ||
        		iPixelRed < 0 || iPixelGreen < 0 || iPixelBlue < 0 )
        	{
        		boolean blah = true;
        	}
        	
            m_frameBuffer.put( iPixelIndex * 4 + 0, (byte)iPixelRed );
            m_frameBuffer.put( iPixelIndex * 4 + 1, (byte)iPixelGreen );
            m_frameBuffer.put( iPixelIndex * 4 + 2, (byte)iPixelBlue );
            m_frameBuffer.put( iPixelIndex * 4 + 3, (byte)iPixelAlpha );
        }    	
    }
    
    @Override
    public boolean IsProcedurallyAnimated()
    {
    	return true;
    }    
}
