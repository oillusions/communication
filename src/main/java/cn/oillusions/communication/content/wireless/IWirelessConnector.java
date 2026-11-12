package cn.oillusions.communication.content.wireless;

import net.minecraft.world.phys.Vec3;

public interface IWirelessConnector {
    Vec3 getPos();

    double getWavelength();
    double getTransmitPower();
    double getReceivedPower();
}
