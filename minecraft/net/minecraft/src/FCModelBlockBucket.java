// FCMOD

package net.minecraft.src;

import java.util.Iterator;

public class FCModelBlockBucket extends FCModelBlock
{
	public static final int m_iAssemblyIDBase = 0;
	public static final int m_iAssemblyIDBody = 1;
	public static final int m_iAssemblyIDRim = 2;
	public static final int m_iAssemblyIDInterior = 3;
	public static final int m_iAssemblyIDContents = 4;
	
	public static final double m_dHeight = ( 8D / 16D );
	public static final double m_dWidth = ( 8D / 16D );
	public static final double m_dHalfWidth = ( m_dWidth / 2D );
	
	public static final double m_dBaseHeight = ( 1D / 16D );
	public static final double m_dBaseWidth = ( 6D / 16D );
	public static final double m_dBaseHalfWidth = ( m_dBaseWidth / 2D );
	
	public static final double m_dBodyWidth = ( 7D / 16D );
	public static final double m_dBodyHalfWidth = ( m_dBodyWidth / 2D );
	
	public static final double m_dRimHeight = ( 1D / 16D );	
	
	public static final double m_dInteriorHeight = ( m_dHeight - m_dBaseHeight - ( 1.5D / 16D ) );
	public static final double m_dInteriorWidth = ( 6D / 16D );
	public static final double m_dInteriorHalfWidth = ( m_dInteriorWidth / 2D );
	
	private static final double m_dMindTheGap = 0.001D; // slight offset used to avoid visual seams
	
	public FCModelBlockBucket()
	{
		super();
	}
	
	@Override
    protected void InitModel()
    {
		FCModelBlock tempModel;
		FCUtilsPrimitiveAABBWithBenefits tempBox;
		
		// base
		
		tempModel = new FCModelBlock( m_iAssemblyIDBase );
		
		tempModel.AddBox( 0.5D - m_dBaseHalfWidth, 0D, 0.5D - m_dBaseHalfWidth,
			0.5D + m_dBaseHalfWidth, m_dBaseHeight, 0.5D + m_dBaseHalfWidth );
		
		AddPrimitive( tempModel );
		
		// body
		
		tempModel = new FCModelBlock( m_iAssemblyIDBody );
		
		tempModel.AddBox( 0.5D - m_dBodyHalfWidth, m_dBaseHeight, 0.5D - m_dBodyHalfWidth,
			0.5D + m_dBodyHalfWidth, m_dHeight - m_dRimHeight, 0.5D + m_dBodyHalfWidth );

		AddPrimitive( tempModel );
		
		// Rim
		
		tempBox = new FCUtilsPrimitiveAABBWithBenefits( 
			0.5D - m_dHalfWidth, m_dHeight - m_dRimHeight, 0.5D - m_dHalfWidth,
			0.5D + m_dHalfWidth, m_dHeight, 0.5D + m_dHalfWidth,			
			m_iAssemblyIDRim );
		
		AddPrimitive( tempBox );
		
		// Interior
		
		tempBox = new FCUtilsPrimitiveAABBWithBenefits( 
			0.5D + m_dInteriorHalfWidth + m_dMindTheGap, 
			m_dHeight, 
			0.5D + m_dInteriorHalfWidth + m_dMindTheGap,
			0.5D - m_dInteriorHalfWidth - m_dMindTheGap, 
			m_dHeight - m_dInteriorHeight, 
			0.5D - m_dInteriorHalfWidth - m_dMindTheGap,
			m_iAssemblyIDInterior );
		
		tempBox.SetForceRenderWithColorMultiplier( true );

		AddPrimitive( tempBox );
    }
	
	//------------- Class Specific Methods ------------//
    
    public static void OffsetModelForFacing( FCModelBlock model, int iFacing )
    {
    	if ( iFacing != 1 )
    	{
			Vec3 offset = GetOffsetForFacing( iFacing );
			
			model.Translate( offset.xCoord, offset.yCoord, offset.zCoord );
    	}
    }

    /**
     * Assumes facing is not 1, where there should be no offset
     */
    public static Vec3 GetOffsetForFacing( int iFacing )
    {
    	if ( iFacing == 0 )
    	{
    		return Vec3.createVectorHelper( 0D, -1D + m_dHeight, 0D );
    	}
    	else
    	{    		
    		Vec3 offset = Vec3.createVectorHelper( 0D, -0.5D + m_dHalfWidth, 
    			-1D + m_dHeight );
			
			offset.RotateAsVectorAroundJToFacing( iFacing );
			
			return offset;
    	}
    }
	
	//----------- Client Side Functionality -----------//
}
