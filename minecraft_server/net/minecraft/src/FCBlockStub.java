// FCMOD

package net.minecraft.src;

public class FCBlockStub extends Block
{
    private final IBehaviorDispenseItem dropperDefaultBehaviour = new BehaviorDefaultDispenseItem();

    protected FCBlockStub(int par1)
    {
        super( par1, Material.rock );
    }
    
}
