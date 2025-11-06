package com.hbmspace.items.weapon;

import com.hbm.items.ModItems;
import com.hbm.items.weapon.ItemMissile;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

// Th3_Sl1ze: a space version, of course. We're changing some attributes slightly here
// I thought of mixin for injecting the required attributes onto existing parts, but then I thought... eh, that will look like a fucking spaghetti
public class ItemCustomMissilePart {

    /**
     * == Chips ==
     * [0]: inaccuracy
     *
     * == Warheads ==
     * [0]: type
     * [1]: strength/radius/cluster count
     * [2]: weight
     *
     * == Fuselages ==
     * [0]: type
     * [1]: tank size
     *
     * == Stability ==
     * [0]: inaccuracy mod
     *
     * == Thrusters ===
     * [0]: type
     * [1]: consumption
     * [2]: lift strength
     * ROCKET SPECIFIC
     * [3]: thrust (N)
     * [4]: ISP (s)
     */
    public Object[] attributes;

    public static final Map<Item, Object[]> THRUSTER_ATTRIBUTES = new HashMap<>();

    public static void initSpaceThrusters() {
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_10_kerosene, new Object[] { ItemMissile.FuelType.KEROSENE, 1F, 1_500, 16_000, 308 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_10_solid,    new Object[] { ItemMissile.FuelType.SOLID,    1F, 1_500, 60_000, 195 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_10_xenon,    new Object[] { ItemMissile.FuelType.XENON,    1F, 1_500, 2_000, 4200 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_kerosene,    new Object[] { ItemMissile.FuelType.KEROSENE,    1F, 7_500,   120_000, 308 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_kerosene_dual,    new Object[] { ItemMissile.FuelType.KEROSENE,    1F, 2_500,   200_000, 308 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_kerosene_triple,    new Object[] { ItemMissile.FuelType.KEROSENE,    1F, 5_000,   280_000, 308 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_solid,    new Object[] { ItemMissile.FuelType.SOLID,    1F, 5_000,   220_000, 195 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_solid_hexdecuple,    new Object[] { ItemMissile.FuelType.SOLID,    1F, 5_000,   260_000, 195 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_hydrogen,    new Object[] { ItemMissile.FuelType.HYDROGEN,    1F, 7_500,   100_000, 380 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_hydrogen_dual,    new Object[] { ItemMissile.FuelType.HYDROGEN,    1F, 2_500,   200_000, 380 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_balefire_short,    new Object[] { ItemMissile.FuelType.BALEFIRE,    1F, 5_000,   800_000, 666 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_balefire,    new Object[] { ItemMissile.FuelType.BALEFIRE,    1F, 5_000,   1_000_000, 666 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_balefire_large,    new Object[] { ItemMissile.FuelType.BALEFIRE,    1F, 7_500,   1_200_000, 666 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_15_balefire_large_rad,    new Object[] { ItemMissile.FuelType.BALEFIRE,    1F, 7_500,   1_200_000, 666 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_20_kerosene,    new Object[] { ItemMissile.FuelType.KEROSENE,    1F, 100_000,   1_536_000, 308 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_20_kerosene_dual,    new Object[] { ItemMissile.FuelType.KEROSENE,    1F, 100_000,   1_934_000, 308 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_20_kerosene_triple,    new Object[] { ItemMissile.FuelType.KEROSENE,    1F, 100_000,   2_542_000, 308 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_20_solid,    new Object[] { ItemMissile.FuelType.SOLID,    1F, 100_000,   1_400_000, 195 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_20_solid_multi,    new Object[] { ItemMissile.FuelType.SOLID,    1F, 100_000,   1_830_000, 195 });
        THRUSTER_ATTRIBUTES.put(ModItems.mp_thruster_20_solid_multier,    new Object[] { ItemMissile.FuelType.SOLID,    1F, 100_000,   2_320_000, 195 });
    }

    public static Object[] getAttributes(Item item) {
        return THRUSTER_ATTRIBUTES.get(item);
    }
}
