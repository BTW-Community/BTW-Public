// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCBlockSlabSandAndGravel extends FCBlockSlabFalling
{
	public static final int m_iSubtypeGravel = 0;
	public static final int m_iSubtypeSand = 1;
	
    public FCBlockSlabSandAndGravel( int iBlockID )
    {
        super( iBlockID, Material.sand );
        
        setHardness( 0.5F );
        SetShovelsEffectiveOn( true );        
        
        setStepSound( soundGravelFootstep );
        
        setUnlocalizedName( "fcBlockSlabFalling" );
        
		setCreativeTab( CreativeTabs.tabBlock );
    }

	@Override
	public int damageDropped( int iMetadata )
    {
		int iSubtype = this.GetSubtypeFromMetadata( iMetadata );
		
		return iSubtype;		            		
    }
	
    @Override
    public float GetMovementModifier( World world, int i, int j, int k )
    {
    	float fModifier = 1.0F;
        	
    	int iSubtype = GetSubtype( world, i, j, k );
    	
		if ( iSubtype == m_iSubtypeGravel )
		{
			fModifier = 1.2F;
		}
		else if ( iSubtype == m_iSubtypeSand )
		{
			fModifier = 0.8F;
		}
    	
    	return fModifier;
    }
    
    @Override
    public StepSound GetStepSound( World world, int i, int j, int k )
    {
    	int iSubtype = GetSubtype( world, i, j, k );
    	
    	if ( iSubtype == m_iSubtypeSand  )
    	{
    		return soundSandFootstep;
    	}
    	
    	return stepSound;
    }
    
	@Override
    public boolean AttemptToCombineWithFallingEntity( World world, int i, int j, int k, EntityFallingSand entity )
	{
		if ( entity.blockID == blockID )
		{
			int iMetadata = world.getBlockMetadata( i, j, k );
			
			if ( iMetadata == entity.metadata )
			{
				world.setBlockWithNotify( i, j, k, GetCombinedBlockID( iMetadata ) );					
					
				return true;
			}			
		}
		
		return false;
	}
	
	@Override
	public int GetCombinedBlockID( int iMetadata )
	{
		if ( iMetadata == m_iSubtypeSand )
		{
			return Block.sand.blockID;
		}
		else
		{
			return Block.gravel.blockID;
		}
	}

	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
    	int iIDToDrop = FCBetterThanWolves.fcItemPileGravel.itemID;
    	
    	if ( GetSubtypeFromMetadata( iMetadata ) == m_iSubtypeSand )
    	{
    		iIDToDrop = FCBetterThanWolves.fcItemPileSand.itemID;
    	}
    	
		DropItemsIndividualy( world, i, j, k, iIDToDrop, 3, 0, fChanceOfDrop );
		
		return true;
	}
	
	@Override
    public boolean GetIsUpsideDown( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return false;
    }
    
	@Override
    public boolean GetIsUpsideDown( int iMetadata )
    {
    	return false;
    }
    
	@Override
    public void SetIsUpsideDown( World world, int i, int j, int k, boolean bUpsideDown )
    {
    }
    
	@Override
    public int SetIsUpsideDown( int iMetadata, boolean bUpsideDown )
	{
    	return iMetadata;    	
	}
    
    @Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public int GetFilterableProperties( ItemStack stack )
    {
    	if ( stack.getItemDamage() == m_iSubtypeGravel )
    	{
    		return Item.m_iFilterable_Small;    		
    	}
    	
		return Item.m_iFilterable_Fine;    		
    }
    
    //------------- Class Specific Methods ------------//    
    
    public int GetSubtype( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetSubtypeFromMetadata( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    public int GetSubtypeFromMetadata( int iMetadata )
    {
    	return iMetadata;
    }
    
    public void SetSubtype( World world, int i, int j, int k, int iSubtype )
    {
    	int iMetadata = world.getBlockMetadata( i, j, k ) & 0; // clear old value
    	
		iMetadata |= iSubtype;
    	
    	world.setBlockMetadataWithNotify( i, j, k, iMetadata );
    }
    
	//----------- Client Side Functionality -----------//
    
	private Icon m_IconSand;
	
	@Override
    public void registerIcons( IconRegister register )
    {
        blockIcon = register.registerIcon( "gravel" );
        
        m_IconSand = register.registerIcon( "sand" );
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		int iSubtype = GetSubtypeFromMetadata( iMetadata );
		
		if ( iSubtype == m_iSubtypeSand )
		{
			return m_IconSand;
		}
		
		return blockIcon;
    }
	
	@Override
    public void getSubBlocks( int iBlockID, CreativeTabs creativeTabs, List list )
    {
        list.add( new ItemStack( iBlockID, 1, m_iSubtypeGravel ) );
        list.add( new ItemStack( iBlockID, 1, m_iSubtypeSand ) );
    }
}
