// FCMOD

package net.minecraft.src;

public class FCItemBlockDirtSlab extends FCItemBlockSlab
{
    public FCItemBlockDirtSlab( int iItemID )
    {
        super( iItemID );        
    }

    @Override
    public int getMetadata( int iItemDamage )
    {
		return iItemDamage << 1;    	
    }
    
    @Override
    public String getUnlocalizedName( ItemStack itemstack )
    {
    	switch( itemstack.getItemDamage() )
    	{
    		case FCBlockDirtSlab.m_iSubtypePackedEarth:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("packed").toString();
                
    		case FCBlockDirtSlab.m_iSubtypeGrass:
    			
                return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append("grass").toString();
                
			default:
				
				return super.getUnlocalizedName();
    	}
    }
    
    //------------- FCItemBlockSlab ------------//
    
    @Override
    public boolean canCombineWithBlock( World world, int i, int j, int k, int iItemDamage )
    {
        int iBlockID = world.getBlockId( i, j, k );
        
        if ( iBlockID == FCBetterThanWolves.fcBlockDirtSlab.blockID )
        {
        	int iTargetSubtype = ((FCBlockDirtSlab)FCBetterThanWolves.fcBlockDirtSlab).GetSubtype( world, i, j, k );
        	
        	if ( iTargetSubtype == FCBlockDirtSlab.m_iSubtypePackedEarth || iItemDamage == FCBlockDirtSlab.m_iSubtypePackedEarth  )
        	{
        		if ( iTargetSubtype == iItemDamage )
        		{
        			return true;
        		}
        	}
        	else
        	{
        		return true;
        	}
        }
        else if ( iBlockID == FCBetterThanWolves.fcBlockMyceliumSlab.blockID )
        {
        	if ( iItemDamage != FCBlockDirtSlab.m_iSubtypePackedEarth )
        	{
        		return true;
        	}
        }
        
    	return false;
    }
    
    @Override
    public boolean convertToFullBlock( World world, int i, int j, int k )
    {
        int iBlockID = world.getBlockId( i, j, k );
        
        if ( iBlockID == FCBetterThanWolves.fcBlockDirtSlab.blockID )
        {
        	FCBlockDirtSlab slabBlock = (FCBlockDirtSlab)(FCBetterThanWolves.fcBlockDirtSlab);
        	
        	boolean bIsTargetUpsideDown = slabBlock.GetIsUpsideDown( world, i, j, k );
        	int iTargetSubType = slabBlock.GetSubtype( world, i, j, k  );
        	
        	int iNewBlockID = Block.dirt.blockID;
        	int iNewMetadata = 0;
        	
        	if ( iTargetSubType == FCBlockDirtSlab.m_iSubtypePackedEarth )
        	{
        		iNewBlockID = FCBetterThanWolves.fcBlockAestheticOpaqueEarth.blockID;
        		iNewMetadata = FCBlockAestheticOpaqueEarth.m_iSubtypePackedEarth;
        	}
        	else if ( bIsTargetUpsideDown )
        	{
        		if ( iTargetSubType == FCBlockDirtSlab.m_iSubtypeGrass )
        		{
        			iNewBlockID = Block.grass.blockID;
        		}
        	}
        	
        	return world.setBlockAndMetadataWithNotify( i, j, k, iNewBlockID, iNewMetadata );
        }
        else if ( iBlockID == FCBetterThanWolves.fcBlockMyceliumSlab.blockID )
        {
        	if ( ((FCBlockMyceliumSlab)FCBetterThanWolves.fcBlockMyceliumSlab).GetIsUpsideDown( world, i, j, k ) )
        	{
        		return world.setBlockAndMetadataWithNotify( i, j, k, Block.mycelium.blockID, 0 );
        	}
        	else
        	{
        		return world.setBlockAndMetadataWithNotify( i, j, k, Block.dirt.blockID, 0 );
        	}
        }
        	
    	return false;
    }
    
    //------------- Class Specific Methods ------------//
}
