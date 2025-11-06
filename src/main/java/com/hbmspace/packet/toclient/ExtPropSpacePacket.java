package com.hbmspace.packet.toclient;

import com.hbm.packet.threading.PrecompiledPacket;
import com.hbmspace.capability.HbmLivingCapabilitySpace;
import com.hbmspace.capability.HbmLivingPropsSpace;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
// is it viable to clone a packet? probably no
// do I want to try injecting another capability's nbt inside ExtPropPacket? that approach will result in even more mess than this
public class ExtPropSpacePacket extends PrecompiledPacket {
    private NBTTagCompound nbt;

    public ExtPropSpacePacket(){
    }

    public ExtPropSpacePacket(NBTTagCompound nbt){
        this.nbt = nbt;
    }

    @Override
    public void fromBytes(ByteBuf buf){
        PacketBuffer pbuf = new PacketBuffer(buf);
        try {
            this.nbt = pbuf.readCompoundTag();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf){
        if (this.nbt != null) {
            PacketBuffer pbuf = new PacketBuffer(buf);
            pbuf.writeCompoundTag(this.nbt);
        }
    }

    public static class Handler implements IMessageHandler<ExtPropSpacePacket, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(ExtPropSpacePacket m, MessageContext ctx){
            Minecraft.getMinecraft().addScheduledTask(() -> {
                if(Minecraft.getMinecraft().world == null || m.nbt == null)
                    return;

                HbmLivingCapabilitySpace.IEntityHbmProps props = HbmLivingPropsSpace.getData(Minecraft.getMinecraft().player);
                props.loadNBTData(m.nbt);
            });

            return null;
        }
    }
}
