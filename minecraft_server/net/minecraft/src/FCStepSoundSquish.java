// FCMOD

package net.minecraft.src;

class FCStepSoundSquish extends StepSound
{
	FCStepSoundSquish( String s, float f, float f1 )
    {
        super( s, f, f1 );
    }
    
    @Override
    public String getStepSound()
    {
        return "mob.slime.attack";
    }    

    @Override
    public String getBreakSound()
    {
        return "mob.slime.small";
    }
}
