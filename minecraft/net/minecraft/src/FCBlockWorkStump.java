// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockWorkStump extends Block
{
    protected FCBlockWorkStump( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialLog );
        
		setHardness( 1.25F ); // log hardness
		
		SetFireProperties( FCEnumFlammability.LOGS );
		
        setUnlocalizedName( "fcBlockWorkStump" );
    }
    
    @Override
    public float getBlockHardness( World world, int i, int j, int k )
    {
    	// doing it this way instead of just setting the hardness in the constructor to replicate behavior of log stumps
    	
        return super.getBlockHardness( world, i, j, k ) * 3; 
    }
    
	@Override
    public boolean onBlockActivated( World world, int i, int j, int k, EntityPlayer player, int iFacing, float fClickX, float fClickY, float fClickZ )
    {
		// prevent access if solid block above
		
		if ( !world.isRemote && !FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing( world, i, j + 1, k, 0 ) )
		{
            player.displayGUIWorkbench( i, j, k );
		}			
		
        return true;
    }
	
	@Override
	public boolean DropComponentItemsOnBadBreak( World world, int i, int j, int k, int iMetadata, float fChanceOfDrop )
	{
		DropItemsIndividualy( world, i, j, k, FCBetterThanWolves.fcItemSawDust.itemID, 6, 0, fChanceOfDrop );
		
		return true;
	}
	
	@Override
    public void dropBlockAsItemWithChance( World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier )
    {
        if ( !world.isRemote )
        {        
            dropBlockAsItem_do( world, i, j, k, new ItemStack( FCBetterThanWolves.fcItemSawDust, 3, 0 ) );
            
            dropBlockAsItem_do( world, i, j, k, new ItemStack( Block.planks.blockID, 1, 0 ) );
        }
    }
	
    @Override
    public boolean CanConvertBlock( ItemStack stack, World world, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public boolean ConvertBlock( ItemStack stack, World world, int i, int j, int k, int iFromSide )
    {
    	int iOldMetadata = world.getBlockMetadata( i, j, k );
    	int iNewMetadata = FCBetterThanWolves.fcBlockLogDamaged.SetIsStump( 0 );
    	
    	world.setBlockAndMetadataWithNotify( i, j, k, FCBetterThanWolves.fcBlockLogDamaged.blockID, iNewMetadata );
    	
    	if ( !world.isRemote )
    	{
            FCUtilsItem.EjectStackFromBlockTowardsFacing( world, i, j, k, new ItemStack( FCBetterThanWolves.fcItemBark, 1, iOldMetadata & 3 ), iFromSide );
    	}
    	
    	return true;
    }
    
    @Override
    public boolean GetIsProblemToRemove( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public boolean GetDoesStumpRemoverWorkOnBlock( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public ItemStack GetStackRetrievedByBlockDispenser( World world, int i, int j, int k )
    {
    	return Block.wood.GetStackRetrievedByBlockDispenser( world, i, j, k );
    }
    
	//----------- Client Side Functionality -----------//
    
    public static final String[] m_sideTextureNames = new String[] { 
    	"fcBlockWorkStumpOak", 
    	"fcBlockWorkStumpSpruce", 
    	"fcBlockWorkStumpBirch", 
    	"fcBlockWorkStumpJungle" 
	};
    
    private Icon[] m_iconSideArray;
    private Icon m_iconTop;
    
    @Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		if ( iSide > 1 )
		{
			return m_iconSideArray[iMetadata & 3];
		}
		else if ( iSide == 1 )
		{
			return m_iconTop;
		}
		
		return super.getIcon( iSide, iMetadata );
    }    
    
    @Override
    public void registerIcons( IconRegister register )
    {
    	blockIcon = register.registerIcon( "fcBlockTrunkTop" );
    	m_iconTop = register.registerIcon( "workbench_top" );
    	
    	m_iconSideArray = new Icon[m_sideTextureNames.length];

        for ( int iTextureID = 0; iTextureID < m_iconSideArray.length; iTextureID++ )
        {
        	m_iconSideArray[iTextureID] = register.registerIcon( m_sideTextureNames[iTextureID] );
        }
    }
}
