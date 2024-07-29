package org.kairofcraft.enxadab.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.kairofcraft.enxadab.utils.mesaMysql.obterNomesDasHomes;
import static org.kairofcraft.enxadab.utils.mesaMysql.pesquisHomeEspecifica;

public class HomeComand implements CommandExecutor {
    private final JavaPlugin plugin;
    public HomeComand(JavaPlugin plugin) {this.plugin = plugin;}
    private Map<Player, Long> cooldowns = new HashMap<>();
    private Map<String, Object> homeData = null;
    @Override
    public boolean onCommand(CommandSender player, Command command, String s, String[] args) {
        if(command.getName().equalsIgnoreCase("home")){
            if(!(player instanceof Player)){
                player.sendMessage(ChatColor.RED+"apenas jogador");
                return false;
            }
            if(!player.hasPermission("enxada.*")){
                player.sendMessage(ChatColor.RED+"Você não Tem permisão");
                return false;
            }
            if(args.length == 0){
                player.sendMessage(" ");
                player.sendMessage(ChatColor.DARK_PURPLE+"[EnxadaA]");
                player.sendMessage(ChatColor.GRAY+"===========/home===========");
                try {
                    List<String> nomes = obterNomesDasHomes(player.getName());
                    if(nomes.isEmpty()){
                        player.sendMessage(ChatColor.RED+"Não tem home ainda");
                        player.sendMessage(ChatColor.GOLD+"(/sethome <nome da home>) "+ChatColor.GRAY+"criar home");
                        player.sendMessage(ChatColor.GOLD+"(/home) "+ChatColor.GRAY+"Mostra seus homes");
                        player.sendMessage(ChatColor.GOLD+"(/home <nome da home>) "+ChatColor.GRAY+"Teleporta para home");
                        player.sendMessage(ChatColor.GOLD+"(/deletehome <nome da home>) "+ChatColor.RED+"Deleta a home");
                    return false;
                    }
                    for (String nome : nomes) {
                        player.sendMessage(ChatColor.GOLD+"Home: "+ChatColor.GRAY+nome);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return false;
            }
            if(args.length != 1){
                player.sendMessage(ChatColor.RED + "Uso incorreto, Use: /home <nome da home>");
                return false;
            }
            String argumento = args[0];
            try {
                homeData = pesquisHomeEspecifica(player.getName(),argumento);
                if(homeData.get("home_name") == null && homeData.get("id") == null){
                    player.sendMessage(ChatColor.RED + "Você não tem esta casa");
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            long tempoAgora = System.currentTimeMillis();
            Long tempoUsado = cooldowns.get(player);
            long COOLDOWN_TIME = plugin.getConfig().getInt("tempoDeRecargaHome")* 1000L;
            if (tempoUsado != null && tempoAgora - tempoUsado < COOLDOWN_TIME) {
                long timeLeft = (COOLDOWN_TIME - (tempoAgora - tempoUsado)) / 1000;
                player.sendMessage(ChatColor.RED+"Tempo "+timeLeft+" usar este comando novamente.");
                return false;
            }
            cooldowns.put((Player) player, tempoAgora);
            String worldName = (String) homeData.get("world");
            World world = Bukkit.getWorld(worldName);
            Location location = new Location(
                    world,
                    (Double) homeData.get("x"),
                    (Double) homeData.get("y"),
                    (Double) homeData.get("z"),
                    (Float) homeData.get("yaw"),
                    (Float) homeData.get("pitch")
            );
            ((Player) player).setMetadata("teleportLocation", new FixedMetadataValue(plugin, location));
            ((Player) player).teleport(location);
            player.sendMessage(ChatColor.GREEN+"Teleportado com sucesso");
        }
        return false;
    }
}
