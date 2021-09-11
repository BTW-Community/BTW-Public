// FCMOD

package net.minecraft.src;

/** 
 * Class to fully replace ItemTool due to large amount of refactoring that was applied to that class
 */
public abstract class FCItemTool extends Item
{
    protected float efficiencyOnProperMaterial = 4F;
    protected int damageVsEntity;
    protected EnumToolMaterial toolMaterial;

    protected FCItemTool( int iITemID, int iBaseEntityDamage, EnumToolMaterial par3EnumToolMaterial )
    {
        super(iITemID);
        
        maxStackSize = 1;        
        toolMaterial = par3EnumToolMaterial;
        
        setMaxDamage( par3EnumToolMaterial.getMaxUses() );
        efficiencyOnProperMaterial = par3EnumToolMaterial.getEfficiencyOnProperMaterial();
        damageVsEntity = iBaseEntityDamage + par3EnumToolMaterial.getDamageVsEntity();
        
    	if ( toolMaterial == EnumToolMaterial.WOOD )
    	{
    		SetBuoyant();    		
        	SetFurnaceBurnTime( FCEnumFurnaceBurnTime.WOOD_TOOLS );
        	SetIncineratedInCrucible();
    	}
    	
    	SetInfernalMaxEnchantmentCost( toolMaterial.GetInfernalMaxEnchantmentCost() );
    	SetInfernalMaxNumEnchants( toolMaterial.GetInfernalMaxNumEnchants() );
    	
        setCreativeTab( CreativeTabs.tabTools );
    }

    @Override
    public boolean hitEntity( ItemStack stack, EntityLiving defendingEntity, EntityLiving attackingEntity )
    {
        stack.damageItem( 2, attackingEntity );
        
        return true;
    }

    @Override
    public boolean onBlockDestroyed( ItemStack stack, World world, int iBlockID, int i, int j, int k, EntityLiving usingEntity )
    {
        if ( Block.blocksList[iBlockID].getBlockHardness( world, i, j, k ) > 0F )
        {
            stack.damageItem( 1, usingEntity );
        }

        return true;
    }

    @Override
    public int getDamageVsEntity( Entity entity )
    {
        return damageVsEntity;
    }

    @Override
    public int getItemEnchantability()
    {
        return toolMaterial.getEnchantability();
    }

    
    @Override
    public boolean IsEnchantmentApplicable( Enchantment enchantment )
    {
    	if ( enchantment.type == EnumEnchantmentType.digger )
    	{
    		return true;
    	}
    	
    	return super.IsEnchantmentApplicable( enchantment );
    }
    
    @Override
    public float getStrVsBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
    	if ( IsEfficientVsBlock( stack, world, block, i, j, k ) )
		{
            return efficiencyOnProperMaterial;
		}

    	return super.getStrVsBlock( stack, world, block, i, j, k );
    }
    
    @Override
    public boolean IsEfficientVsBlock( ItemStack stack, World world, Block block, int i, int j, int k )
    {
    	if ( !block.blockMaterial.isToolNotRequired() )
    	{
    		if ( canHarvestBlock( stack, world, block, i, j, k ) )
    		{
    			return true;
    		}
    	}
    	
    	return IsToolTypeEfficientVsBlockType( block );
    }
    
    @Override
    public void onCreated( ItemStack stack, World world, EntityPlayer player ) 
    {
		if ( player.m_iTimesCraftedThisTick == 0 && world.isRemote )
		{
			if ( toolMaterial == EnumToolMaterial.WOOD )
			{
				player.playSound( "mob.zombie.woodbreak", 0.1F, 1.25F + ( world.rand.nextFloat() * 0.25F ) );
			}
			else if ( toolMaterial == EnumToolMaterial.STONE )
			{
				player.playSound( "random.anvil_land", 0.5F, world.rand.nextFloat() * 0.25F + 1.75F );
			}
			else
			{
				player.playSound( "random.anvil_use", 0.5F, world.rand.nextFloat() * 0.25F + 1.25F );
			}			
		}
		
    	super.onCreated( stack, world, player );
    }
    
    @Override
    public boolean CanItemBeUsedByPlayer( World world, int i, int j, int k, int iFacing, EntityPlayer player, ItemStack stack )
    {
    	return !player.IsLocalPlayerAndHittingBlock();
    }
    
    @Override
    public boolean onItemUse( ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int iFacing, 
    	float fClickX, float fClickY, float fClickZ )
    {
        if ( player != null && player.canPlayerEdit( i, j, k, iFacing, stack ) && GetCanBePlacedAsBlock() )
        {
        	FCUtilsBlockPos placementPos = new FCUtilsBlockPos( i, j, k );
        	FCUtilsBlockPos stuckInPos = new FCUtilsBlockPos( i, j, k );

        	if ( !FCUtilsWorld.IsReplaceableBlock( world, i, j, k ) )
        	{
        		placementPos.AddFacingAsOffset( iFacing );
        	}
        	else
    		{
    			iFacing = 1;
    			stuckInPos.AddFacingAsOffset( 0 );
    		}
        	
        	if ( FCUtilsWorld.DoesBlockHaveCenterHardpointToFacing( world, stuckInPos.i, stuckInPos.j, stuckInPos.k, iFacing, true ) &&        		
        		FCBetterThanWolves.fcBlockToolPlaced.canPlaceBlockAt( world, placementPos.i, placementPos.j, placementPos.k ) )
        	{
            	Block blockStuckIn = Block.blocksList[world.getBlockId( stuckInPos.i, stuckInPos.j, stuckInPos.k )];
            	
            	if ( blockStuckIn != null &&
            		blockStuckIn.CanToolsStickInBlock( world, stuckInPos.i, stuckInPos.j, stuckInPos.k ) &&
            		CanToolStickInBlock( stack, blockStuckIn, world, stuckInPos.i, stuckInPos.j, stuckInPos.k ) )            	
        		{
            		int iTargetFacing;
            		int iTargetFacingLevel;
            		
            		if ( iFacing >= 2 )
            		{
            			iTargetFacing = Block.GetOppositeFacing( iFacing );
            			iTargetFacingLevel = 2;
            		}
            		else
            		{
            			iTargetFacing = FCUtilsMisc.ConvertOrientationToFlatBlockFacing( player );
            			iTargetFacingLevel = Block.GetOppositeFacing( iFacing );            			
            		}

            		int iMetadata = FCBetterThanWolves.fcBlockToolPlaced.SetFacing( 0, iTargetFacing );
            		
            		iMetadata = FCBetterThanWolves.fcBlockToolPlaced.SetVerticalOrientation( iMetadata, iTargetFacingLevel );
            		
            		world.setBlockAndMetadataWithNotify( placementPos.i, placementPos.j, placementPos.k, 
            			FCBetterThanWolves.fcBlockToolPlaced.blockID, iMetadata );
            		
                    TileEntity targetTileEntity = world.getBlockTileEntity( placementPos.i, placementPos.j, placementPos.k );

                    if ( targetTileEntity != null && targetTileEntity instanceof FCTileEntityToolPlaced )
                    {
                    	((FCTileEntityToolPlaced)targetTileEntity).SetToolStack( stack );
                    	
                    	if ( !world.isRemote )
                    	{
                    		PlayPlacementSound( stack, blockStuckIn, world, placementPos.i, placementPos.j, placementPos.k );
                    	}
                    	
                        stack.stackSize--;
                        
                        return true;
                    }
        		}
        	}
        }
        
        return false;
    }
    
    //------------- Class Specific Methods ------------//
	
    abstract protected boolean IsToolTypeEfficientVsBlockType( Block block );    
    
    public FCItemTool SetDamageVsEntity( int iDamage )
    {
    	damageVsEntity = iDamage;
    	
    	return this;
    }
    
    protected boolean CanToolStickInBlock( ItemStack stack, Block block, World world, int i, int j, int k )
    {
		return IsEfficientVsBlock( stack, world, block, i, j, k );
    }
    
    protected void PlayPlacementSound( ItemStack stack, Block blockStuckIn, World world, int i, int j, int k )
    {
        world.playSoundEffect( (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, blockStuckIn.stepSound.getStepSound(), 
    		( blockStuckIn.stepSound.getVolume() + 1F) / 2F, blockStuckIn.stepSound.getPitch() * 0.8F );
    }
    
    protected boolean GetCanBePlacedAsBlock()
    {
    	return true;
    }
    
    protected float GetVisualVerticalOffsetAsBlock()
    {
    	return 0.75F;
    }
    
    protected float GetVisualHorizontalOffsetAsBlock()
    {
    	return 0.5F;
    }
    
    protected float GetVisualRollOffsetAsBlock()
    {
    	return 0F;
    }
    
    protected float GetBlockBoundingBoxHeight()
    {
    	return 0.75F;
    }
    
    protected float GetBlockBoundingBoxWidth()
    {
    	return 0.75F;
    }
    
	//----------- Client Side Functionality -----------//
}