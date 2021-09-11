// FCMOD

package net.minecraft.src;

import java.util.List;

public class FCBlockOre extends BlockOre
{
    public FCBlockOre( int iBlockID )
    {
        super( iBlockID );
        
        SetPicksEffectiveOn();
    }

    @Override
    public boolean HasStrata()
    {
    	return true;
    }
    
    @Override
    public int GetMetadataConversionForStrataLevel( int iLevel, int iMetadata )
    {
    	return iLevel;
    }
    
    @Override
    public float getBlockHardness( World world, int i, int j, int k )
    {
    	int iStrata = GetStrata( world, i, j, k );
    	
    	if ( iStrata != 0 )
    	{
    		// normal ore has a hardness of 3
    		
	    	if ( iStrata == 1 )
	    	{
	    		return 4.0F;
	    	}
	    	else
	    	{
	    		return 6.0F; 
	    	}
    	}
    	
        return super.getBlockHardness( world, i, j, k );
    }
    
    @Override
    public float getExplosionResistance( Entity entity, World world, int i, int j, int k )
    {
    	int iStrata = GetStrata( world, i, j, k );
    	
    	if ( iStrata != 0 )
    	{
    		// normal ore has a resistance of 5
    		
	    	if ( iStrata == 1 )
	    	{
	    		return 7F * ( 3.0F / 5.0F );
	    	}
	    	else
	    	{
	    		return  10F * ( 3.0F / 5.0F );
	    	}
    	}
    	
        return super.getExplosionResistance( entity, world, i, j, k );
    }
    
    @Override
    public boolean IsNaturalStone( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return true;
    }
    
    //------------- Class Specific Methods ------------//
	
    public int GetStrata( IBlockAccess blockAccess, int i, int j, int k )
    {
		return GetStrata( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    public int GetStrata( int iMetadata )
    {
    	return iMetadata & 3;
    }
    
    public int GetRequiredToolLevelForStrata( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iStrata = GetStrata( blockAccess, i, j, k );
    	
    	if ( iStrata > 1 )
    	{
    		return iStrata + 1;
    	}
    	
    	return 2;
    }
    
	//----------- Client Side Functionality -----------//
}
