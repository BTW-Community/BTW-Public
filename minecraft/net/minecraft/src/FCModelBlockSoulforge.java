// FCMOD

package net.minecraft.src;

public class FCModelBlockSoulforge extends FCModelBlock
{
	public static final float m_fAnvilBaseHeight = ( 2.0F / 16F );
	public static final float m_fAnvilBaseWidth = ( 8.0F / 16F );
	public static final float m_fAnvilHalfBaseWidth = ( m_fAnvilBaseWidth / 2F );
	public static final float m_fAnvilShaftHeight = ( 7.0F / 16F );
	public static final float m_fAnvilShaftWidth = ( 4.0F / 16F );
	public static final float m_fAnvilHalfShaftWidth = ( m_fAnvilShaftWidth / 2.0F );
	public static final float m_fAnvilTopHeight = ( 7.0F / 16F );
	public static final float m_fAnvilTopWidth = ( 6.0F / 16F );
	public static final float m_fAnvilTopHalfWidth = ( m_fAnvilTopWidth / 2F );
	
	@Override
    protected void InitModel()
    {
    	// base
    	
    	AddBox( 0.5F - m_fAnvilHalfBaseWidth, 0.0F, 0.0F,  
    		0.5F + m_fAnvilHalfBaseWidth, m_fAnvilBaseHeight, 1.0F );
        
        // shaft 
        
    	AddBox( 0.5F - m_fAnvilHalfShaftWidth, m_fAnvilBaseHeight, 0.5F - m_fAnvilHalfShaftWidth, 
    		0.5F + m_fAnvilHalfShaftWidth, m_fAnvilBaseHeight + m_fAnvilShaftHeight, 0.5F + m_fAnvilHalfShaftWidth );
        
        // top
        
    	AddBox( 0.5F - m_fAnvilTopHalfWidth, 1.0F - m_fAnvilTopHeight, 0.5F - m_fAnvilTopHalfWidth, 
    		0.5F + m_fAnvilTopHalfWidth, 1.0F, 0.5F + m_fAnvilTopHalfWidth );
        
        // back end
        
    	AddBox( 0.5F - m_fAnvilTopHalfWidth, 1.0F - m_fAnvilTopHeight + ( 3.0F / 16F ), 0.5F - ( m_fAnvilTopHalfWidth + ( 1.0F / 16F ) ),  
    		0.5F + m_fAnvilTopHalfWidth, 1.0F, 0.5F - m_fAnvilTopHalfWidth ); 
        
        AddBox( 0.5F - m_fAnvilTopHalfWidth, 1.0F - ( 1.0F / 16F ), 0F, 
        	0.5F + m_fAnvilTopHalfWidth, 1.0F, 0.5F - ( m_fAnvilTopHalfWidth  + ( 1.0F / 16F ) )  );
        
        // front end

        AddBox(  0.5F - m_fAnvilTopHalfWidth, 1.0F - m_fAnvilTopHeight + ( 3.0F / 16F ), 0.5F + m_fAnvilTopHalfWidth, 
        	0.5F + m_fAnvilTopHalfWidth, 1.0F, 0.5F + m_fAnvilTopHalfWidth + ( 1.0F / 16F ) ); 
        
        AddBox( 0.5F - m_fAnvilTopHalfWidth + ( 1.0F / 16F ), 1.0F - m_fAnvilTopHeight + ( 4.0F / 16F ), 0.5F + m_fAnvilTopHalfWidth  + ( 1.0F / 16F ), 
        	0.5F + m_fAnvilTopHalfWidth  - ( 1.0F / 16F ), 1.0F, 0.5F + m_fAnvilTopHalfWidth + ( 3.0F / 16F ) ); 
        
        AddBox(  0.5F - m_fAnvilTopHalfWidth + ( 2.0F / 16F ), 1.0F - m_fAnvilTopHeight + ( 5.0F / 16F ), 0.5F + m_fAnvilTopHalfWidth  + ( 3.0F / 16F ), 
        	0.5F + m_fAnvilTopHalfWidth  - ( 2.0F / 16F ), 1.0F, 0.5F + m_fAnvilTopHalfWidth + ( 5.0F / 16F ) ); 
    }    
}
