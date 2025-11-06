package com.hbmspace.packet.toserver;

import com.hbmspace.inventory.slots.SlotLayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class GuiLayerPacket implements IMessage {

    private int layer;

    public GuiLayerPacket() {}

    public GuiLayerPacket(int layer) {
        this.layer = layer;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        layer = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(layer);
    }

    public static class Handler implements IMessageHandler<GuiLayerPacket, IMessage> {

        @Override
        public IMessage onMessage(GuiLayerPacket message, MessageContext ctx) {
            if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                return null;

            EntityPlayerMP player = ctx.getServerHandler().player;
            if(player.openContainer != null) {
                for(int i = 0; i < player.openContainer.inventorySlots.size(); i++) {
                    Slot slot = player.openContainer.inventorySlots.get(i);
                    if(slot instanceof SlotLayer layer) {
                        layer.setLayer(message.layer);
                    }
                }
            }

            return null;
        }

    }

}
