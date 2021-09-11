// FCMOD

package net.minecraft.src;

import net.minecraft.client.Minecraft;

import java.nio.ByteBuffer;
import java.util.List;

public class FCClientTextureFireStoked extends FCClientTextureFire
{
    public FCClientTextureFireStoked( String sName, int iFireAnimationIndex )
    {
        super( sName, iFireAnimationIndex );
        
        m_iFireAnimationIndex = iFireAnimationIndex;
    }
    
    @Override
    public void updateAnimation()
    {
    	frameCounter = 0;

        if ( m_FireAnimation != null )
        {
        	m_FireAnimation.CopyStokedFireFrameToByteBuffer( m_frameBuffer, m_iBufferPixelSize );
        }
    	
    	textureSheet.UploadByteBufferToGPU( originX, originY, m_frameBuffer, m_iBufferWidth, m_iBufferHeight );
    }
    
    @Override
    public boolean IsProcedurallyAnimated()
    {
    	return true;
    }    
}
