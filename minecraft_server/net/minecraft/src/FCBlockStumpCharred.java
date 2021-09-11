// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockStumpCharred extends Block
{
    public final static float m_fHardness = 3F; // 6 on regular stump
    
    private FCModelBlock m_blockModelsNarrowOneSide[];
    
    public FCBlockStumpCharred( int iBlockID )
    {
        super( iBlockID, FCBetterThanWolves.fcMaterialLog );
        
        setHardness( m_fHardness );
        
		SetAxesEffectiveOn();
		SetChiselsEffectiveOn();
        
        SetBuoyant();
        
        InitModels();       
        
        Block.useNeighborBrightness[iBlockID] = true;
        
        setLightOpacity( 8 );
        
        setStepSound( soundGravelFootstep );
        
        setUnlocalizedName( "fcBlockStumpCharred" );
    }    
    
    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return 0;
    }
    
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }

	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace( World world, int i, int j, int k, Vec3 startRay, Vec3 endRay )
    {
    	return GetCurrentModelForBlock( world, i, j, k ).CollisionRayTrace( world, i, j, k, startRay, endRay );
    }
    
    @Override
	public boolean HasCenterHardPointToFacing( IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency )
	{
		return iFacing <= 1;
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
    		iDamageLevel++;

    		SetDamageLevel( world, i, j, k, iDamageLevel );
    		
    		return true;
    	}
        
    	return false;
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
    public boolean GetCanBlockBeIncinerated( World world, int i, int j, int k )
    {
    	return false;
    }
    
    @Override
    public int GetHarvestToolLevel( IBlockAccess blockAccess, int i, int j, int k )
    {
		return 1000; // always convert, never harvest
    }
    
    //------------- Class Specific Methods ------------//
    
    private final static float m_fRimWidth = ( 1F / 16F );
    
    private final static float m_fLayerHeight = ( 2F / 16F );    
    private final static float m_fFirstLayerHeight = ( 3F / 16F );    
    private final static float m_fLayerWidthGap = ( 1F / 16F );
    
    private FCModelBlock m_tempCurrentModel;
    
    protected void InitModels()
    {
        m_blockModelsNarrowOneSide = new FCModelBlock[4];

        // center colum
        
        for ( int iTempIndex = 0; iTempIndex < 4; iTempIndex++ )
        {
        	FCModelBlock tempNarrowOneSide = m_blockModelsNarrowOneSide[iTempIndex] = new FCModelBlock();
        	
            float fCenterColumnWidthGap = m_fRimWidth + ( m_fLayerWidthGap * iTempIndex );
            float fCenterColumnHeightGap = 0F;
            
            if ( iTempIndex > 0 )
            {
            	fCenterColumnHeightGap = m_fFirstLayerHeight + ( m_fLayerHeight * ( iTempIndex - 1 ) );
            }

            tempNarrowOneSide.AddBox( fCenterColumnWidthGap, fCenterColumnHeightGap, fCenterColumnWidthGap, 
            	1F - fCenterColumnWidthGap, 1F, 1F - fCenterColumnWidthGap );
        }
        
        // first layer
        
        for ( int iTempIndex = 1; iTempIndex < 4; iTempIndex++ )
        {
	        m_blockModelsNarrowOneSide[iTempIndex].AddBox( m_fRimWidth, 0, m_fRimWidth, 1F - m_fRimWidth, m_fFirstLayerHeight, 1F - m_fRimWidth );
        }
        
        // second layer 
        
        float fWidthGap = m_fRimWidth + m_fLayerWidthGap;
        float fHeightGap = m_fFirstLayerHeight;
        
        for ( int iTempIndex = 2; iTempIndex < 4; iTempIndex++ )
        {
	        m_blockModelsNarrowOneSide[iTempIndex].AddBox( fWidthGap, fHeightGap, fWidthGap, 1F - fWidthGap, fHeightGap + m_fLayerHeight, 1F - fWidthGap );
        }
        
    	// third layer
        
        fWidthGap = m_fRimWidth + ( m_fLayerWidthGap * 2 );
        fHeightGap = m_fFirstLayerHeight + m_fLayerHeight;
        
        m_blockModelsNarrowOneSide[3].AddBox( fWidthGap, fHeightGap, fWidthGap, 1F - fWidthGap, fHeightGap + m_fLayerHeight, 1F - fWidthGap );
    }
    
    public void SetDamageLevel( World world, int i, int j, int k, int iDamageLevel )
    {
    	int iMetadata = SetDamageLevel( world.getBlockMetadata( i, j, k ), iDamageLevel );
    	
		world.setBlockMetadataWithNotify(  i, j, k, iMetadata );
    }
    
    public int SetDamageLevel( int iMetadata, int iDamageLevel )
    {
    	iMetadata &= ~3;
    	
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
    
    public FCModelBlock GetCurrentModelForBlock( IBlockAccess blockAccess, int i, int j, int k )
    {
    	int iDamageLevel = GetDamageLevel( blockAccess, i, j, k );
    	
		return m_blockModelsNarrowOneSide[iDamageLevel];
    }

	//----------- Client Side Functionality -----------//
}  
