package com.hbmspace.items.tool;

import com.hbm.packet.PacketDispatcher;
import com.hbm.packet.toclient.PlayerInformPacketLegacy;
import com.hbm.util.BobMathUtil;
import com.hbm.util.ChatBuilder;
import com.hbmspace.dim.trait.CBT_Atmosphere;
import com.hbmspace.handler.atmosphere.ChunkAtmosphereManager;
import com.hbmspace.items.ItemBakedSpace;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ItemAtmosphereScanner extends ItemBakedSpace {

    public ItemAtmosphereScanner(String s) {
        super(s);
    }

    @Override
    public void onUpdate(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {

        if(!(entityIn instanceof EntityPlayerMP player) || worldIn.getTotalWorldTime() % 5 != 0) return;

        CBT_Atmosphere atmosphere = ChunkAtmosphereManager.proxy.getAtmosphere(entityIn);

        boolean hasAtmosphere = false;
        if(atmosphere != null) {
            for(int i = 0; i < atmosphere.fluids.size(); i++) {
                CBT_Atmosphere.FluidEntry entry = atmosphere.fluids.get(i);
                if(entry.pressure > 0.001) {
                    double pressure = BobMathUtil.roundDecimal(entry.pressure, 3);
                    PacketDispatcher.wrapper.sendTo(new PlayerInformPacketLegacy(ChatBuilder.startTranslation(entry.fluid.getTranslationKey()).color(TextFormatting.AQUA).next(": ").next(pressure + "atm").color(TextFormatting.RESET).flush(), 969 + i, 4000), player);
                    hasAtmosphere = true;
                }
            }
        }

        if(!hasAtmosphere) {
            PacketDispatcher.wrapper.sendTo(new PlayerInformPacketLegacy(ChatBuilder.start("NEAR VACUUM").color(TextFormatting.YELLOW).flush(), 969, 4000), player);
        }
    }
}
