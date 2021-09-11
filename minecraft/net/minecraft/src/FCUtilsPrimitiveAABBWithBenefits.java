// FCMOD

package net.minecraft.src;

public class FCUtilsPrimitiveAABBWithBenefits extends AxisAlignedBB
{
	private int m_iAssemblyID = -1; // used to identify objects within a parent block model
	private boolean m_bForceRenderWithColorMultiplier = false;
	
    protected FCUtilsPrimitiveAABBWithBenefits( double dXMin, double dYMin, double dZMin, 
    	double dXMax, double dYMax, double dZMax )
    {
    	super( dXMin, dYMin, dZMin, dXMax, dYMax, dZMax  );
    }
    
    protected FCUtilsPrimitiveAABBWithBenefits( double dXMin, double dYMin, double dZMin, 
    	double dXMax, double dYMax, double dZMax, int iAssemblyID )
    {
    	super( dXMin, dYMin, dZMin, dXMax, dYMax, dZMax );
    	
    	m_iAssemblyID = iAssemblyID;
    }
    
    @Override
    public FCUtilsPrimitiveAABBWithBenefits MakeTemporaryCopy()
    {
    	FCUtilsPrimitiveAABBWithBenefits tempCopy = 
    		new FCUtilsPrimitiveAABBWithBenefits( minX, minY, minZ, maxX, maxY, maxZ, m_iAssemblyID );

    	tempCopy.m_bForceRenderWithColorMultiplier = m_bForceRenderWithColorMultiplier;
    	
    	return tempCopy; 
    }
    
    @Override
    public int GetAssemblyID()
    {
    	return m_iAssemblyID;
    }
    
	//------------- Class Specific Methods ------------//

    public void SetForceRenderWithColorMultiplier( boolean bForce )
    {
    	m_bForceRenderWithColorMultiplier = bForce;
    }
	
	//----------- Client Side Functionality -----------//

    @Override
	public boolean RenderAsBlock( RenderBlocks renderBlocks, Block block, int i, int j, int k )
	{	
    	if ( !m_bForceRenderWithColorMultiplier )
    	{
    		return super.RenderAsBlock( renderBlocks, block, i, j, k );
    	}
    	else
    	{
    		return RenderAsBlockWithColorMultiplier( renderBlocks, block, i, j, k );
    	}
    }    
}
