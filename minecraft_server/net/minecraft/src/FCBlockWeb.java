// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockWeb extends BlockWeb
{
    public FCBlockWeb( int iBlockID )
    {
    	super( iBlockID );
        
        SetAxesEffectiveOn( true );        
        SetChiselsEffectiveOn( true );  
        
        setHardness( 4F );
        SetBuoyant();
    	
    	setStepSound( FCBetterThanWolves.fcStepSoundSquish );
        
        setUnlocalizedName( "web" );        
        
        setCreativeTab( null ); // creative tab has to be set for indivdual blocks, due to legacy vanilla block using this class
    }
    
	@Override
    public void harvestBlock( World world, EntityPlayer player, int i, int j, int k, int iMetadata )
    {
        if ( player.getCurrentEquippedItem() != null && 
    		player.getCurrentEquippedItem().itemID == Item.shears.itemID && 
    		GetDamageLevel( iMetadata ) == 0 )
        {
            player.addStat( StatList.mineBlockStatArray[this.blockID], 1 );
            
            dropBlockAsItem_do( world, i, j, k, new ItemStack( FCBetterThanWolves.fcBlockWeb, 1, 0 ) );            
        } 
        else
        {
            super.harvestBlock( world, player, i, j, k, iMetadata );
        }
    }

	@Override
    public int GetEfficientToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return 1;
    }
    
	@Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
		// remove string drop so that cutting webs with swords or axes only destroys the web
		
        return 0;
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
    	int iDamageLevel = GetDamageLevel( iOldMetadata );
    		
    	if ( iDamageLevel < 3 )
    	{
        	SetDamageLevel( world, i, j, k, iDamageLevel + 1 );
        	
        	return true;
    	}
    	else if ( !world.isRemote && IsEffectiveItemConversionTool( stack, world, i, j, k ) )
    	{
            world.playSoundEffect( (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 
            	"random.bow", 0.75F + world.rand.nextFloat() * 0.25F, world.rand.nextFloat() * 0.25F + 1.25F );
			
    		FCUtilsItem.DropStackAsIfBlockHarvested( world, i, j, k, new ItemStack( Item.silk, 1 ) );
    	}
    	
    	return false;
    }

    //------------- Class Specific Methods ------------//
    
    public void SetDamageLevel( World world, int i, int j, int k, int iDamageLevel )
    {
    	int iMetadata = SetDamageLevel( world.getBlockMetadata( i, j, k ), iDamageLevel );
    	
		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
    }
    
    public int SetDamageLevel( int iMetadata, int iDamageLevel )
    {
    	iMetadata &= (~3);
    	
    	return iMetadata | iDamageLevel;
    }
    
    public int GetDamageLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetDamageLevel( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    public int GetDamageLevel( int iMetadata )
    {
    	return iMetadata & 3;
    }
    
    public boolean IsEffectiveItemConversionTool( ItemStack stack, World world, int i, int j, int k )
    {
    	if ( stack != null && stack.getItem() instanceof FCItemChisel )
    	{
			int iToolLevel = ((FCItemChisel)stack.getItem()).toolMaterial.getHarvestLevel();
			
			return iToolLevel >= GetEfficientToolLevel( world, i, j, k );
    	}  
    	
    	return false;
    }
    
	//----------- Client Side Functionality -----------//
}
    
