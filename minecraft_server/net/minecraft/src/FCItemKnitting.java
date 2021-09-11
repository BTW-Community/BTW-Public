// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCItemKnitting extends FCItemCraftingProgressive
{
    public FCItemKnitting( int iItemID )
    {
    	super( iItemID );
    	
        SetBuoyant();
    	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.SHAFT.m_iBurnTime + 
    		( 2 * FCEnumFurnaceBurnTime.WOOL.m_iBurnTime ) );
    	
        setUnlocalizedName( "fcItemKnitting" );        
    }

    @Override
    protected void PlayCraftingFX( ItemStack stack, World world, EntityPlayer player )
    {
        player.playSound( "step.wood", 
        	0.25F + 0.25F * (float)world.rand.nextInt( 2 ), 
        	( world.rand.nextFloat() - world.rand.nextFloat() ) * 0.25F + 1.75F );
    }
    
    @Override
    public ItemStack onEaten( ItemStack stack, World world, EntityPlayer player )
    {
    	int iColorIndex = FCItemWool.GetClosestColorIndex( GetColor( stack ) );
    	ItemStack woolStack = new ItemStack( FCBetterThanWolves.fcItemWoolKnit, 1, iColorIndex );
    	
        world.playSoundAtEntity( player, "step.cloth", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F );
        
		FCUtilsItem.GivePlayerStackOrEject( player, woolStack );
		
        return new ItemStack( FCBetterThanWolves.fcItemKnittingNeedles );
    }
    
    @Override
    public boolean GetCanBeFedDirectlyIntoCampfire( int iItemDamage )
    {
    	return false;
    }
    
    @Override
    public boolean GetCanBeFedDirectlyIntoBrickOven( int iItemDamage )
    {
    	return false;
    }
    
    //------------- Class Specific Methods ------------//
    
    static public void SetColor( ItemStack stack, int iColor )
    {
        NBTTagCompound tag = stack.getTagCompound();

        if ( tag == null )
        {
            tag = new NBTTagCompound();
            stack.setTagCompound( tag );
        }
        
        tag.setInteger( "fcColor", iColor );
        
    }
    
    static public int GetColor( ItemStack stack )
    {
        NBTTagCompound tag = stack.getTagCompound();

        if ( tag != null )
        {
        	if ( tag.hasKey( "fcColor" ) )
        	{
        		return tag.getInteger( "fcColor" );
        	}
        }            
        
        return 0;
    }
    
	//------------ Client Side Functionality ----------//
}
