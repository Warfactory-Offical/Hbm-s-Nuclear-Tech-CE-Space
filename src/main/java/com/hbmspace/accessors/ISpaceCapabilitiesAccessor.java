package com.hbmspace.accessors;

import com.hbmspace.dim.trait.CBT_Atmosphere;

public interface ISpaceCapabilitiesAccessor {
    int getOxygen();
    void setOxygen(int oxygen);
    CBT_Atmosphere getAtmosphere();
    void setAtmosphere(CBT_Atmosphere atmosphere);
    boolean hasGravity();
}
