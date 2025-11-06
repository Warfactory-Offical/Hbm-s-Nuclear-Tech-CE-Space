package com.hbmspace.inventory.container;

import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.container.ContainerBase;
import com.hbmspace.tileentity.bomb.TileEntityLaunchPadRocket;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerLaunchPadRocket extends ContainerBase {

    public ContainerLaunchPadRocket(InventoryPlayer invPlayer, TileEntityLaunchPadRocket machine) {
        super(invPlayer, machine.inventory);

        addSlotToContainer(new SlotNonRetarded(machine.inventory, 0, 37, 21)); // Rocket slot
        addSlotToContainer(new SlotNonRetarded(machine.inventory, 1, 37, 39)); // Drive slot

        addSlotToContainer(new SlotNonRetarded(machine.inventory, 2, 167, 90)); // Battery slot

        addSlotToContainer(new SlotItemHandler(machine.inventory, 3, 77, 21)); // Input
        addSlotToContainer(new SlotItemHandler(machine.inventory, 4, 77, 39)); // Output

        addSlots(invPlayer, 9, 14, 154, 3, 9); // Player inventory
        addSlots(invPlayer, 0, 14, 212, 1, 9); // Player hotbar
    }

    public void addSlots(InventoryPlayer inv, int from, int x, int y, int rows, int cols) {
        this.addSlots(inv, from, x, y, rows, cols, 18);
    }

    public void addSlots(InventoryPlayer inv, int from, int x, int y, int rows, int cols, int slotSize) {
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                this.addSlotToContainer(new Slot(inv, col + row * cols + from, x + col * slotSize, y + row * slotSize));
            }
        }
    }

}
