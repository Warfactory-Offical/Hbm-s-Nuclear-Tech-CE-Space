package com.hbmspace.mixin.mod.hbm;

import com.hbm.blocks.generic.BlockNTMOre;
import com.hbm.util.I18nUtil;
import com.hbmspace.blocks.generic.BlockOre;
import com.hbmspace.config.SpaceConfig;
import com.hbmspace.dim.SolarSystem;
import com.hbmspace.main.SpaceMain;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;

@Mixin(BlockNTMOre.class)
public class MixinBlockNTMOre {

    @Inject(method = "addInformation", at = @At("TAIL"), remap = false)
    private void showOreLocations(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced, CallbackInfo ci) {
        if (!SpaceConfig.showOreLocations) return;

        Set<SolarSystem.Body> bodies = BlockOre.spawnMap.get(this);
        if (bodies == null || bodies.isEmpty()) {
            return;
        }

        if (bodies.size() == SolarSystem.Body.values().length) {
            tooltip.add(TextFormatting.GOLD + "Can be found anywhere");
            return;
        } else if (bodies.size() == SolarSystem.Body.values().length - 1) {
            tooltip.add(TextFormatting.GOLD + "Can be found anywhere except:");
            for (SolarSystem.Body body : SolarSystem.Body.values()) {
                if (bodies.contains(body)) continue;
                tooltip.add(TextFormatting.RED + " - " + I18nUtil.resolveKey("body." + body.name));
            }
            return;
        }

        tooltip.add(TextFormatting.GOLD + "Can be found on:");
        for (SolarSystem.Body body : bodies) {
            tooltip.add(TextFormatting.AQUA + " - " + I18nUtil.resolveKey("body." + body.name));
        }
    }
}
