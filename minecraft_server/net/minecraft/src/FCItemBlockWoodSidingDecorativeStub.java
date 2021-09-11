// FCMOD

package net.minecraft.src;

public class FCItemBlockWoodSidingDecorativeStub extends ItemBlock
{
	public static final int m_iTypeBench = 0;
	public static final int m_iTypeFence = 1;
	
    public FCItemBlockWoodSidingDecorativeStub( int iItemID )
    {
        super( iItemID );
        
        setHasSubtypes( true );        
    }
    
    @Override
    public int GetBlockIDToPlace( int iItemDamage, int iFacing, float fClickX, float fClickY, float fClickZ )
    {
    	int iWoodType = GetWoodType( iItemDamage );
    	
    	switch ( iWoodType )
    	{
    		case 0: // oak
    			
    			return FCBetterThanWolves.fcBlockWoodOakSidingAndCorner.blockID;
    			
    		case 1: // spruce
    			
    			return FCBetterThanWolves.fcBlockWoodSpruceSidingAndCorner.blockID;
    			
    		case 2: // birch
    			
    			return FCBetterThanWolves.fcBlockWoodBirchSidingAndCorner.blockID;
    			
    		case 3: // jungle
    			
    			return FCBetterThanWolves.fcBlockWoodJungleSidingAndCorner.blockID;
    			
    		default: // blood
    			
    			return FCBetterThanWolves.fcBlockWoodBloodSidingAndCorner.blockID;    			
    	}
    }
    
    @Override
    public int getMetadata( int iItemDamage )
    {
    	int iBlockType = GetBlockType( iItemDamage );
    	
    	if ( iBlockType == m_iTypeBench )
    	{
    		return FCBlockSidingAndCornerAndDecorative.m_iSubtypeBench;
    	}
    	else // fence
    	{
    		return FCBlockSidingAndCornerAndDecorative.m_iSubtypeFence;
    	}
    }
    
    @Override
    public String getUnlocalizedName( ItemStack itemstack )
    {
    	int iWoodType = GetWoodType( itemstack.getItemDamage() );
    	
    	String sWoodTypeName;
    	
    	if( iWoodType == 0 )
    	{
    		sWoodTypeName = "oak";
    	}
    	else if( iWoodType == 1 )
    	{
    		sWoodTypeName = "spruce";
    	}
    	else if( iWoodType == 2 )
    	{
    		sWoodTypeName = "birch";
    	}
    	else if( iWoodType == 3 )
    	{
    		sWoodTypeName = "jungle";
    	}
    	else // 4
    	{
    		sWoodTypeName = "blood";
    	}
    	
    	int iBlockType = itemstack.getItemDamage() >> 2;
    	
    	String sBlockTypeName;
    	
    	if ( iBlockType == m_iTypeBench )
    	{
    		sBlockTypeName = "bench";
    	}
    	else // fence
    	{
    		sBlockTypeName = "fence";    		
    	}
    	
    	return (new StringBuilder()).append(super.getUnlocalizedName()).append(".").append(sWoodTypeName).append(".").append(sBlockTypeName).toString();
    }
    
    @Override
    public int GetFurnaceBurnTime( int iItemDamage )
    {
		return FCBlockPlanks.GetFurnaceBurnTimeByWoodType( GetWoodType( iItemDamage ) ) / 2;
    }
    
    //------------- Class Specific Methods ------------//
	
    static public int GetWoodType( int iItemDamage )
    {
    	int iWoodType = ( iItemDamage & 3 ) | ( ( iItemDamage >> 4 ) << 2 );
    	
    	return iWoodType;    	
    }
    
    static public int GetBlockType( int iItemDamage )
    {
    	int iBlockType = ( iItemDamage >> 2 ) & 3;
    	
    	return iBlockType;
    }
    
    static public int GetItemDamageForType( int iWoodType, int iBlockType )
    {
    	int iItemDamage = ( iWoodType & 3 ) | ( ( iWoodType >> 2 ) << 4 ) | ( iBlockType << 2 );
    	
    	return iItemDamage;
    }
}