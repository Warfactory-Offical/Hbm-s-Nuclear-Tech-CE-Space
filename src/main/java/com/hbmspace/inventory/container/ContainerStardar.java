package com.hbmspace.inventory.container;

import com.hbm.inventory.container.ContainerBase;
import com.hbmspace.tileentity.machine.TileEntityMachineStardar;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerStardar extends ContainerBase {

    public ContainerStardar(InventoryPlayer player, TileEntityMachineStardar stardar) {
        super(player, stardar.inventory);

        this.addSlotToContainer(new SlotItemHandler(stardar.inventory, 0, 150, 124));

        playerInv(player, 8, 174, 232);
    }

}
