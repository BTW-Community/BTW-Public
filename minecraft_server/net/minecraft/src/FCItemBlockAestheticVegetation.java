//FCMOD 

package net.minecraft.src;

import java.util.List;

public class FCItemBlockAestheticVegetation extends ItemBlock
{
    public FCItemBlockAestheticVegetation( int iItemID )
    {
        super( iItemID );
        
        setMaxDamage( 0 );
        setHasSubtypes(true);
        
        setUnlocalizedName( "fcAestheticVegetation" );
    }

    @Override
    public int getMetadata( int iItemDamage )
    {
    	if ( iItemDamage == FCBlockAestheticVegetation.m_iSubtypeBloodLeaves )
    	{
    		// substitute the new block type for the old
    		
    		return 4;
    	}
    	
		return iItemDamage;    	
    }
    
    @Override
    public String getUnlocalizedName( ItemStack itemstack )
    {
    	switch( itemstack.getItemDamage() )
    	{
    		case FCBlockAestheticVegetation.m_iSubtypeVineTrap:
    		case FCBlockAestheticVegetation.m_iSubtypeVineTrapTriggeredByEntity:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("vinetrap").toString();
    			
    		case FCBlockAestheticVegetation.m_iSubtypeBloodWoodSapling:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("bloodwoodsapling").toString();
                
			case FCBlockAestheticVegetation.m_iSubtypeBloodLeaves:				

                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("bloodleaves").toString();                
    			
			default:
				
				return super.getUnlocalizedName();
    	}
    }
    
    @Override
    public boolean onItemUse( ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ )    
    {
    	int iSubtype = itemStack.getItemDamage();
    	
    	if ( iSubtype == FCBlockAestheticVegetation.m_iSubtypeBloodWoodSapling )
    	{
	        if ( iFacing != 1)
	        {
	            return false;
	        }
	        
	        if (  player != null )
	        {
	            if ( !player.canPlayerEdit( i, j, k, iFacing, itemStack ) || !player.canPlayerEdit( i, j + 1, k, iFacing, itemStack ) )
		        {
		            return false;
		        }
	        }	        
	            	
	        int iTargetBlockID = world.getBlockId( i, j, k );
	        
	        boolean bValidBlockForGrowth = false;
	        
	        if ( iTargetBlockID == Block.slowSand.blockID )
	        {
	        	bValidBlockForGrowth = true;
	        }
    		else if ( iTargetBlockID == FCBetterThanWolves.fcPlanter.blockID )
    		{
    			if ( ((FCBlockPlanter)FCBetterThanWolves.fcPlanter).GetPlanterType( world, i, j, k ) == FCBlockPlanter.m_iTypeSoulSand )
    			{
    	        	bValidBlockForGrowth = true;
    			}
    		}
	        
	        if ( bValidBlockForGrowth && world.isAirBlock(i, j + 1, k))
	        {
	            world.setBlockAndMetadataWithNotify( i, j + 1, k, FCBetterThanWolves.fcAestheticVegetation.blockID, FCBlockAestheticVegetation.m_iSubtypeBloodWoodSapling );
	            
	            itemStack.stackSize--;
	            
    	        // verify if we're in the nether
    	        
    	        WorldChunkManager worldchunkmanager = world.getWorldChunkManager();
    	        
    	        if ( worldchunkmanager != null )
    	        {
    	            BiomeGenBase biomegenbase = worldchunkmanager.getBiomeGenAt(i, k);
    	            
    	            if ( biomegenbase instanceof BiomeGenHell )
    	            {
    		            AngerPigmen( world, player );
    	            }
    	        }
	            
	            return true;
	        } 
	        else
	        {
	            return false;
	        }
    	}
    	else
    	{
    		return super.onItemUse( itemStack, player, world, i, j, k, iFacing, fClickX, fClickY, fClickZ );
    	}
    }
    
    @Override
    public int GetBlockIDToPlace( int iItemDamage, int iFacing, float fClickX, float fClickY, float fClickZ )
    {
    	if ( iItemDamage == FCBlockAestheticVegetation.m_iSubtypeBloodLeaves )
    	{
    		// substitute the new block type for the old
    		
    		return FCBetterThanWolves.fcBlockBloodLeaves.blockID;
    	}
    	
    	return super.GetBlockIDToPlace( iItemDamage, iFacing, fClickX, fClickY, fClickZ );
    }
    
    //------------- Class Specific Methods ------------//
    
    private void AngerPigmen( World world, EntityPlayer entityPlayer )
    {
        List list = world.getEntitiesWithinAABB( FCEntityPigZombie.class, entityPlayer.boundingBox.expand( 32D, 32D, 32D ) );
        
        for( int tempIndex = 0; tempIndex < list.size(); tempIndex++ )
        {
            Entity targetEntity = (Entity)list.get( tempIndex );
            
            targetEntity.attackEntityFrom( DamageSource.causePlayerDamage( entityPlayer ), 0 );
            
        }
    }
    
	//----------- Client Side Functionality -----------//
}