// FCMOD

package net.minecraft.src;

public class FCModelBlockShovel extends FCModelBlock
{
	public static final float m_fSideThickness = ( 1F / 16F );
	public static final float m_fSlopHeight = ( 2F / 16F );
	public static final float m_fBackSlopHeight = ( 4F / 16F );
	public static final float m_fSlopeCollisionHeight = ( 0.25F / 16F );
	
	public static final float m_fSlopeMiddleMajorGap = ( 6F / 16F );
	
	public static final double m_dMindTheGap = 0.001D;
	
	public FCModelBlock m_rayTraceModel; // simplified collision model to cut down on ray trace overhead
	public FCModelBlock m_collisionModel; // stair step model for climbing
	
	@Override
    protected void InitModel()
    {
		m_rayTraceModel = new FCModelBlock();
		m_collisionModel = new FCModelBlock();
		
		// Side walls
		
    	AddBox( 0D, 0D, 0.5D, m_fSideThickness, 1D, 1D );
    	AddBox( 0D, 0D, 0D, m_fSideThickness, 0.5D, 0.5D );
    	
    	m_rayTraceModel.AddBox( 0D, 0D, 0.5D, m_fSideThickness, 1D, 1D );
    	m_rayTraceModel.AddBox( 0D, 0D, 0D, m_fSideThickness, 0.5D, 0.5D );
    	
    	AddBox( 1D - m_fSideThickness, 0D, 0.5D, 1D, 1D, 1D );
    	AddBox( 1D - m_fSideThickness, 0D, 0D, 1D, 0.5D, 0.5D );
    	
    	m_rayTraceModel.AddBox( 1D - m_fSideThickness, 0D, 0.5D, 1D, 1D, 1D );
    	m_rayTraceModel.AddBox( 1D - m_fSideThickness, 0D, 0D, 1D, 0.5D, 0.5D );

    	// Wedge slope
    	
    	AddPrimitive( ( new FCUtilsPrimitiveQuad(
    		Vec3.createVectorHelper( m_fSideThickness, 0D, 0D ),
    		Vec3.createVectorHelper( m_fSideThickness, m_fSlopHeight, 1D - m_fSlopeMiddleMajorGap ),
    		Vec3.createVectorHelper( 1D - m_fSideThickness, m_fSlopHeight, 1D - m_fSlopeMiddleMajorGap ),
    		Vec3.createVectorHelper( 1D - m_fSideThickness, 0D, 0D )
    		) ).SetIconIndex( 1 ).SetUVFractions( m_fSideThickness, 0F, 1F - m_fSideThickness, 1F - m_fSlopeMiddleMajorGap ) );
    	
    	// Wedge base
    	
    	AddPrimitive( ( new FCUtilsPrimitiveQuad(
    		Vec3.createVectorHelper( 1D - m_fSideThickness, 0D, 0D ),
    		Vec3.createVectorHelper( 1D - m_fSideThickness, 0D, 1D - m_fBackSlopHeight ),
    		Vec3.createVectorHelper( m_fSideThickness, 0D, 1D - m_fBackSlopHeight ),
    		Vec3.createVectorHelper( m_fSideThickness, 0D, 0D )
    		) ).SetIconIndex( 2 ).SetUVFractions( m_fSideThickness, 0F, 1F - m_fSideThickness, 1F - m_fBackSlopHeight ) );
    	
    	m_rayTraceModel.AddBox( 0D, 0D, 0D, 1D, m_fSlopeCollisionHeight, 1D - m_fBackSlopHeight );
    	
    	// Top Wedge slope
    	
    	AddPrimitive( ( new FCUtilsPrimitiveQuad(
    		Vec3.createVectorHelper( 1D - m_fSideThickness, 1D, 1D ),
    		Vec3.createVectorHelper( 1D - m_fSideThickness, m_fSlopeMiddleMajorGap, 1D - m_fSlopHeight ),
    		Vec3.createVectorHelper( m_fSideThickness, m_fSlopeMiddleMajorGap, 1D - m_fSlopHeight ),
    		Vec3.createVectorHelper( m_fSideThickness, 1D, 1D )
    		) ).SetIconIndex( 1 ).SetUVFractions( m_fSideThickness, 0F, 1F - m_fSideThickness, 1F - m_fSlopeMiddleMajorGap ) );
    	
    	// Top Wedge Base
    	
    	AddPrimitive( ( new FCUtilsPrimitiveQuad(
    		Vec3.createVectorHelper( m_fSideThickness, 1D, 1D ),
    		Vec3.createVectorHelper( m_fSideThickness, 0 + m_fBackSlopHeight, 1D ),
    		Vec3.createVectorHelper( 1D - m_fSideThickness, 0 + m_fBackSlopHeight, 1D ),
    		Vec3.createVectorHelper( 1D - m_fSideThickness, 1D, 1D )
    		) ).SetIconIndex( 2 ).SetUVFractions( m_fSideThickness, 0F, 1F - m_fSideThickness, 1F - m_fBackSlopHeight ) );
    	
    	m_rayTraceModel.AddBox( 0D, m_fBackSlopHeight, 1D - m_fSlopeCollisionHeight, 1D, 1D, 1D );
    	
    	// Front Middle slope
    	
    	AddPrimitive( ( new FCUtilsPrimitiveQuad(
    		Vec3.createVectorHelper( 1D - m_fSideThickness, m_fSlopeMiddleMajorGap, 1D - m_fSlopHeight ),
    		Vec3.createVectorHelper( 1D - m_fSideThickness, m_fSlopHeight, 1D - m_fSlopeMiddleMajorGap ),
    		Vec3.createVectorHelper( m_fSideThickness, m_fSlopHeight, 1D - m_fSlopeMiddleMajorGap ),
    		Vec3.createVectorHelper( m_fSideThickness, m_fSlopeMiddleMajorGap, 1D - m_fSlopHeight )
			) ).SetIconIndex( 3 ).SetUVFractions( m_fSideThickness, 0F, 1F - m_fSideThickness, ( 6F / 16F ) ) );
    	
    	// Back Middle Slope
    	
    	AddPrimitive( ( new FCUtilsPrimitiveQuad(
    		Vec3.createVectorHelper( m_fSideThickness, 0 + m_fBackSlopHeight, 1D ),
    		Vec3.createVectorHelper( m_fSideThickness, 0D, 1D - m_fBackSlopHeight ),
    		Vec3.createVectorHelper( 1D - m_fSideThickness, 0D, 1D - m_fBackSlopHeight ),
    		Vec3.createVectorHelper( 1D - m_fSideThickness, 0 + m_fBackSlopHeight, 1D )
			) ).SetIconIndex( 3 ).SetUVFractions( m_fSideThickness, 1F - ( 6F / 16F ), 1F - m_fSideThickness, 1F  ) );
    	
    	m_rayTraceModel.AddPrimitive( ( new FCUtilsPrimitiveQuad(
    		Vec3.createVectorHelper( m_fSideThickness, 0 + m_fBackSlopHeight, 1D ),
    		Vec3.createVectorHelper( m_fSideThickness, 0D, 1D - m_fBackSlopHeight ),
    		Vec3.createVectorHelper( 1D - m_fSideThickness, 0D, 1D - m_fBackSlopHeight ),
    		Vec3.createVectorHelper( 1D - m_fSideThickness, 0 + m_fBackSlopHeight, 1D )
			) ).SetIconIndex( 3 ).SetUVFractions( m_fSideThickness, 1F - ( 6F / 16F ), 1F - m_fSideThickness, 1F  ) );
    	
    	// stair steps for collision
    	
    	m_collisionModel.AddBox( 0D, 0D, 0D, 1D, 0.5D, 1D );
    	m_collisionModel.AddBox( 0D, 0.5D, 0.5D, 1D, 1D, 1D );
    }    
}
