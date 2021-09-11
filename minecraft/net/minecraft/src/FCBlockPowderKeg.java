// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockPowderKeg extends BlockTNT
{
	public FCBlockPowderKeg( int iBlockID )
	{
		super( iBlockID );
		
		setHardness( 0F );
		
		SetBuoyant();
		
		SetFireProperties( FCEnumFlammability.EXPLOSIVES );		
		
		setStepSound( soundGrassFootstep );
		
		setUnlocalizedName( "tnt" );
	}
	
	@Override 
    public void onBlockDestroyedByExplosion(World par1World, int par2, int par3, int par4, Explosion par5Explosion)
    {
		// override so that explosion param can be null
        if (!par1World.isRemote)
        {
        	EntityLiving explosionOwner = null;
        	
        	if ( par5Explosion != null )
        	{
        		explosionOwner = par5Explosion.func_94613_c();
        	}
        	
            EntityTNTPrimed var6 = new EntityTNTPrimed(par1World, (double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), explosionOwner );
            var6.fuse = par1World.rand.nextInt(var6.fuse / 4) + var6.fuse / 8;
            par1World.spawnEntityInWorld(var6);
        }
    }
    
	@Override 
    public void OnDestroyedByFire( World world, int i, int j, int k, int iFireAge, boolean bForcedFireSpread )
    {
		super.OnDestroyedByFire( world, i, j, k, iFireAge, bForcedFireSpread );
		
        onBlockDestroyedByPlayer( world, i, j, k, 1 );
    }
    
	//----------- Client Side Functionality -----------//
	
    private Icon[] m_IconBySideArray = new Icon[6];
    
	@Override
    public void registerIcons( IconRegister register )
    {
		Icon bottomIcon = register.registerIcon( "fcBlockPowderKeg_bottom" );
		
        blockIcon = bottomIcon; // for hit effects
        
        m_IconBySideArray[0] = bottomIcon;
        m_IconBySideArray[1] = register.registerIcon( "fcBlockPowderKeg_top" );
        
        Icon sideIcon = register.registerIcon( "fcBlockPowderKeg_side" );
        
        m_IconBySideArray[2] = sideIcon;
        m_IconBySideArray[3] = sideIcon;
        m_IconBySideArray[4] = sideIcon;
        m_IconBySideArray[5] = sideIcon;
    }
	
	@Override
    public Icon getIcon( int iSide, int iMetadata )
    {
		return m_IconBySideArray[iSide];
    }	
}
