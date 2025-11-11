package com.hbmspace.mixin.mod.hbm;

import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.AssemblyMachineRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.inventory.recipes.loader.GenericRecipes;
import com.hbm.items.ItemEnums;
import com.hbm.items.ModItems;
import com.hbmspace.blocks.ModBlocksSpace;
import com.hbmspace.items.ModItemsSpace;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hbm.inventory.OreDictManager.*;
import static com.hbmspace.inventory.OreDictManagerSpace.*;

@Mixin(AssemblyMachineRecipes.class)
public class MixinAssemblyRecipes {

    @Inject(method = "registerDefaults", at = @At("TAIL"), remap = false)
    private void registerSpace(CallbackInfo ci) {
        AssemblyMachineRecipes instance = (AssemblyMachineRecipes) (Object) this;
        String autoPlate = "autoswitch.plates";
        instance.register(new GenericRecipe("ass.platenickel").setup(60, 100).outputItems(new ItemStack(ModItemsSpace.plate_nickel, 1)).inputItems(new RecipesCommon.OreDictStack(NI.ingot())).setPools(GenericRecipes.POOL_PREFIX_ALT + "plates").setGroup(autoPlate, instance));

        instance.register(new GenericRecipe("ass.launchpadrocket").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.launch_pad_rocket, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateWelded(), 12),
                        new RecipesCommon.OreDictStack(AL.pipe(), 24),
                        new RecipesCommon.OreDictStack(ANY_CONCRETE.any(), 64),
                        new RecipesCommon.OreDictStack(ANY_CONCRETE.any(), 64),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 16),
                        new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 64),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.ADVANCED)));

        instance.register(new GenericRecipe("ass.rocketassembly").setup(400, 100).outputItems(new ItemStack(ModBlocksSpace.machine_rocket_assembly, 1))
                .inputItems(
                        new RecipesCommon.OreDictStack(STEEL.plateCast(), 8),
                        new RecipesCommon.OreDictStack(STEEL.pipe(), 12),
                        new RecipesCommon.OreDictStack(ANY_CONCRETE.any(), 16),
                        new RecipesCommon.OreDictStack(ANY_PLASTIC.ingot(), 8),
                        new RecipesCommon.ComparableStack(ModBlocks.steel_scaffold, 64),
                        new RecipesCommon.ComparableStack(ModItems.circuit, 4, ItemEnums.EnumCircuitType.BASIC)));
    }
}


