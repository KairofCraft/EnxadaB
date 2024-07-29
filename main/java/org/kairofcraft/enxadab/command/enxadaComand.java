package org.kairofcraft.enxadab.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
public class enxadaComand implements CommandExecutor {
    private final JavaPlugin plugin;
    public enxadaComand(JavaPlugin plugin) {this.plugin = plugin;}
    @Override
    public boolean onCommand(CommandSender player, Command command, String s, String[] args) {
        if(command.getName().equalsIgnoreCase("enxada")){
            if(!player.hasPermission("enxada.*")){
                player.sendMessage(ChatColor.RED+"Você não Tem permisão");
                return false;
            }
            if(args.length != 1){
                player.sendMessage(" ");
                player.sendMessage(ChatColor.DARK_PURPLE+"[EnxadaA]");
                player.sendMessage(ChatColor.GRAY+"===========/enxada===========");
                player.sendMessage(ChatColor.GOLD+"(/enxada reload) "+ChatColor.GRAY+"recarrega o config.yml");
                player.sendMessage(ChatColor.GOLD+"(/enxada WindCharge) "+ChatColor.GRAY+"recarrega o config.yml");
                return false;
            }
            if (args[0].equalsIgnoreCase("reload")){
                plugin.reloadConfig();
                player.sendMessage(" ");
                player.sendMessage(ChatColor.DARK_PURPLE+"[EnxadaA] "+ChatColor.GREEN+"Recarregado");
                return false;
            }
            if(args[0].equalsIgnoreCase("windCharge")) {
                if (!(player instanceof Player)) {
                    player.sendMessage(ChatColor.DARK_PURPLE + "[EnxadaA] " + ChatColor.RED + "apenas jogadores");
                    return false;
                }
                ItemStack item = new ItemStack(Material.WIND_CHARGE);
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatColor.DARK_PURPLE + "[EnxadaA]");
                    meta.setCustomModelData(761); // Adicionar dados personalizados
                    meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "cooldown"), PersistentDataType.INTEGER,10);
                    item.setItemMeta(meta);
                }
                Player jogador = (Player) player;
                jogador.getInventory().addItem(item);
                return false;
            }
        }
        return false;
    }
}
