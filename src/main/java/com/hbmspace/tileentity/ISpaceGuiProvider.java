package com.hbmspace.tileentity;

import com.hbm.tileentity.IGUIProvider;
import com.hbmspace.main.SpaceMain;

public interface ISpaceGuiProvider extends IGUIProvider {
    @Override
    default Object getModInstanceForGui() {
        return SpaceMain.instance;
    }
}
