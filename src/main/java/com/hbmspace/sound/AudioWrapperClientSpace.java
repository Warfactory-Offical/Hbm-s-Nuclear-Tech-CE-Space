package com.hbmspace.sound;

import com.hbm.sound.AudioDynamic;
import com.hbm.sound.AudioWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class AudioWrapperClientSpace extends AudioWrapper {

    AudioDynamic sound;

    public AudioWrapperClientSpace(SoundEvent loc, SoundCategory cat, Entity entity) {
        if(loc != null && cat != null && entity != null && !entity.isDead)
            sound = new AudioDynamicEntity(loc, cat, entity);
    }

    @Override
    public void setKeepAlive(int keepAlive) {
        if(sound != null) sound.setKeepAlive(keepAlive);
    }

    @Override
    public void keepAlive() {
        if(sound != null) sound.keepAlive();
    }

    @Override
    public void updatePosition(float x, float y, float z) {
        if(sound != null) sound.setPosition(x, y, z);
    }

    @Override
    public void updateVolume(float volume) {
        if(sound != null) sound.setVolume(volume);
    }

    @Override
    public void updateRange(float range) {
        if(sound != null) sound.setRange(range);
    }

    @Override
    public void updatePitch(float pitch) {
        if(sound != null) sound.setPitch(pitch);
    }

    @Override
    public float getVolume() {
        if(sound != null) return sound.getVolume();
        return 1;
    }

    @Override
    public float getPitch() {
        if(sound != null) return sound.getPitch();
        return 1;
    }

    @Override
    public void startSound() {
        if(sound != null) sound.start();
    }

    @Override
    public void stopSound() {
        if(sound != null) {
            sound.stop();
            sound.setKeepAlive(0);
        }
    }

    @Override
    public boolean isPlaying() {
        return sound != null ? sound.isPlaying() : false;
    }
}
