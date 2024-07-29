package org.kairofcraft.enxadab.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

import static org.kairofcraft.enxadab.utils.mesaMysql.deletarHomeEspecifica;

public class deleteHomeComand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender player, Command command, String s, String[] args) {
        if(command.getName().equalsIgnoreCase("deletehome")){
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
                player.sendMessage(ChatColor.RED + "Uso incorreto, Use: /deletehome <nome da home>");
                return false;
            }
            String argumento = args[0];
            try {
                boolean conseguil = deletarHomeEspecifica(player.getName(),argumento);
                if(conseguil){
                    player.sendMessage(ChatColor.GREEN+"Home deletada com sucesso : "+argumento);
                }else {
                    player.sendMessage(ChatColor.RED+"Você não tem esta home : "+argumento);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return false;
    }
}
