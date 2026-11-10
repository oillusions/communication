package cn.oillusions.communication.foundation.core;

import cn.oillusions.communication.foundation.core.type.SlotType;

public interface SlotItem<S extends Slot> {
    S newPort(IAdapter adapter, int index);
    SlotType<S> type();
}
