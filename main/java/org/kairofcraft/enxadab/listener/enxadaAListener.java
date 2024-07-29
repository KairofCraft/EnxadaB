package org.kairofcraft.enxadab.listener;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WindCharge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.kairofcraft.enxadab.utils.customExplosions;

import java.util.Random;

import static org.kairofcraft.enxadab.utils.customExplosions.randomParticleOffset;

public class enxadaAListener implements Listener {

    private final Random random = new Random();
    private final JavaPlugin plugin;
    public enxadaAListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    private customExplosions customExplosions;

    public enxadaAListener(JavaPlugin plugin, customExplosions customExplosions) {
        this.plugin = plugin;
        this.customExplosions = customExplosions;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK ||event.getAction() == Action.RIGHT_CLICK_BLOCK) {return;}
        if (event.getItem() != null && event.getItem().getType() == Material.WIND_CHARGE) {
            event.setCancelled(true);
            String string = ChatColor.DARK_PURPLE+"[EnxadaA] "+ChatColor.RED+"/enxada windCharge";
            boolean bloqueaWindCharge = plugin.getConfig().getBoolean("windChargeVanilla");
            ItemStack item = event.getItem();
            if (item.getItemMeta() != null){
                ItemMeta meta = item.getItemMeta();
                if (meta.hasCustomModelData()) {
                    int customModelData = meta.getCustomModelData();
                    if (customModelData != 761) {return;}
                    if(event.getAction() == Action.RIGHT_CLICK_AIR){
                        WindCharge windCharge = event.getPlayer().getWorld().spawn(event.getPlayer().getEyeLocation(), WindCharge.class);
                        windCharge.setMetadata("761", new FixedMetadataValue(plugin, "761"));
                        double velocidadeConfig = plugin.getConfig().getDouble("velocidadeDoWindCharge");
                        float velocidadeDoWindCharge = (float) velocidadeConfig;
                        windCharge.setVelocity(event.getPlayer().getEyeLocation().getDirection().multiply(velocidadeDoWindCharge));
                        return;}
                    return;
                }
                if (!bloqueaWindCharge) {
                    event.getPlayer().sendMessage(string);
                    event.setCancelled(true);
                    return;
                }
                WindCharge windCharge = event.getPlayer().getWorld().spawn(event.getPlayer().getEyeLocation(), WindCharge.class);
                windCharge.setVelocity(event.getPlayer().getEyeLocation().getDirection().multiply(1.5));
                return;
            }
        }
    }
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof WindCharge)) {return;}
        WindCharge windCharge = (WindCharge) event.getEntity();
        if (windCharge.getShooter() != null) {
            Entity shooter = (Entity) windCharge.getShooter();
            if (shooter instanceof Player) {
                Player player = (Player) shooter;
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                ItemMeta itemMeta = itemInHand.getItemMeta();
                if (itemMeta != null) {
                    if (itemMeta.hasCustomModelData()) {
                        Integer customModelData = itemMeta.getCustomModelData();
                        if (customModelData != 761) {
                            return;
                        }
                    } else {
                        boolean bloqueaWindCharge = plugin.getConfig().getBoolean("windChargeVanilla");
                        if (!bloqueaWindCharge) {
                            player.sendMessage(ChatColor.DARK_PURPLE + "[EnxadaA] " + ChatColor.RED + "/enxada windCharge");
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
        long quantidadeDeParticulaPorTick = plugin.getConfig().getLong("ParticulaPorTickVoado");
        int quantidadeDeParticulaPorAparicao = plugin.getConfig().getInt("ParticulaPorAparicaoVoado");
        new BukkitRunnable() {
            private int tickCount = 0;
            private final int maxTicks = 6000;
            @Override
            public void run() {
                String particulaStr = plugin.getConfig().getString("particulaVoado");
                Particle particula;
                try {
                    particula = Particle.valueOf(particulaStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Particula voado no config: " + particulaStr);
                    this.cancel();
                    return;
                }
                if (tickCount >= maxTicks || windCharge.isDead() || !windCharge.isValid()) {
                    this.cancel();
                    if (!windCharge.isValid()) {
                        windCharge.getWorld().spawnParticle(particula, windCharge.getLocation(), quantidadeDeParticulaPorAparicao, 0.2, 0.2, 0.2, 0.05);
                    }
                    return;
                }
                windCharge.getWorld().spawnParticle(particula, windCharge.getLocation(), quantidadeDeParticulaPorAparicao, 0.2, 0.2, 0.2, 0.05);
                tickCount += 6;
            }
        }.runTaskTimer(plugin, 0L, quantidadeDeParticulaPorTick);
    }
    @EventHandler
    public void onEntityExplodeEvent(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof WindCharge)) {return;}
        event.setCancelled(true);
        Location location = event.getLocation();
        World world = location.getWorld();
        if (world != null) {
            float forca = ((Double) plugin.getConfig().getDouble("forcaWindCharge")).floatValue();
            world.playSound(
                    location,
                    Sound.ENTITY_WIND_CHARGE_WIND_BURST,
                    SoundCategory.BLOCKS,
                    4.0F,
                    (1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F
            );
            String particulaStr = plugin.getConfig().getString("particulaExplocao");
            Particle  particle;
            try {
                particle = Particle.valueOf(particulaStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Particula explocao no config: " + particulaStr);
                particle = Particle.EXPLOSION;
            }
            int quantidadeDeParticula = plugin.getConfig().getInt("quantidadeDeParticulasAoExplodir");
            if (quantidadeDeParticula > 1) {
                for (int i = 0; i < quantidadeDeParticula; i++) {
                    world.spawnParticle(particle, location, 1, randomParticleOffset(forca), randomParticleOffset(forca), randomParticleOffset(forca));
                }
            } else {
                world.spawnParticle(particle, location,1 );
            }
            customExplosions.windExplode(location, forca);
        }

    }
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (!(player.hasMetadata("teleportLocation"))) {return;}
        Location destination = event.getTo();
        World world = destination.getWorld();
        String particulaStr = plugin.getConfig().getString("particulaDestinoDoTeleport");
        int quantidadeparticula = plugin.getConfig().getInt("quantidadeParticulaDestino");
        Particle particula;
        try {
            particula = Particle.valueOf(particulaStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Particula destino no config: " + particulaStr);
            particula = Particle.CLOUD;
            return;
        }
        world.spawnParticle(particula, destination, quantidadeparticula, randomParticleOffset(1.5F), randomParticleOffset(1.5F), randomParticleOffset(1.5F));
        player.removeMetadata("teleportLocation", plugin);
    }
}
