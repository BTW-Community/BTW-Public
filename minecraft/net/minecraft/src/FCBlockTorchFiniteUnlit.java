// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockTorchFiniteUnlit extends FCBlockTorchBaseUnlit
{
    protected FCBlockTorchFiniteUnlit( int iBlockID )
    {
    	super( iBlockID );    	
    	
    	setUnlocalizedName( "fcBlockTorchFiniteIdle" );
    }
    
	@Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
		if ( GetIsBurnedOut( iMetadata ) )
		{
			return 0;
		}
		
    	return super.idDropped( iMetadata, rand, iFortuneModifier );
    }
	
	@Override
    public boolean GetCanBeSetOnFireDirectly( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return !GetIsBurnedOut( blockAccess, i, j, k );
    }
    
	@Override
    public boolean SetOnFireDirectly( World world, int i, int j, int k )
    {
		if ( !GetIsBurnedOut( world, i, j, k ) )
		{
			if ( IsRainingOnTorch( world, i, j, k ) )
			{
	            world.playAuxSFX( FCBetterThanWolves.m_iFireFizzSoundAuxFXID, i, j, k, 0 );
	            
	            return true;
			}
			
			return super.SetOnFireDirectly( world, i, j, k );
		}
		
		return false;
    }
	
	@Override
    public int GetChanceOfFireSpreadingDirectlyTo( IBlockAccess blockAccess, int i, int j, int k )
    {
		if ( GetIsBurnedOut( blockAccess, i, j, k ) )
		{
			return 0; // same chance as leaves and other highly flammable objects
		}

		return super.GetChanceOfFireSpreadingDirectlyTo( blockAccess, i, j, k );
    }
    
	@Override
	protected int GetLitBlockID()
	{
		return FCBetterThanWolves.fcBlockTorchFiniteBurning.blockID;
	}
	
    //------------- Class Specific Methods ------------//    
    
	public void SetIsBurnedOut( World world, int i, int j, int k, boolean bBurnedOut )
	{
		int iMetadata = SetIsBurnedOut( world.getBlockMetadata( i, j, k ), bBurnedOut );
		
		world.setBlockMetadataWithNotify( i, j, k, iMetadata );
	}
	
	static public int SetIsBurnedOut( int iMetadata, boolean bIsBurnedOut )
	{
		if ( bIsBurnedOut )
		{
			iMetadata |= 8;
		}
		else
		{
			iMetadata &= (~8);
		}
		
		return iMetadata;
	}
    
	public boolean GetIsBurnedOut( IBlockAccess blockAccess, int i, int j, int k )
	{
		return GetIsBurnedOut( blockAccess.getBlockMetadata( i, j, k ) );
	}
	
	static public boolean GetIsBurnedOut( int iMetadata )
	{
		return ( iMetadata & 8 ) != 0;
	}
	
	//----------- Client Side Functionality -----------//
	
    private Icon m_burnedIcon;
    
	@Override
    public void registerIcons( IconRegister register )
    {
		super.registerIcons( register );
		
		m_burnedIcon = register.registerIcon( "fcBlockTorchFiniteIdle_burned" );		
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		if ( GetIsBurnedOut( iMetadata ) )
		{
			return m_burnedIcon;
		}
		
		return super.getIcon( iSide, iMetadata );
    }
}
    
