package org.kairofcraft.enxadab.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

import static org.kairofcraft.enxadab.utils.mesaMysql.contarHomesPorPlayer;
import static org.kairofcraft.enxadab.utils.mesaMysql.insertHome;
public class setHomeComand implements CommandExecutor {
    private final JavaPlugin plugin;
    public setHomeComand(JavaPlugin plugin) {this.plugin = plugin;}
    @Override
    public boolean onCommand(CommandSender player, Command command, String s, String[] args) {
        if(command.getName().equalsIgnoreCase("sethome")){
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
                player.sendMessage(ChatColor.GOLD+"(/sethome <nome da home>) "+ChatColor.GRAY+"criar home");
                player.sendMessage(ChatColor.GOLD+"(/home) "+ChatColor.GRAY+"Mostra seus homes");
                player.sendMessage(ChatColor.GOLD+"(/home <nome da home>) "+ChatColor.GRAY+"Teleporta para home");
                player.sendMessage(ChatColor.GOLD+"(/deletehome <nome da home>) "+ChatColor.RED+"Deleta a home");
                return false;
            }
            if(args.length != 1){
                player.sendMessage(ChatColor.RED + "Uso incorreto, Use: /sethome <nome da home>");
                return false;
            }
            String argumento = args[0];
            try {
                int quantidadeDeHome = contarHomesPorPlayer(player.getName());
                int quantidaDePossivelDeHome = plugin.getConfig().getInt("quantidadeDeHome");
                if(quantidadeDeHome >= quantidaDePossivelDeHome){
                    player.sendMessage(ChatColor.RED+ "você ja possui "+quantidaDePossivelDeHome+" homes e o maximo");
                    return false;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                boolean jaTemComNome = insertHome((Player) player,argumento);
                if(jaTemComNome){
                    player.sendMessage(ChatColor.GREEN + "home setada com sucesso "+argumento);
                    return false;
                }else {
                    player.sendMessage(ChatColor.RED + "Voce já possui esta home : "+argumento);
                }
            } catch (SQLException e) {
                player.sendMessage(ChatColor.RED+ "Casa setada com Erro "+argumento);
                throw new RuntimeException(e);

            }
        }
        return false;
    }
}
