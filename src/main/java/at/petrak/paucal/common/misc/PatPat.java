package at.petrak.paucal.common.misc;

import at.petrak.paucal.PaucalMod;
import at.petrak.paucal.api.contrib.Contributors;
import at.petrak.paucal.common.PaucalConfig;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = PaucalMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PatPat {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPat(PlayerInteractEvent.EntityInteract evt) {
        if (!PaucalConfig.allowPats.get()) {
            // you philistine
            return;
        }

        var player = evt.getPlayer();
        if (player.getItemInHand(evt.getHand()).isEmpty()
            && player.isDiscrete() && evt.getTarget() instanceof Player target) {
            if (player.getLevel() instanceof ServerLevel world) {
                var pos = target.getEyePosition();
                world.sendParticles(ParticleTypes.HEART, pos.x, pos.y + 0.5, pos.z, 1, 0, 0, 0, 0.1);
            } else {
                player.swing(evt.getHand());
            }

            var contributor = Contributors.getContributor(target.getUUID());
            if (contributor != null) {
                var soundKeyStr = contributor.getString("paucal:patSound");
                if (soundKeyStr != null) {
                    var soundKey = new ResourceLocation(soundKeyStr);
                    var sound = ForgeRegistries.SOUND_EVENTS.getValue(soundKey);
                    if (sound != null) {
                        var pitchCenter = Objects.requireNonNullElse(contributor.getFloat("paucal:patPitchCenter"), 1f);
                        target.getLevel()
                            .playSound(target, target, sound, SoundSource.PLAYERS, 1f,
                                pitchCenter + (float) (Math.random() - 0.5) * 0.5f);
                    }
                }
            }
            evt.setCanceled(true);
        }
    }
}
