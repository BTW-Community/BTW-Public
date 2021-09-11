// FCMOD

package net.minecraft.src;

public class FCUtilsBlockPos
{
	public int i, j, k;
	
    public FCUtilsBlockPos()
    {
    	i = j = k = 0;
    }
    
    public FCUtilsBlockPos( int i1, int j1, int k1 )
    {    	
    	i = i1;
    	j = j1;
    	k = k1;
    }
    
    public FCUtilsBlockPos( int iBaseI, int iBaseJ, int iBaseK, int iFacing )
    {
    	this( iBaseI, iBaseJ, iBaseK );
    	
    	AddFacingAsOffset( iFacing );
    }
    
    public void AddFacingAsOffset( int iFacing )
    {
    	i += Facing.offsetsXForSide[iFacing];
    	j += Facing.offsetsYForSide[iFacing];
    	k += Facing.offsetsZForSide[iFacing];
    }
    
    public void Invert()
    {
    	i = -i;
    	j = -j;
    	k = -k;
    }
    
    public void AddPos( FCUtilsBlockPos pos )
    {
    	i += pos.i;
    	j += pos.j;
    	k += pos.k;
    }
    
    public void Set( int i1, int j1, int k1 )
    {
    	i = i1;
    	j = j1;
    	k = k1;
    }
    
    public void Set( FCUtilsBlockPos pos )
    {
    	i = pos.i;
    	j = pos.j;
    	k = pos.k;
    }    
}
