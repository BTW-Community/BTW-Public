// FCMOD

package net.minecraft.src;

public class FCCommandServerLoc extends CommandBase
{
    public FCCommandServerLoc()
    {
    }

    @Override
    public String getCommandName()
    {
        return "loc";
    }

    @Override
    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return "/loc";
    }

    @Override
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])    
    {   
    	if ( par1ICommandSender instanceof EntityPlayer )
    	{
    		EntityPlayer player = (EntityPlayer)par1ICommandSender;
    		
	    	par1ICommandSender.sendChatToPlayer( ( new StringBuilder()).
	    		append( "\247e"). // yellow text
	    		append( "Current Location: " ).
				append( MathHelper.floor_double( player.posX ) ).append( ", " ).
				append( MathHelper.floor_double( player.posY ) ).append( ", " ).
				append( MathHelper.floor_double( player.posZ ) ).
				toString() );
    	}
    }
}
