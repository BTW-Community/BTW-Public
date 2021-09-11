// FCMOD

package net.minecraft.src;

public class FCClientModelSquid extends ModelSquid
{
    public FCClientModelSquid()
    {
    	super();
    }
    
    @Override
    public void render( Entity entity, float par2, float par3, float par4, float par5, float par6, float fScale )
    {
        setRotationAngles( par2, par3, par4, par5, par6, fScale, entity );
        
        squidBody.render( fScale );

        int iAttackTentacle = -1;
        
        if ( ((FCEntitySquid)entity).m_iTentacleAttackInProgressCounter > 0 )
        {
        	iAttackTentacle = 6;
        }
        
        for ( int iTempTentacle = 0; iTempTentacle < squidTentacles.length; ++iTempTentacle )
        {
        	if ( iTempTentacle != iAttackTentacle )
        	{
                squidTentacles[iTempTentacle].render( fScale );
        	}
        }
    }
}
