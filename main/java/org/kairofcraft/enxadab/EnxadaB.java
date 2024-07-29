package org.kairofcraft.enxadab;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.kairofcraft.enxadab.command.HomeComand;
import org.kairofcraft.enxadab.command.deleteHomeComand;
import org.kairofcraft.enxadab.command.enxadaComand;
import org.kairofcraft.enxadab.command.setHomeComand;
import org.kairofcraft.enxadab.listener.enxadaAListener;
import org.kairofcraft.enxadab.utils.mesaMysql;

import java.sql.SQLException;

public final class EnxadaB extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE+"[EnxadaA]"+ChatColor.GREEN+"Iniciado");
        this.getCommand("enxada").setExecutor(new enxadaComand(this));
        this.getCommand("sethome").setExecutor(new setHomeComand(this));
        this.getCommand("home").setExecutor(new HomeComand(this));
        this.getCommand("deletehome").setExecutor(new deleteHomeComand());
        Bukkit.getPluginManager().registerEvents(new enxadaAListener(this),this);
        saveDefaultConfig();
        mesaMysql db = new mesaMysql(this);
        try {
            mesaMysql.connect();
            Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE+"[EnxadaA]"+"Banco de dados Conectado");
            mesaMysql.createHomesTable();
            Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE+"[EnxadaA]"+"Mesa OK");
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE+"[EnxadaA]"+"Houve um problema com o banco de dados: " + e.getMessage());
            this.getServer().shutdown();
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE+"[EnxadaA]"+ChatColor.RED+"Desligado");
        try {
            mesaMysql.close();
            Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE+"[EnxadaA]"+ChatColor.RED+"Banco de dados Desconectado");
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE+"[EnxadaA]"+ChatColor.RED+"Banco de dados Erro ao Desconectar");
            throw new RuntimeException(e);
        }
    }
}
