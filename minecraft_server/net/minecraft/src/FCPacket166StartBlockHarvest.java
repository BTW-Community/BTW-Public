// FCMOD

package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FCPacket166StartBlockHarvest extends Packet
{
    public int iIPos;
    public int iJPos;
    public int iKPos;
    
    public int iFace;
    
    private int iMiningSpeedModifier;

    public FCPacket166StartBlockHarvest() {}

    public FCPacket166StartBlockHarvest( int i, int j, int k, int iHitFace, float fMiningSpeedModifier )
    {
        iIPos = i;
        iJPos = j;
        iKPos = k;
        
        iFace = iHitFace;
        
        iMiningSpeedModifier = (int)( fMiningSpeedModifier * 8000F );
    }

    public void readPacketData( DataInputStream stream ) throws IOException
    {
        iIPos = stream.readInt();
        iJPos = stream.read();
        iKPos = stream.readInt();
        
        iFace = stream.read();
        
        iMiningSpeedModifier = stream.readShort();
    }

    public void writePacketData( DataOutputStream stream ) throws IOException
    {
        stream.writeInt( iIPos );
        stream.write( iJPos );
        stream.writeInt( iKPos );
        
        stream.write( iFace );
        
        stream.writeShort( iMiningSpeedModifier );
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.HandleStartBlockHarvest( this );
    }

    public int getPacketSize()
    {
        return 12;
    }
    
    public float GetMiningSpeedModifier()
    {
    	return (float)iMiningSpeedModifier / 8000F; 
    }
}
