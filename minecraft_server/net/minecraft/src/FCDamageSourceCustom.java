// FCMOD

package net.minecraft.src;

public class FCDamageSourceCustom extends DamageSource
{
    public static DamageSource m_DamageSourceSaw = new FCDamageSourceCustom( "fcSaw", "rests in pieces" );	
    public static DamageSource m_DamageSourceChoppingBlock = new FCDamageSourceCustom( "fcChoppingBlock", "was put on the chopping block" );	
    public static DamageSource m_DamageSourceGroth = new FCDamageSourceCustom( "fcGroth", "became one with the Groth" ).setDamageBypassesArmor();	
    public static DamageSource m_DamageSourceGrothSpores = new FCDamageSourceCustom( "fcGrothSpores", "succumbed to the Groth menace" ).setDamageBypassesArmor();	
    public static DamageSource m_DamageSourceDeadWeight = new FCDamageSourceCustom( "fcDeadWeight", "had their hopes crushed by poor design" );	
    public static DamageSource m_DamageSourceMelon = new FCDamageSourceCustom( "fcMelon", "was smothered in melons" );	
    public static DamageSource m_DamageSourcePumpkin = new FCDamageSourceCustom( "fcPumpkin", "took wearing a pumpkin a bit too far" );	
    public static DamageSource m_DamageSourceGloom = new FCDamageSourceCustom( "fcGloom", "was consumed by the darkness" ).setDamageBypassesArmor();	
	
	String m_sDeathMessage;
	
    protected FCDamageSourceCustom( String sName, String sDeathMessage )
    {
    	super( sName );
    	
    	m_sDeathMessage = sDeathMessage;
    }
    
    @Override
    public String getDeathMessage( EntityLiving entity )
    {
        return (new StringBuilder( entity.getTranslatedEntityName() ) ).append( " " ).append( m_sDeathMessage ).toString();
    }
}
