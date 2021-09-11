// FCMOD

package net.minecraft.src;

public abstract class FCItemArmorMod extends FCItemArmor
{
    public FCItemArmorMod( int iItemID, EnumArmorMaterial armorMaterial, int iRenderIndex, int iArmorType, int iWeight )
    {
    	super( iItemID, armorMaterial, iRenderIndex, iArmorType, iWeight );
    }
    
    @Override
    public boolean hasColor( ItemStack stack )
    {
    	if ( HasCustomColors() )
    	{
    		if ( stack.hasTagCompound() )
    		{
    			if ( stack.getTagCompound().hasKey("display") &&
    				stack.getTagCompound().getCompoundTag("display").hasKey("color") )
    			{
    				return true;
    			}
    		}    		
    	}
    	
    	return false;
    }

    @Override
    public int getColor( ItemStack stack )
    {
    	if ( HasCustomColors() )
        {
            NBTTagCompound var2 = stack.getTagCompound();

            if ( var2 != null )
            {
                NBTTagCompound var3 = var2.getCompoundTag("display");
                
                if ( var3 != null )
                {
                	if ( var3.hasKey("color") )
                	{
                		return var3.getInteger("color");
                	}
                }
            }
            
            return GetDefaultColor();
        }
        
        return -1;
    }
    
    @Override
    public void removeColor( ItemStack stack )
    {
    	if ( HasCustomColors() )
        {
            NBTTagCompound var2 = stack.getTagCompound();

            if (var2 != null)
            {
                NBTTagCompound var3 = var2.getCompoundTag("display");

                if (var3.hasKey("color"))
                {
                    var3.removeTag("color");
                }
            }
        }
    }

    @Override
    public void func_82813_b(ItemStack par1ItemStack, int par2)
    {
    	if ( !HasCustomColors() )
        {
            throw new UnsupportedOperationException("Can\'t dye this shiznit fo'shnizzle!");
        }
        else
        {
            NBTTagCompound var3 = par1ItemStack.getTagCompound();

            if (var3 == null)
            {
                var3 = new NBTTagCompound();
                par1ItemStack.setTagCompound(var3);
            }

            NBTTagCompound var4 = var3.getCompoundTag("display");

            if (!var3.hasKey("display"))
            {
                var3.setCompoundTag("display", var4);
            }

            var4.setInteger("color", par2);
        }
    }
    
    public boolean HasCustomColors()
    {
    	return false;
    }
    
    public boolean HasSecondRenderLayerWhenWorn()
    {
    	return false;
    }
    
    public int GetDefaultColor()
    {
    	return 0;
    }
    
    public String GetWornTextureDirectory()
    {
    	return "/btwmodtex/";
    }
    
	abstract public String GetWornTexturePrefix();
	
	//----------- Client Side Functionality -----------//

    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return false;
    }
    
    @Override
    public Icon getIconFromDamageForRenderPass( int iDamage, int iRenderPass )
    {
    	// override to prevent cloth type armors getting the render pass icons from leather
    	
        return getIconFromDamage( iDamage );
    }
}

