package cn.oillusions.communication.content.redstone.block.entity;

import cn.oillusions.communication.content.redstone.RedstoneSignalStrengthPayload;
import cn.oillusions.communication.content.redstone.block.RedstoneAdapterBlock;
import cn.oillusions.communication.foundation.core.AdapterBlockEntity;
import cn.oillusions.communication.content.basic.BasicSlot;
import cn.oillusions.communication.foundation.core.Connector;
import cn.oillusions.communication.foundation.core.Payload;
import cn.oillusions.communication.foundation.core.graph.EdgeID;
import cn.oillusions.communication.foundation.core.graph.IDAllocator;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedstoneAdapterEntity extends AdapterBlockEntity {
    protected static final IDAllocator idAllocator = new IDAllocator(0);
    public final long id;
    protected final Map<Long, Byte> redstoneLevels = new HashMap<>();

    private int redstoneLevel;
    public RedstoneAdapterEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        createDefaultSlots();
        redstoneLevel = 0;
        this.id = idAllocator.allocId();
    }

    public void sendRedstoneSignal(int level) {
        for (int i = 0; i < slotCount(); i++) {
            slot(i).send(new RedstoneSignalStrengthPayload(id, (byte) level));
        }
    }

    @Override
    public void setRemoved() {
        sendRedstoneSignal(0);
        setRedstoneLevel(0);
        super.setRemoved();
    }

    @Override
    protected void createDefaultSlots() {
        slots.clear();
        slots.add(new BasicSlot(this, slotCount()));
    }

    @Override
    public void onReceive(int index, Payload payload) {
        if (payload instanceof RedstoneSignalStrengthPayload(long id1, byte level1)) {
            if (slot(index).connector() instanceof Connector connector) {
                redstoneLevels.put(id1, level1);

                setRedstoneLevel(Collections.max(redstoneLevels.values()));
            }
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        CreateLang.builder()
            .add(Component.literal("适配器状态: ")
                .append(getBlockState().getValue(RedstoneAdapterBlock.TRANSMITTER)
                    ? Component.literal("发射器")
                    : Component.literal("接收器")))
            .forGoggles(tooltip);

        return true;
    }

    public void setRedstoneLevel(int newLevel) {
        newLevel = Mth.clamp(newLevel, 0, 15);
        if (this.redstoneLevel == newLevel) return;
        this.redstoneLevel = newLevel;

        if (level != null && !level.isClientSide) {
            setChanged();
            BlockState state = getBlockState();
            Block block = state.getBlock();
            level.blockUpdated(getBlockPos(), block);

            Direction facing = state.getValue(RedstoneAdapterBlock.FACING);
            BlockPos supportBlockPos = getBlockPos().relative(facing.getOpposite());

            Block supportBlock = level.getBlockState(supportBlockPos).getBlock();
            level.blockUpdated(supportBlockPos, supportBlock);
        }
    }

    public int redstoneLevel() {
        return redstoneLevel;
    }
}
