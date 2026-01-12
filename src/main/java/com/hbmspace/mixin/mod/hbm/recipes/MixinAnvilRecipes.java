package com.hbmspace.mixin.mod.hbm.recipes;

import com.hbm.inventory.OreDictManager;
import com.hbm.inventory.RecipesCommon;
import com.hbm.inventory.recipes.anvil.AnvilRecipes;
import com.hbm.inventory.recipes.anvil.AnvilSmithingRecipe;
import com.hbm.items.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.ListIterator;

import static com.hbm.inventory.OreDictManager.CU;
import static com.hbmspace.inventory.OreDictManagerSpace.ZI;

@Mixin(value = AnvilRecipes.class, remap = false)
public abstract class MixinAnvilRecipes {

    @Inject(method = "registerSmithing", at = @At("TAIL"))
    private static void hbm$replaceGunmetalRecipe(CallbackInfo ci) {
        final String cu = OreDictManager.CU.ingot();
        final String al = OreDictManager.AL.ingot();

        for (ListIterator<AnvilSmithingRecipe> it = AnvilRecipes.smithingRecipes.listIterator(); it.hasNext(); ) {
            AnvilSmithingRecipe r = it.next();
            if (r == null || r.tier != 1) continue;

            if (!space$stacksEqual(r.getSimpleOutput(), new ItemStack(ModItems.ingot_gunmetal, 1))) continue;

            List<ItemStack> left = r.getLeft();
            List<ItemStack> right = r.getRight();
            if (left == null || right == null || left.isEmpty() || right.isEmpty()) continue;

            boolean direct = space$anyOreMatch(left, cu) && space$anyOreMatch(right, al);
            boolean mirrored = space$anyOreMatch(left, al) && space$anyOreMatch(right, cu);

            if (direct || mirrored) {
                it.set(new AnvilSmithingRecipe(
                        1,
                        new ItemStack(ModItems.ingot_gunmetal, 1),
                        new RecipesCommon.OreDictStack(CU.ingot()),
                        new RecipesCommon.OreDictStack(ZI.ingot())
                ));
                break;
            }
        }
    }

    @Unique
    private static boolean space$anyOreMatch(List<ItemStack> stacks, String oreName) {
        for (ItemStack s : stacks) {
            if (s == null || s.isEmpty()) continue;
            for (int id : OreDictionary.getOreIDs(s)) {
                if (OreDictionary.getOreName(id).equals(oreName)) return true;
            }
        }
        return false;
    }

    @Unique
    private static boolean space$stacksEqual(ItemStack a, ItemStack b) {
        return a != null && b != null && !a.isEmpty() && !b.isEmpty()
                && a.getItem() == b.getItem()
                && a.getMetadata() == b.getMetadata()
                && ItemStack.areItemStackTagsEqual(a, b);
    }
}
