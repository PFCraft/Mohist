package com.mohistmc.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TileEntityCommand extends Command {

    private List<String> params = Arrays.asList("reload", "dump-all", "dump-existing");

    public TileEntityCommand(String name) {
        super(name);
        this.description = "TileEntity tick limiting commands";
        this.usageMessage = "/tileentity [reload|dump-all|dump-existing]";
        this.setPermission("mohist.command.tileentity");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> list = new ArrayList<>();
        if (args.length == 1 && (sender.isOp() || testPermission(sender))) {
            for (String param : params) {
                if (param.toLowerCase().startsWith(args[0].toLowerCase())) {
                    list.add(param);
                }
            }
        }

        return list;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "reload":
                MinecraftServer.tileEntityConfig.load();
                sender.sendMessage(ChatColor.GREEN + "tileentity.yml reload complete.");
                break;

            case "dump-existing":
                MinecraftServer.tileEntityConfig.save();
                sender.sendMessage(ChatColor.GREEN + "tileentity.yml updated with found TileEntities");
                break;

            case "dump-all":
                Iterator i = TileEntity.getRegisteredTileEntities().iterator();

                for (Object rl : TileEntity.getRegisteredTileEntities().getKeys()) {
                    ResourceLocation a = (ResourceLocation) rl;

                    MinecraftServer.LOGGER.info(
                            "Found TileEntity with name: " + com.mohistmc.util.TileEntity.sanitizeClassName(
                                    ((Class) TileEntity.getRegisteredTileEntities().getObject(rl))
                            )
                    );
                }

                sender.sendMessage(ChatColor.GREEN + "Found TileEntity names dumped to console.");
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
                return false;
        }

        return true;
    }
}
