// FCMOD

package net.minecraft.src;

public class FCItemFood extends ItemFood
{
	static public final int m_iFoodPoisioningStandardDuration = 60; // in seconds
	static public final float m_fFoodPoisoningStandardChance = 0.3F;
	
	static public final int m_iFoodPoisioningIncreasedDuration = 60; // in seconds
	static public final float m_fFoodPoisoningIncreasedChance = 0.8F;
	
	static public final int m_iDonutHungerHealed = 1;
	static public final float m_fDonutSaturationModifier = 0.5F;
	static public final String m_sDonutItemName = "fcItemDonut";

	static public final int m_iDogFoodHungerHealed = 3;
	static public final float m_fDogFoodSaturationModifier = 0F;
	static public final String m_sDogFoodItemName = "fcItemKibble";
	
	static public final int m_iRawEggHungerHealed = 2;
	static public final float m_fRawEggSaturationModifier = 0.25F;
	static public final String m_sRawEggItemName = "fcItemEggRaw";
	
	static public final int m_iFriedEggHungerHealed = 3;
	static public final float m_fFriedEggSaturationModifier = 0.25F;
	static public final String m_sFriedEggItemName = "fcItemEggFried";
	
	static public final int m_iBoiledPotatoHungerHealed = 2;
	static public final float m_fBoiledPotatoSaturationModifier = 0F;
	static public final String m_sBoiledPotatoItemName = "fcItemPotatoBoiled";
	
	static public final int m_iCookedCarrotHungerHealed = 2;
	static public final float m_fCookedCarrotSaturationModifier = 0F;
	static public final String m_sCookedCarrotItemName = "fcItemCarrotCooked";
	
	static public final int m_iTastySandwichHungerHealed = 5;
	static public final float m_fTastySandwichSaturationModifier = 0.25F;
	static public final String m_sTastySandwichItemName = "fcItemSandwichTasty";
	
	static public final int m_iSteakAndPotatoesHungerHealed = 6;
	static public final float m_fSteakAndPotatoesSaturationModifier = 0.25F;
	static public final String m_sSteakAndPotatoesItemName = "fcItemSteakAndPotatoes";
	
	static public final int m_iHamAndEggsHungerHealed = 6;
	static public final float m_fHamAndEggsSaturationModifier = 0.25F;
	static public final String m_sHamAndEggsItemName = "fcItemHamAndEggs";
	
	static public final int m_iSteakDinnerHungerHealed = 8;
	static public final float m_fSteakDinnerSaturationModifier = 0.25F;
	static public final String m_sSteakDinnerItemName = "fcItemDinnerSteak";
	
	static public final int m_iPorkDinnerHungerHealed = 8;
	static public final float m_fPorkDinnerSaturationModifier = 0.25F;
	static public final String m_sPorkDinnerItemName = "fcItemDinnerPork";
	
	static public final int m_iWolfDinnerHungerHealed = 8;
	static public final float m_fWolfDinnerSaturationModifier = 0.25F;
	static public final String m_sWolfDinnerItemName = "fcItemDinnerWolf";
	
	static public final int m_iRawKebabHungerHealed = 6;
	static public final float m_fRawKebabSaturationModifier = 0.25F;
	static public final String m_sRawKebabItemName = "fcItemKebabRaw";
	
	static public final int m_iCookedKebabHungerHealed = 8;
	static public final float m_fCookedKebabSaturationModifier = 0.25F;
	static public final String m_sCookedKebabItemName = "fcItemKebabCooked";
	
	static public final int m_iChickenSoupHungerHealed = 8;
	static public final float m_fChickenSoupSaturationModifier = 0.25F;
	static public final String m_sChickenSoupItemName = "fcItemSoupChicken";
	
	static public final int m_iFishSoupHungerHealed = 5;
	static public final float m_fFishSoupSaturationModifier = 0.25F;
	static public final String m_sFishSoupItemName = "fcItemChowder";
	
	static public final int m_iHeartyStewHungerHealed = 10;
	static public final float m_fHeartyStewSaturationModifier = 0.25F;
	static public final String m_sHeartyStewItemName = "fcItemStewHearty";
	
	static public final int m_iRawMushroomOmeletHungerHealed = 3;
	static public final float m_fRawMushroomOmeletSaturationModifier = 0.25F;
	static public final String m_sRawMushroomOmeletItemName = "fcItemMushroomOmletRaw";
	
	static public final int m_iCookedMushroomOmeletHungerHealed = 4;
	static public final float m_fCookedMushroomOmeletSaturationModifier = 0.25F;
	static public final String m_sCookedMushroomOmeletItemName = "fcItemMushroomOmletCooked";
	
	static public final int m_iRawScrambledEggsHungerHealed = 3;
	static public final float m_fRawScrambledEggsSaturationModifier = 0.25F;
	static public final String m_sRawScrambledEggsItemName = "fcItemEggScrambledRaw";
	
	static public final int m_iCookedScrambledEggsHungerHealed = 4;
	static public final float m_fCookedScrambledEggsSaturationModifier = 0.25F;
	static public final String m_sCookedScrambledEggsItemName = "fcItemEggScrambledCooked";
	
	static public final int m_iCreeperOystersHungerHealed = 2;
	static public final float m_fCreeperOystersSaturationModifier = 0.8F;
	static public final String m_sCreeperOystersItemName = "fcItemCreeperOysters";
	
	static public final int m_iBatWingHungerHealed = 1;
	static public final float m_fBatWingSaturationModifier = 0.8F;
	static public final String m_sBatWingItemName = "fcItemBatWing";
	
	static public final int m_iChocolateHungerHealed = 2;
	static public final float m_fChocolateSaturationModifier = 0.5F;
	static public final String m_sChocolateItemName = "fcItemChocolate";
	
	static public final int m_iMuttonRawHungerHealed = 3;
	static public final int m_iMuttonCookedHungerHealed = 4;
	static public final float m_fMuttonSaturationModifier = 0.25F;	
	
	static public final int m_iBeastLiverRawHungerHealed = 5;
	static public final int m_iBeastLiverCookedHungerHealed = 6;
	static public final float m_fBeastLiverSaturationModifier = 0.5F;
	
	static public final int m_iChickenRawHungerHealed = 3;
	static public final int m_iChickenCookedHungerHealed = 4;	
	static public final float m_fChickenSaturationModifier = 0.25F;
	
	static public final int m_iBeefRawHungerHealed = 4;
	static public final int m_iBeefCookedHungerHealed = 5;
	static public final float m_fBeefSaturationModifier = 0.25F;
	
	static public final int m_iFishRawHungerHealed = 3;
	static public final int m_iFishCookedHungerHealed = 4;
	static public final float m_fFishSaturationModifier = 0.25F;
	
	static public final int m_iPorkChopRawHungerHealed = 4;
	static public final int m_iPorkChopCookedHungerHealed = 5;
	static public final float m_fPorkChopSaturationModifier = 0.25F;
	
	static public final int m_iMeatCuredHungerHealed = 2;
	static public final float m_fMeatCuredSaturationModifier = 0.25F;
	
	static public final int m_iMeatBurnedHungerHealed = 2;
	static public final float m_fMeatBurnedSaturationModifier = 0.25F;
	
	private String m_sIconNameOverride = null;
	
    public FCItemFood( int iItemID, int iHungerHealed, float fSaturationModifier, boolean bWolfMeat, String sItemName )
    {
        super( iItemID, iHungerHealed, fSaturationModifier, bWolfMeat );
        
        setUnlocalizedName( sItemName );    
    }
    
    public FCItemFood( int iItemID, int iHungerHealed, float fSaturationModifier, boolean bWolfMeat, String sItemName, boolean bZombiesConsume )
    {
        super( iItemID, iHungerHealed, fSaturationModifier, bWolfMeat, bZombiesConsume );
        
        setUnlocalizedName( sItemName );    
    }    
    
    //------------- Class Specific Methods ------------//
    
    public FCItemFood SetStandardFoodPoisoningEffect()
    {
    	setPotionEffect( Potion.hunger.id, m_iFoodPoisioningStandardDuration, 0, m_fFoodPoisoningStandardChance );
    	
    	return this;
    }
    
    public FCItemFood SetIncreasedFoodPoisoningEffect()
    {
    	setPotionEffect( Potion.hunger.id, m_iFoodPoisioningIncreasedDuration, 0, m_fFoodPoisoningIncreasedChance );
    	
    	return this;
    }

    public FCItemFood SetIconName( String sName )
    {
    	m_sIconNameOverride = sName;
    	
    	return this;
    }
    
	//----------- Client Side Functionality -----------//
}