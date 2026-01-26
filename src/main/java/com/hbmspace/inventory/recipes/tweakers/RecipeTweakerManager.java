package com.hbmspace.inventory.recipes.tweakers;

public class RecipeTweakerManager {

    public static void initRecipeTweakers() {
        AnvilRecipeTweaker.init();
        ArcWelderRecipesTweaker.init();
        AssemblyRecipesTweaker.init();
        BlastFurnaceRecipesTweaker.init();
        CentrifugeRecipesTweaker.init();
        ChemicalPlantRecipesTweaker.init();
        CrucibleRecipesTweaker.init();
        CrystallizerRecipesTweaker.init();
        ElectrolyserFluidRecipesTweaker.init();
        FractionRecipesTweaker.init();
        MixerRecipesTweaker.init();
        ShredderRecipesTweaker.init();
        SolderingRecipesTweaker.init();
    }
}
