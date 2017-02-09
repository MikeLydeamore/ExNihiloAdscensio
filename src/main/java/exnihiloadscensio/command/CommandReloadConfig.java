package exnihiloadscensio.command;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.config.Config;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import java.io.File;

public class CommandReloadConfig extends CommandBase
{
    @Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }
    
    @Override @Nonnull
    public String getName()
    {
        return "enreloadconfig";
    }

    @Override @Nonnull
    public String getUsage(@Nonnull ICommandSender sender)
    {
        return "enreloadconfig";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
    {
        ExNihiloAdscensio.loadConfigs();
        Config.doNormalConfig(new File(ExNihiloAdscensio.configDirectory, "ExNihiloAdscensio.cfg"));
        sender.sendMessage(new TextComponentTranslation("commands.enreloadconfig.confirm"));
    }
}
