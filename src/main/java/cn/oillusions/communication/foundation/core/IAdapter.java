package cn.oillusions.communication.foundation.core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public interface IAdapter {
    int slotCount();
    Slot slot(int index);

    void onReceive(int index, Payload payload);

    void onAttached(int index);
    void onDetached(int index);

    Vec3 getPos();

    boolean isClientSide();
}
