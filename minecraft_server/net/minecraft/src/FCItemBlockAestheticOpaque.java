// FCMOD 

package net.minecraft.src;

public class FCItemBlockAestheticOpaque extends ItemBlock
{
    public FCItemBlockAestheticOpaque( int iItemID )
    {
        super( iItemID );
        
        setMaxDamage( 0 );
        setHasSubtypes(true);
        
        setUnlocalizedName( "fcAestheticOpaque" );
    }

    @Override
    public int getMetadata( int iItemDamage )
    {
    	if ( iItemDamage == FCBlockAestheticOpaque.m_iSubtypeSteel )
    	{
    		// substitute the new block type for the old
    		
    		return 0;
    	}
    	
		return iItemDamage;    	
    }
    
    @Override
    public String getUnlocalizedName( ItemStack itemstack )
    {
    	switch( itemstack.getItemDamage() )
    	{
    		case FCBlockAestheticOpaque.m_iSubtypeWicker:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("wicker").toString();
    			
    		case FCBlockAestheticOpaque.m_iSubtypeDung:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("dung").toString();
    			
    		case FCBlockAestheticOpaque.m_iSubtypeSteel:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("steel").toString();
    			
    		case FCBlockAestheticOpaque.m_iSubtypeHellfire:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("hellfire").toString();
    			
    		case FCBlockAestheticOpaque.m_iSubtypePadding:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("padding").toString();
    			
    		case FCBlockAestheticOpaque.m_iSubtypeSoap:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("soap").toString();
    			
    		case FCBlockAestheticOpaque.m_iSubtypeRope:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("rope").toString();
    			
    		case FCBlockAestheticOpaque.m_iSubtypeFlint:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("flint").toString();
    			
    		case FCBlockAestheticOpaque.m_iSubtypeWhiteStone:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("whitestone").toString();
    			
    		case FCBlockAestheticOpaque.m_iSubtypeWhiteCobble:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("whitecobble").toString();
    			
    		case FCBlockAestheticOpaque.m_iSubtypeBarrel:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("barrel").toString();
    			
    		case FCBlockAestheticOpaque.m_iSubtypeChoppingBlockClean:
    		case FCBlockAestheticOpaque.m_iSubtypeChoppingBlockDirty:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("choppingblock").toString();
    			
    		case FCBlockAestheticOpaque.m_iSubtypeEnderBlock:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("enderblock").toString();
    			
    		case FCBlockAestheticOpaque.m_iSubtypeBone:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("bone").toString();
    			
			default:
				
				return super.getUnlocalizedName();
    	}
    }    

    @Override
    public int GetBlockIDToPlace( int iItemDamage, int iFacing, float fClickX, float fClickY, float fClickZ )
    {
    	if ( iItemDamage == FCBlockAestheticOpaque.m_iSubtypeSteel )
    	{
    		// substitute the new block type for the old
    		
    		return FCBetterThanWolves.fcSoulforgedSteelBlock.blockID;
    	}
    	
    	return super.GetBlockIDToPlace( iItemDamage, iFacing, fClickX, fClickY, fClickZ );
    }
    
    @Override
    public float GetBuoyancy( int iItemDamage )
    {
    	switch ( iItemDamage )
    	{
    		case FCBlockAestheticOpaque.m_iSubtypeWicker:
    		case FCBlockAestheticOpaque.m_iSubtypeDung:
    		case FCBlockAestheticOpaque.m_iSubtypePadding:
    		case FCBlockAestheticOpaque.m_iSubtypeSoap:
    		case FCBlockAestheticOpaque.m_iSubtypeRope:
    		case FCBlockAestheticOpaque.m_iSubtypeBone:
    			
    			return 1.0F;
    			
    	}
    	
    	return super.GetBuoyancy( iItemDamage );
    }    
}