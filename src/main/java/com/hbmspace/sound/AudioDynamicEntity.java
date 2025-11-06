package com.hbmspace.sound;

import com.hbm.sound.AudioDynamic;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class AudioDynamicEntity extends AudioDynamic {
    protected Entity entity;

    protected AudioDynamicEntity(SoundEvent loc, SoundCategory cat, Entity entity) {
        super(loc, cat, true);
        this.entity = entity;
        this.xPosF = (float)entity.posX;
        this.yPosF = (float)entity.posY;
        this.zPosF = (float)entity.posZ;
    }

    @Override
    public void update() {
        super.update();

        if(entity == null || entity.isDead) {
            stop();
            entity = null;
            return;
        }
    }

    @Override
    public float getXPosF() {
        if(entity == null) return super.getXPosF();
        return (float)entity.posX;
    }

    @Override
    public float getYPosF() {
        if(entity == null) return super.getYPosF();
        return (float)entity.posY;
    }

    @Override
    public float getZPosF() {
        if(entity == null) return super.getZPosF();
        return (float)entity.posZ;
    }
}
