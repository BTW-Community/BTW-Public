// FCMOD

package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class FCBlockAestheticOpaque extends Block
{
    public final static int m_iSubtypeWicker = 0; // deprecated
    public final static int m_iSubtypeDung = 1; // deprecated
    public final static int m_iSubtypeSteel = 2;
    public final static int m_iSubtypeHellfire = 3;
    public final static int m_iSubtypePadding = 4;
    public final static int m_iSubtypeSoap = 5;
    public final static int m_iSubtypeRope = 6;
    public final static int m_iSubtypeFlint = 7;
    public final static int m_iSubtypeNetherrackWithGrowth = 8;
    public final static int m_iSubtypeWhiteStone = 9;
    public final static int m_iSubtypeWhiteCobble = 10;
    public final static int m_iSubtypeBarrel = 11;
    public final static int m_iSubtypeChoppingBlockDirty = 12;
    public final static int m_iSubtypeChoppingBlockClean = 13;
    public final static int m_iSubtypeEnderBlock = 14;
    public final static int m_iSubtypeBone = 15;
    
    public final static int m_iNumSubtypes = 16;    
    
    private final static float m_fDefaultHardness = 2F;
    
    public FCBlockAestheticOpaque( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialMiscellaneous );
        
        setHardness( m_fDefaultHardness ); 
        SetAxesEffectiveOn( true );        
        SetPicksEffectiveOn( true );       
        
        setStepSound( soundStoneFootstep );
        
		setCreativeTab( CreativeTabs.tabBlock );
		
        setUnlocalizedName( "fcBlockAestheticOpaque" );        
    }

	@Override
	public int damageDropped( int iMetadata )
    {
		if ( iMetadata == m_iSubtypeWicker )
		{
			return 0;
		}
		else if ( iMetadata == m_iSubtypeSteel )
		{
			// the new block type should not have a metadata value
			
			return 0;
		}
		else if ( iMetadata == m_iSubtypeFlint )
		{
			return 0;
		}
		else if ( iMetadata == m_iSubtypeNetherrackWithGrowth )
		{
			return 0;
		}
		else if ( iMetadata == m_iSubtypeWhiteStone )
		{
			return m_iSubtypeWhiteCobble;
		}
		
        return iMetadata; 
    }
    
	@Override
    public int idDropped(int metadata, Random random, int fortuneModifier)
    {
		if (metadata == m_iSubtypeWicker) {
			return FCBetterThanWolves.fcBlockWicker.blockID;
		}
		else if (metadata == m_iSubtypeSteel) {
			// convert to the new steel block
			
			return FCBetterThanWolves.fcSoulforgedSteelBlock.blockID;
		}
		else if (metadata == m_iSubtypeFlint) {
			return Item.flint.itemID;
		}
		else if (metadata == m_iSubtypeNetherrackWithGrowth) {
			return Block.netherrack.blockID;
		}
		
		return blockID;
    }
    
	@Override
    public void dropBlockAsItemWithChance( World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier )
    {
		if ( iMetadata == m_iSubtypeFlint )
		{
	        if ( world.isRemote )
	        {
	            return;
	        }
	        
	        int iNumDropped = 9;
	        
	        for(int k1 = 0; k1 < iNumDropped; k1++)
	        {
	            int iItemID = idDropped( iMetadata, world.rand, iFortuneModifier );
	            
	            if ( iItemID > 0 )
	            {
	                dropBlockAsItem_do( world, i, j, k, new ItemStack( iItemID, 1, damageDropped( iMetadata ) ) );
	            }
	        }
		}
		else
		{
			super.dropBlockAsItemWithChance( world, i, j, k, iMetadata, fChance, iFortuneModifier );
		}
    }

	@Override
    protected boolean canSilkHarvest( int iMetadata )
    {
    	int iSubType = iMetadata;

    	if ( iSubType == m_iSubtypeNetherrackWithGrowth || iSubType == m_iSubtypeSteel  )
    	{
    		return false;
    	}
    	
        return true;
    }    
    
	@Override
    public void onNeighborBlockChange( World world, int i, int j, int k, int iChangedBlockID )
    {
    	int iSubType = world.getBlockMetadata( i, j, k );

    	if ( iSubType == m_iSubtypeNetherrackWithGrowth )
    	{
    		int iBlockAboveID = world.getBlockId( i, j + 1, k );
    		
    		if ( iBlockAboveID != FCBetterThanWolves.fcBlockBloodMoss.blockID )
    		{
    			// convert back to regular netherrack if we don't have a growth above us
    			
    			world.setBlock( i, j, k, Block.netherrack.blockID );
    		}
    	}
    }
	
	@Override
    public boolean DoesInfiniteBurnToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing )
    {
    	int iSubType = blockAccess.getBlockMetadata( i, j, k );

    	return iSubType == m_iSubtypeHellfire;
    }
	
	@Override
    public boolean DoesBlockBreakSaw( World world, int i, int j, int k )
    {
    	int iSubtype = world.getBlockMetadata( i, j, k );

		if ( iSubtype == m_iSubtypeWicker ||
			iSubtype == m_iSubtypeDung ||
			iSubtype == m_iSubtypePadding ||
			iSubtype == m_iSubtypeSoap ||
			iSubtype == m_iSubtypeRope || 
			iSubtype == m_iSubtypeBarrel ||
			iSubtype == m_iSubtypeChoppingBlockDirty ||
			iSubtype == m_iSubtypeChoppingBlockClean ||
			iSubtype == m_iSubtypeBone )
		{
			return false;
		}
		
		return true;
    }
	
	@Override
    public boolean OnBlockSawed( World world, int i, int j, int k )
    {
    	int iSubtype = world.getBlockMetadata( i, j, k );

		if (  iSubtype == m_iSubtypeChoppingBlockDirty ||
			iSubtype == m_iSubtypeChoppingBlockClean )
		{
			return false;
		}
		
		return super.OnBlockSawed( world, i, j, k );
    }
    
	@Override
    public int GetItemIDDroppedOnSaw( World world, int i, int j, int k )
    {
    	int iSubtype = world.getBlockMetadata( i, j, k );
    	
    	if ( iSubtype == m_iSubtypeWicker )
    	{
    		return FCBetterThanWolves.fcBlockWickerSlab.blockID;
    	}
    	else if ( iSubtype == m_iSubtypeBone )
    	{
    		return Item.bone.itemID;
    	}

    	return super.GetItemIDDroppedOnSaw( world, i, j, k );
    }
	
	@Override
    public int GetItemCountDroppedOnSaw( World world, int i, int j, int k )
    {
    	int iSubtype = world.getBlockMetadata( i, j, k );
    	
    	if ( iSubtype == m_iSubtypeWicker )
    	{
    		return 2;
    	}
    	else if ( iSubtype == m_iSubtypeBone )
    	{
    		return 5; // 9 in full block
    	}

    	return super.GetItemCountDroppedOnSaw( world, i, j, k );
    }
    
	@Override
    public float GetMovementModifier( World world, int i, int j, int k )
    {
    	int iSubtype = world.getBlockMetadata( i, j, k );
    	
    	if ( iSubtype == m_iSubtypeDung )
    	{
    		return 1F;
    	}
        
        return 1.2F;
    }
	
    @Override
    public StepSound GetStepSound( World world, int i, int j, int k )
    {
    	int iSubtype = world.getBlockMetadata( i, j, k );
    	
    	if ( iSubtype == m_iSubtypeDung )
    	{
    		return FCBetterThanWolves.fcStepSoundSquish;
    	}
    	else if ( iSubtype == m_iSubtypeBone )
    	{
    		return soundGravelFootstep;
    	}
    	
    	return stepSound;
    }
    
    @Override
    public boolean CanBePistonShoveled( World world, int i, int j, int k )
    {
    	int iSubtype = world.getBlockMetadata( i, j, k );
    	
    	return iSubtype == m_iSubtypeDung || iSubtype == m_iSubtypeBone || iSubtype == m_iSubtypeSoap;
    }
    
    @Override
    public boolean CanToolsStickInBlock( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iSubtype = blockAccess.getBlockMetadata( i, j, k );
    	
    	return iSubtype != m_iSubtypeWicker;
    }
    
	//------------- Class Specific Methods ------------//
    
	//----------- Client Side Functionality -----------//
    
    private Icon m_IconWicker;
    private Icon m_IconDung;    
    private Icon m_IconSteel;
    private Icon m_IconHellfire;
    private Icon m_IconPadding;
    private Icon m_IconSoap;
    private Icon m_IconSoapTop;
    private Icon m_IconRopeSide;
    private Icon m_IconRopeTop;
    private Icon m_IconFlint;
    private Icon m_IconNetherrackWithGrothSide;
    private Icon m_IconNetherrackWithGrothTop;
    private Icon m_IconNetherrackWithGrothBottom;
    private Icon m_IconWhiteStone;
    private Icon m_IconWhiteCobble;
    private Icon m_IconBarrelTop;
    private Icon m_IconBarrelSide;
    private Icon m_IconChoppingBlock;
    private Icon m_IconChoppingBlockDirty;
    private Icon m_IconEnderBlock;
    private Icon m_IconBoneSide;
    private Icon m_IconBoneTop;
    
	@Override
    public void registerIcons( IconRegister register )
    {
        blockIcon = register.registerIcon( "stone" ); // for hit effects
        
        m_IconWicker = register.registerIcon( "fcBlockWicker" );
        m_IconDung = register.registerIcon( "fcBlockDung" );
        m_IconSteel = register.registerIcon( "fcBlockSoulforgedSteel" );
        m_IconHellfire = register.registerIcon( "fcBlockConcentratedHellfire" );
        m_IconPadding = register.registerIcon( "fcBlockPadding" );
        m_IconSoap = register.registerIcon( "fcBlockSoap" );
        m_IconSoapTop = register.registerIcon( "fcBlockSoap_top" );
        m_IconRopeSide = register.registerIcon( "fcBlockRope_side" );
        m_IconRopeTop = register.registerIcon( "fcBlockRope_top" );
        m_IconFlint = register.registerIcon( "bedrock" );
        m_IconNetherrackWithGrothSide = register.registerIcon( "fcBlockNetherrackGrothed_side" );
        m_IconNetherrackWithGrothTop = register.registerIcon( "fcBlockNetherrackGrothed_top" );
        m_IconNetherrackWithGrothBottom = register.registerIcon( "fcBlockNetherrackGrothed_bottom" );
        m_IconWhiteStone = register.registerIcon( "fcBlockWhiteStone" );
        m_IconWhiteCobble = register.registerIcon( "fcBlockWhiteCobble" );
        m_IconBarrelTop = register.registerIcon( "fcBlockBarrel_top" );
        m_IconBarrelSide = register.registerIcon( "fcBlockBarrel_side" );
        m_IconChoppingBlock = register.registerIcon( "fcBlockChoppingBlock" );
        m_IconChoppingBlockDirty = register.registerIcon( "fcBlockChoppingBlock_dirty" );
        m_IconEnderBlock = register.registerIcon( "fcBlockEnder" );
        m_IconBoneSide = register.registerIcon( "fcBlockBone_side" );
        m_IconBoneTop = register.registerIcon( "fcBlockBone_top" );
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetaData )
    {
    	int iSubType = iMetaData;
    	
    	switch ( iSubType )
    	{
	    	case m_iSubtypeWicker:
	    		
    			return m_IconWicker;
    			
	    	case m_iSubtypeDung:
	    		
    			return m_IconDung;
    			
	    	case m_iSubtypeSteel:
	    		
    			return m_IconSteel;
    			
	    	case m_iSubtypeHellfire:
	    		
    			return m_IconHellfire;
    			
	    	case m_iSubtypePadding:
	    		
    			return m_IconPadding;
    			
	    	case m_iSubtypeSoap:
	    		
	    		if ( iSide == 1 )
	    		{
	    			return m_IconSoapTop;
	    		}
	    		else
	    		{
	    			return m_IconSoap;
	    		}
    			
	    	case m_iSubtypeRope:
	    		
	    		if ( iSide < 2 )
	    		{
	    			return m_IconRopeTop;
	    		}
	    		else
	    		{
	    			return m_IconRopeSide;
	    		}
	    		
	    	case m_iSubtypeFlint:
    			
				return m_IconFlint;
				
	    	case m_iSubtypeNetherrackWithGrowth:
	    		
	    		if ( iSide == 0 )
	    		{
	    			return m_IconNetherrackWithGrothBottom;
	    		}
	    		else if ( iSide == 1 )
	    		{
	    			return m_IconNetherrackWithGrothTop;
	    		}
	    		else
	    		{
	    			return m_IconNetherrackWithGrothSide;
	    		}
	    	
	    	case m_iSubtypeWhiteStone:
    			
				return m_IconWhiteStone;
				
	    	case m_iSubtypeWhiteCobble:
    			
				return m_IconWhiteCobble;
				
	    	case m_iSubtypeBarrel:
    			
	    		if ( iSide < 2 )
	    		{
	    			return m_IconBarrelTop;
	    		}
	    		else
	    		{	    		
	    			return m_IconBarrelSide;
	    		}
				
	    	case m_iSubtypeChoppingBlockClean:
    			
    			return m_IconChoppingBlock;
				
	    	case m_iSubtypeChoppingBlockDirty:
    			
    			return m_IconChoppingBlockDirty;
				
	    	case m_iSubtypeEnderBlock:
    			
    			return m_IconEnderBlock;
				
	    	case m_iSubtypeBone:
	    		
	    		if ( iSide < 2 )
	    		{
	    			return m_IconBoneTop;
	    		}
	    		else
	    		{
	    			return m_IconBoneSide;
	    		}
	    		
    		default:
    	
    	    	return blockIcon;
    	}    	
    }
    
	@Override
	public void getSubBlocks(int blockID, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(blockID, 1, m_iSubtypeHellfire));
        list.add(new ItemStack(blockID, 1, m_iSubtypePadding));
        list.add(new ItemStack(blockID, 1, m_iSubtypeSoap));
        list.add(new ItemStack(blockID, 1, m_iSubtypeRope));
        list.add(new ItemStack(blockID, 1, m_iSubtypeFlint));
        list.add(new ItemStack(blockID, 1, m_iSubtypeWhiteStone));
        list.add(new ItemStack(blockID, 1, m_iSubtypeWhiteCobble));
        list.add(new ItemStack(blockID, 1, m_iSubtypeBarrel));
        list.add(new ItemStack(blockID, 1, m_iSubtypeChoppingBlockDirty));
        list.add(new ItemStack(blockID, 1, m_iSubtypeChoppingBlockClean));
        list.add(new ItemStack(blockID, 1, m_iSubtypeEnderBlock));
        list.add(new ItemStack(blockID, 1, m_iSubtypeBone));
    }
	
	@Override
    public int idPicked(World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		
		if (metadata == m_iSubtypeFlint) {
			return blockID;
		}
		
        return idDropped(metadata, world.rand, 0);
    }

	@Override
    public int getDamageValue(World world, int x, int y, int z) {
		// used only by pick block
		int metadata = world.getBlockMetadata(x, y, z);
		
		if (metadata == m_iSubtypeFlint) {
			return m_iSubtypeFlint;
		}
		else if (metadata == m_iSubtypeWhiteStone) {
			return m_iSubtypeWhiteStone;
		}
		
		return super.getDamageValue(world, x, y, z);		
    }
	
	@Override
    public boolean shouldSideBeRendered( IBlockAccess blockAccess, int i, int j, int k, int iSide )
    {
		if ( iSide == 1 && blockAccess.getBlockId( i, j, k ) == FCBetterThanWolves.fcBlockBloodMoss.blockID )
		{
			// this is specifically for netherrack with groth
			
			return false;
		}
		
		return !blockAccess.isBlockOpaqueCube( i, j, k );
    }
}