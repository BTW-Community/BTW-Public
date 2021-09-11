// FCMOD

package net.minecraft.src;

public class FCItemRottenFlesh extends FCItemFood
{
    public FCItemRottenFlesh( int iItemID )
    {
        super( iItemID, 3, 0F, true, "rottenFlesh" );        

        SetIncreasedFoodPoisoningEffect();
    }
    
    @Override
    public boolean IsPistonPackable( ItemStack stack )
    {
    	return true;
    }
    
    @Override
    public int GetRequiredItemCountToPistonPack( ItemStack stack )
    {
    	return 9;
    }
    
    @Override
    public int GetResultingBlockIDOnPistonPack( ItemStack stack )
    {
    	return FCBetterThanWolves.fcBlockRottenFlesh.blockID;
    }    
}
