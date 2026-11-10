package cn.oillusions.communication.content.basic;

import cn.oillusions.communication.foundation.core.IAdapter;
import cn.oillusions.communication.foundation.core.Slot;
import cn.oillusions.communication.foundation.core.type.SlotType;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

public class BasicSlot extends Slot {

    public static final MapCodec<BasicSlot> CODEC = codec(BasicSlot::new);
    public static final SlotType<BasicSlot> TYPE = new SlotType<>(CODEC);

    public BasicSlot() {
        super();
    }

    public BasicSlot(IAdapter adapter, int index) {
        super(adapter, index);
    }

    @Override
    @NotNull
    public SlotType<?> getType() {
        return TYPE;
    }

    protected Slot base() {
        return this;
    }
}
