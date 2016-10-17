package exnihiloadscensio.command;

import java.io.File;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.config.Config;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandReloadConfig extends CommandBase
{
    @Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }
    
    @Override
    public String getCommandName()
    {
        return "enreloadconfig";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "enreloadconfig";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        ExNihiloAdscensio.loadConfigs();
        Config.doNormalConfig(new File(ExNihiloAdscensio.configDirectory, "ExNihiloAdscensio.cfg"));
        sender.addChatMessage(new TextComponentTranslation("commands.enreloadconfig.confirm"));
    }
}
