package com.hbmspace.inventory.gui;

import com.hbm.inventory.gui.GuiInfoContainer;
import com.hbm.packet.PacketDispatcher;
import com.hbmspace.inventory.slots.SlotLayer;
import com.hbmspace.packet.toserver.GuiLayerPacket;
import net.minecraft.inventory.Container;

public abstract class GuiInfoContainerLayered extends GuiInfoContainer {

    protected int currentLayer = 0;

    public GuiInfoContainerLayered(Container container) {
        super(container);
    }

    public void setLayer(int layer) {
        currentLayer = layer;
        for(Object o : inventorySlots.inventorySlots) {
            if(!(o instanceof SlotLayer slot)) continue;
            slot.setLayer(layer);
        }

        PacketDispatcher.wrapper.sendToServer(new GuiLayerPacket(layer));
    }

    public int getLayer() {
        return currentLayer;
    }

}
