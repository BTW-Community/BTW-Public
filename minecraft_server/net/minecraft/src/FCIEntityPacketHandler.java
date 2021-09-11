// FCMOD

package net.minecraft.src;

public interface FCIEntityPacketHandler
{
	/*
	 *  NOTE: Don't forget to add the entity into NetClientHandler!!! 
	 */
	
	/* 
	 * hook into EntityTrackerEntry getPacketForThisEntity() to get the packet sent from the server when this entity spawns
	 */ 
    public Packet GetSpawnPacketForThisEntity();
    
    public int GetTrackerViewDistance();
    
    public int GetTrackerUpdateFrequency();

    public boolean GetTrackMotion();

    /*
     * Partially disables server-side visibility tests for interacting with an entity
     */
    public boolean ShouldServerTreatAsOversized();
}