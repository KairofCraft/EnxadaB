package org.kairofcraft.enxadab.utils;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class customExplosions {
    private static JavaPlugin plugin;

    public customExplosions(JavaPlugin plugin) {
        customExplosions.plugin = plugin;
    }

    public static final Random RANDOM = new Random();
    public static void windExplode(Location location, float power) {
        float forca = power * 2.0F;

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        List<Entity> entityList = (List<Entity>) location.getWorld().getNearbyEntities(new BoundingBox(
                Math.floor(x - (double) forca - 1.0),
                Math.floor(y - (double) forca - 1.0),
                Math.floor(z - (double) forca - 1.0),
                Math.floor(x + (double) forca + 1.0),
                Math.floor(y + (double) forca + 1.0),
                Math.floor(z + (double) forca + 1.0)
        ));
        Iterator<Entity> iterator = entityList.iterator();

        while (true) {
            Entity entity;

            double distanciax;
            double distanciay;
            double distanciaz;
            double distanciaRaio;
            double distanciaCauculada;

            do {
                do {
                    do {
                        if (!iterator.hasNext()) {return;}//verifica se a mais uma criatura antes de manter o while

                        entity = iterator.next();// aki pegamos a proxima entidade
                    }

                    while (entity instanceof ArmorStand || entity instanceof Warden); // As únicas entidades que são imunes a explosões
                    distanciaRaio = Math.sqrt(entity.getLocation().distanceSquared(location)) / (double) forca;// distanceSquared; Calcula a distância ao quadrado entre a localização da entidade.

                } while (!(distanciaRaio <= 1.0));

                distanciax = entity.getLocation().getX() - x;
                distanciay = (entity instanceof TNTPrimed ? entity.getLocation().getY() : (entity instanceof LivingEntity ? ((LivingEntity) entity).getEyeLocation().getY() : entity.getLocation().getY())) - y; //utilozo da visão do mob ou jogador para cordernar
                distanciaz = entity.getLocation().getZ() - z;
                distanciaCauculada = Math.sqrt(distanciax * distanciax + distanciay * distanciay + distanciaz * distanciaz);//cauculo de ango de empurrão,pita
            } while (distanciaCauculada == 0.0);

            distanciax /= distanciaCauculada;
            distanciay /= distanciaCauculada;
            distanciaz /= distanciaCauculada;

            double aa = (1.0 - distanciaRaio) * (double) getExposure(location, entity) * (double) getKnockbackModifier(entity);
            double ab;
            if (entity instanceof LivingEntity livingEntity) {
                ab = aa * (1.0 - livingEntity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getValue());
            } else {
                ab = aa;
            }

            distanciax *= ab;
            distanciay *= ab;
            distanciaz *= ab;
            // Magic value needed to mimic minecraft's exact behavior
            // Note: I tried to find the reason behind this, but couldn't. At least it works, so we should not question
            // it too much :D
            distanciay /= 10.0;
            Vector explosionVector = new Vector(distanciax, distanciay, distanciaz).multiply(power);
            entity.setVelocity(entity.getVelocity().add(explosionVector));
        }
    }
    private static float getExposure(Location source, Entity entity) {
        BoundingBox hitbox = entity.getBoundingBox();
        double escalax = 1.0 / ((hitbox.getMaxX() - hitbox.getMinX()) * 2.0 + 1.0);
        double escalay = 1.0 / ((hitbox.getMaxY() - hitbox.getMinY()) * 2.0 + 1.0);
        double escalaz = 1.0 / ((hitbox.getMaxZ() - hitbox.getMinZ()) * 2.0 + 1.0);
        double centralizacaoEscalaX = (1.0 - Math.floor(1.0 / escalax) * escalax) / 2.0;
        double centralizacaoEscalaz = (1.0 - Math.floor(1.0 / escalaz) * escalaz) / 2.0;

        if (escalax < 0.0 || escalay < 0.0 || escalaz < 0.0) {
            return 0.0F;
        }

        int contador1 = 0;
        int contador2 = 0;
        World world = entity.getWorld();
        Vector hitbox3d = source.toVector();

        for (double contadorBloco = 0.0; contadorBloco <= 1.0; contadorBloco += escalax) {
            for (double for2contador = 0.0; for2contador <= 1.0; for2contador += escalay) {
                for (double for3contador = 0.0; for3contador <= 1.0; for3contador += escalaz) {
                    double alinhamentoX = lerp(contadorBloco, hitbox.getMinX(), hitbox.getMaxX());
                    double alinhamentoY = lerp(for2contador, hitbox.getMinY(), hitbox.getMaxY());
                    double alinhamentoZ = lerp(for3contador, hitbox.getMinZ(), hitbox.getMaxZ());
                    Vector pontoNoEspaco = new Vector(alinhamentoX + centralizacaoEscalaX, alinhamentoY,alinhamentoZ + centralizacaoEscalaz);

                    RayTraceResult pontoDeBaixoEntydade = world.rayTrace(pontoNoEspaco.toLocation(world), hitbox3d.subtract(pontoNoEspaco), hitbox3d.distance(pontoNoEspaco), FluidCollisionMode.NEVER, true, 0.0, entt -> entt == entity);

                    if (pontoDeBaixoEntydade == null || pontoDeBaixoEntydade.getHitBlock() == null) {
                        ++contador1;
                    }
                    ++contador2;
                }
            }
        }
        return (float) contador1 / (float) contador2;
    }
    private static float getKnockbackModifier(Entity entity) {
        boolean isFlying = false;
        if (entity instanceof Player player) {
            isFlying = player.isFlying();
        }
        return isFlying ? 0.0F : 1.2F;
    }
    public static float randomParticleOffset(float multiplier) {
        return (-1.0F + RANDOM.nextFloat()) * Math.max(multiplier / 2.0F, 1.0F); // usado para randomizar a particulas, no ambiente
    }

    public static double lerp(double delta, double start, double end) {
        return start + delta * (end - start);
    }
}