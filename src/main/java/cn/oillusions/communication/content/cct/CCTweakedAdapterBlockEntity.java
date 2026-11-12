package cn.oillusions.communication.content.cct;

import cn.oillusions.communication.content.basic.BasicSlot;
import cn.oillusions.communication.foundation.core.AdapterBlockEntity;
import cn.oillusions.communication.foundation.core.Payload;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;

public class CCTweakedAdapterBlockEntity extends AdapterBlockEntity {
    private Consumer<byte[]> inputBuffer;
    public CCTweakedAdapterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        createDefaultSlots();
    }

    @Override
    protected void createDefaultSlots() {
        slots.clear();
        slots.add(new BasicSlot(this, slotCount()));
    }

    public void setInputBuffer(@NotNull Consumer<byte[]> consumer) {
        this.inputBuffer = consumer;
    }

    public void sendBytes(int index, byte[] bytes) {
        if (slotCount() <= index) return;
        slot(index).send(new CCTweakedPayload(bytes));
    }

    public ArrayList<String> connectorNames() {
        return new ArrayList<>(slots.stream()
            .map(slot -> {
                if (slot.isOccupied()) {
                    ResourceLocation typeID = slot.connector().getType().id();
                    if (typeID == null) return "not registered";
                    return typeID.toString();
                }
                return "null";
            })
            .toList());
    }

    @Override
    public void onReceive(int index, Payload payload) {
        if (payload instanceof CCTweakedPayload(byte[] bytes)) {
            inputBuffer.accept(bytes);
        }
    }
}
