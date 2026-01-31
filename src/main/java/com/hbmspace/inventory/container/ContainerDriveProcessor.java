package com.hbmspace.inventory.container;

import com.hbm.inventory.slot.SlotBattery;
import com.hbm.inventory.container.ContainerBase;
import com.hbmspace.tileentity.machine.TileEntityMachineDriveProcessor;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerDriveProcessor extends ContainerBase {

    public ContainerDriveProcessor(InventoryPlayer invPlayer, TileEntityMachineDriveProcessor machine) {
        super(invPlayer, machine.inventory);

        // 0 - active drive slot
        // 1 - cloning drive slot

        // 2 - processor circuit slot (GO FUCK YOURSELF IT'S NOT AN UPGRADE SLOT)

        // 3 - battery slot

        addSlotToContainer(new SlotItemHandler(machine.inventory, 0, 30, 18));
        addSlotToContainer(new SlotItemHandler(machine.inventory, 1, 50, 38));

        addSlotToContainer(new SlotItemHandler(machine.inventory, 2, 81, 24));

        addSlotToContainer(new SlotBattery(machine.inventory, 3, 134, 72));

        playerInv(invPlayer, 8, 125, 183);
    }

}
