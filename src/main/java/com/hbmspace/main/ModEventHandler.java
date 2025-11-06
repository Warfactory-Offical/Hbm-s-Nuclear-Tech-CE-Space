package com.hbmspace.main;

import com.hbm.render.amlfrom1710.Vec3;
import com.hbm.util.AstronomyUtil;
import com.hbm.util.ParticleUtil;
import com.hbmspace.capability.HbmLivingCapabilitySpace;
import com.hbmspace.dim.*;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

@Mod.EventBusSubscriber(modid = RefStrings.MODID)
public class ModEventHandler {

    public static final ResourceLocation ENT_HBM_PROP_ID = new ResourceLocation(RefStrings.MODID, "HBMLIVINGPROPS");

    public static Random rand = new Random();

    @SubscribeEvent
    public static void attachRadCap(AttachCapabilitiesEvent<Entity> e) {
        if (e.getObject() instanceof EntityLivingBase)
            e.addCapability(ENT_HBM_PROP_ID, new HbmLivingCapabilitySpace.EntityHbmPropsProvider());
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        boolean isFlying = event.getEntity() instanceof EntityPlayer && ((EntityPlayer) event.getEntity()).capabilities.isFlying;

        if(!isFlying) {
            float gravity = CelestialBody.getGravity(event.getEntityLiving());

            if(gravity == 0) {
                event.getEntityLiving().motionY /= 0.98F;
                event.getEntityLiving().motionY += (AstronomyUtil.STANDARD_GRAVITY / 20F);

                if(event.getEntityLiving() instanceof EntityPlayer player) {
                    if(player.isSneaking()) event.getEntityLiving().motionY -= 0.01F;
                    if(player.isJumping) event.getEntityLiving().motionY += 0.01F;
                } else if(event.getEntity() instanceof EntityChicken) {
                    event.getEntityLiving().motionY = 0;
                }

                event.getEntityLiving().motionY *= 0.91F;
            } else if(!event.getEntityLiving().isInWater() && event.getEntityLiving().ticksExisted > 20 && (gravity < 1.5F || gravity > 1.7F)) {
                // If gravity is basically the same as normal, do nothing
                // Also do nothing in water, or if we've been alive less than a second (so we don't glitch into the ground)

                // Minimum gravity to prevent floating bug
                if(gravity < 0.2F) gravity = 0.2F;

                // Undo falling, and add our intended falling speed
                // On high gravity planets, only apply falling speed when descending, so we can still jump up single blocks
                if((gravity < 1.5F || event.getEntityLiving().motionY < 0) && !(event.getEntity() instanceof EntityChicken)) {
                    event.getEntityLiving().motionY /= 0.98F;
                    event.getEntityLiving().motionY += (AstronomyUtil.STANDARD_GRAVITY / 20F);
                    event.getEntityLiving().motionY -= (gravity / 20F);
                    event.getEntityLiving().motionY *= 0.98F;
                }
            }
        }
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            CelestialTeleporter.runQueuedTeleport();
            if (event.world.getTotalWorldTime() % 20 == 0) {
                CelestialBody.updateChemistry(event.world);
            }
        }
        if (event.phase == TickEvent.Phase.START && event.world.provider instanceof WorldProviderCelestial && event.world.provider.getDimension() != 0) {
            if (event.world.getGameRules().getBoolean("doDaylightCycle")) {
                event.world.provider.setWorldTime(event.world.provider.getWorldTime() + 1L);
            }
        }
        if (event.phase == TickEvent.Phase.START) {
            updateWaterOpacity(event.world);
        }
    }

    private static void updateWaterOpacity(World world) {
        // Per world water opacity!
        int waterOpacity = 3;
        if (world.provider instanceof WorldProviderCelestial) {
            waterOpacity = ((WorldProviderCelestial) world.provider).getWaterOpacity();
        }

        Blocks.WATER.setLightOpacity(waterOpacity);
        Blocks.FLOWING_WATER.setLightOpacity(waterOpacity);
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (player.posY > 300 && player.posY < 1000) {
            Vec3 vec = Vec3.createVectorHelper(3 * rand.nextDouble(), 0, 0);
            CBT_Atmosphere thatmosphere = CelestialBody.getTrait(player.world, CBT_Atmosphere.class);

            if (thatmosphere != null && thatmosphere.getPressure() > 0.05 && !player.isRiding()) {
                if (Math.abs(player.motionX) > 1 || Math.abs(player.motionY) > 1 || Math.abs(player.motionZ) > 1) {
                    ParticleUtil.spawnGasFlame(player.world, player.posX - 1 + vec.xCoord, player.posY + vec.yCoord, player.posZ + vec.zCoord, 0, 0, 0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onGenerateOre(OreGenEvent.GenerateMinable event) {
        if (event.getWorld().provider instanceof WorldProviderCelestial && event.getWorld().provider.getDimension() != 0) {
            WorldGeneratorCelestial.onGenerateOre(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLoad(WorldEvent.Load event) {
        if (event.getWorld().provider.getDimension() == 0) {
            WorldProviderEarth customProvider = new WorldProviderEarth();
            customProvider.setWorld(event.getWorld());
            customProvider.setDimension(0);
            event.getWorld().provider = customProvider;
        }
    }
}
