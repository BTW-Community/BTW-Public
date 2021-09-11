// FCMOD

package net.minecraft.src;

public class FCClientRenderSpider extends RenderSpider
{
    public FCClientRenderSpider()
    {
    	super();
    }
    
    @Override
    protected int shouldRenderPass( EntityLiving entity, int iRenderPass, float par3)
    {
    	FCEntitySpider spider = (FCEntitySpider)entity;
    	
    	if ( !spider.DoEyesGlow() )
    	{
    		return -1;
    	}
    	
        return setSpiderEyeBrightness( spider, iRenderPass, par3 );
    }
}
