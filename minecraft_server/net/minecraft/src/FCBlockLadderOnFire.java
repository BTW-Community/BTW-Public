// FCMOD

package net.minecraft.src;

import java.util.Random;

public class FCBlockLadderOnFire extends FCBlockLadderBase
{
	private static final int m_iChanceOfLightingLadderAbove = 4;
	
	private static final int m_iTickRate = 60;
	private static final int m_iTickRateVariance = 30;
	
    protected FCBlockLadderOnFire( int iBlockID )
    {
        super( iBlockID );
        
        setLightValue( 1F );
        
        setTickRandomly( true );
        
        setUnlocalizedName( "fcBlockLadderOnFire" );
    }

    @Override
    public int idDropped( int iMetadata, Random rand, int iFortuneModifier )
    {
        return 0;
    }
    
	@Override
    public int tickRate( World world )
    {
    	return m_iTickRate;
    }
    
	@Override
    public void onBlockAdded( World world, int i, int j, int k )
    {
        world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) + world.rand.nextInt( m_iTickRateVariance ) );
    }
    	
    @Override
    public void updateTick( World world, int i, int j, int k, Random rand )
    {
    	if ( IsRainingOnLadder( world, i, j, k ) )
    	{
    		Extinguish( world, i, j, k );
    	}
    	else
    	{
            FCBlockFire.CheckForFireSpreadFromLocation( world, i, j, k, rand, 0 );
            
            int iCounter = GetBurnCounter( world, i, j, k );
            
            if ( iCounter < 3 )
            {
                if ( rand.nextInt( m_iChanceOfLightingLadderAbove ) == 0 )
                {
            		LightLadderAtLocationIfPresent( world, i, j + 1, k );
                }
                    
            	SetBurnCounter( world, i, j, k, iCounter + 1 );
            	
                world.scheduleBlockUpdate( i, j, k, blockID, tickRate( world ) + rand.nextInt( m_iTickRateVariance ) );
            }
            else
            {
        		LightLadderAtLocationIfPresent( world, i, j + 1, k );
        		
            	world.setBlockToAir( i, j, k );
            }
    	}
    }
    
    @Override
    public boolean GetDoesFireDamageToEntities( World world, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public boolean GetCanBlockLightItemOnFire( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return true;
    }
    
    @Override
    public boolean CanBeCrushedByFallingEntity( World world, int i, int j, int k, EntityFallingSand entity )
    {
    	return true;
    }
    
    //------------- Class Specific Methods ------------//
    
    protected void LightLadderAtLocationIfPresent( World world, int i, int j, int k )
    {
		int iBlockID = world.getBlockId( i, j, k );
		
		if ( iBlockID == FCBetterThanWolves.fcBlockLadder.blockID )
		{
			FCBetterThanWolves.fcBlockLadder.SetOnFireDirectly( world, i, j, k );
		}
    }
    
    protected void Extinguish( World world, int i, int j, int k )
    {
    	int iNewMetadata = FCBetterThanWolves.fcBlockLadder.SetFacing( 0, GetFacing( world, i, j, k ) ); 
    		
    	world.setBlockAndMetadataWithNotify( i, j, k, FCBetterThanWolves.fcBlockLadder.blockID, iNewMetadata );
    }
    
    protected boolean IsRainingOnLadder( World world, int i, int j, int k )
    {
    	return world.IsRainingAtPos( i, j, k );
    }
    
    protected int GetBurnCounter( IBlockAccess blockAccess, int i, int j, int k )
    {
    	return GetBurnCounter( blockAccess.getBlockMetadata( i, j, k ) );
    }
    
    protected int GetBurnCounter( int iMetadata )
    {
    	return ( iMetadata >> 2 ) & 3;
    }
    
    protected void SetBurnCounter( World world, int i, int j, int k, int iCounter )
    {
    	int iMetadata = SetBurnCounter( world.getBlockMetadata( i, j, k ), iCounter );
    	
    	world.setBlockMetadata( i, j, k, iMetadata );
    }
    
    protected int SetBurnCounter( int iMetadata, int iCounter )
    {
    	iMetadata &= ~12;
    	
    	return iMetadata | ( iCounter << 2 );
    }
    
	//----------- Client Side Functionality -----------//
}
