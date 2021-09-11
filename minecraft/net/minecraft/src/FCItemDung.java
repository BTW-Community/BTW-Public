// FCMOD

package net.minecraft.src;

public class FCItemDung extends Item
{
    public FCItemDung( int iItemID )
    {
    	super( iItemID );
    	
    	SetBuoyant();
    	SetIncineratedInCrucible();
		SetFilterableProperties( m_iFilterable_Small );
    	
    	setUnlocalizedName( "fcItemDung" );
    	
    	setCreativeTab( CreativeTabs.tabMaterials );
    }
    
    @Override
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityLiving entity )
    //public boolean useItemOnEntity( ItemStack itemStack, EntityLiving entity )
    {
        if ( entity instanceof FCEntitySheep )
        {
            entity.attackEntityFrom( DamageSource.generic, 0 );
            
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean IsPistonPackable( ItemStack stack )
    {
    	return true;
    }
    
    @Override
    public int GetRequiredItemCountToPistonPack( ItemStack stack )
    {
    	return 9;
    }
    
    @Override
    public int GetResultingBlockIDOnPistonPack( ItemStack stack )
    {
    	return FCBetterThanWolves.fcBlockAestheticOpaqueEarth.blockID;
    }
    
    @Override
    public int GetResultingBlockMetadataOnPistonPack( ItemStack stack )
    {
    	return FCBlockAestheticOpaqueEarth.m_iSubtypeDung;
    }
}