package cn.oillusions.communication.content.basic.item;

import cn.oillusions.communication.content.basic.BasicSlot;
import cn.oillusions.communication.foundation.core.IAdapter;
import cn.oillusions.communication.foundation.core.SlotItem;
import cn.oillusions.communication.foundation.core.type.SlotType;
import net.minecraft.world.item.Item;

public class BasicSlotItem extends Item implements SlotItem<BasicSlot> {
    public BasicSlotItem(Properties properties) {
        super(properties);
    }


    @Override
    public BasicSlot newPort(IAdapter adapter, int index) {
        return new BasicSlot(adapter, index);
    }

    @Override
    public SlotType<BasicSlot> type() {
        return BasicSlot.TYPE;
    }
}
