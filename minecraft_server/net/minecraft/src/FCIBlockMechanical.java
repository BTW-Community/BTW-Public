// FCMOD

package net.minecraft.src;

public interface FCIBlockMechanical
{
	public boolean CanOutputMechanicalPower();
	
	public boolean CanInputMechanicalPower();
	
	public boolean IsInputtingMechanicalPower( World world, int i, int j, int k );
	
	public boolean IsOutputtingMechanicalPower( World world, int i, int j, int k );
	
	public boolean CanInputAxlePowerToFacing( World world, int i, int j, int k, int iFacing );
	
	public void Overpower( World world, int i, int j, int k );
}