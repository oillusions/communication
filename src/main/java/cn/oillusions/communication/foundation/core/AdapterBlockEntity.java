package cn.oillusions.communication.foundation.core;

import cn.oillusions.communication.AllCodecs;
import cn.oillusions.communication.Communication;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdapterBlockEntity extends BlockEntity implements IAdapter, IHaveGoggleInformation {
    protected boolean isChunkUnload = false;
    protected final ArrayList<Slot> slots = new ArrayList<>();

    protected CompoundTag encodeSlots() {
        if (slotCount() == 0) return new CompoundTag();

        CompoundTag tag = new CompoundTag();
        AllCodecs.SLOT_CODEC.listOf()
            .encodeStart(NbtOps.INSTANCE, this.slots)
            .resultOrPartial(err -> Communication.LOGGER.error("适配器方块实体插槽组序列化异常 {}", err))
            .ifPresent(nbt -> tag.put("slots", nbt));

        return tag;
    }

    protected void parseSlots(@NotNull CompoundTag tag) {
        this.slots.clear();
        if (!tag.contains("slots")) {
            if (isClientSide()) return;
            createDefaultSlots();
            return;
        }

        AllCodecs.SLOT_CODEC.listOf()
            .parse(NbtOps.INSTANCE, tag.get("slots"))
            .resultOrPartial(Communication.LOGGER::error)
            .ifPresent(list -> {
                for (int i = 0; i < list.size(); i++) {
                    Slot slot = list.get(i);
                    slot.setAdapter(this, i);
                    this.slots.add(slot);
                }
            });
    }

    protected void createDefaultSlots() {
        this.slots.clear();
    }

    public AdapterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void loadAdditional(
        @NotNull CompoundTag tag,
        @NotNull HolderLookup.Provider regs
    ) {
        super.loadAdditional(tag, regs);
        parseSlots(tag.getCompound("data"));
    }

    @Override
    protected void saveAdditional(
        @NotNull CompoundTag tag,
        @NotNull HolderLookup.Provider regs
    ) {
        super.saveAdditional(tag, regs);
        tag.put("data", encodeSlots());
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider regs) {
        CompoundTag tag = super.getUpdateTag(regs);
        tag.put("data", encodeSlots());
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (isClientSide()) return;
        for (Slot slot : slots) {
            slot.startUp();
        }
    }

    @Override
    public void onChunkUnloaded() {
        if (!isClientSide()) {
            for (Slot slot : slots) {
                slot.shutDown();
            }
        }
        isChunkUnload = true;
        super.onChunkUnloaded();
    }

    @Override
    public void setRemoved() {
        if (!isClientSide() && !isChunkUnload) {
            for (Slot slot : slots) {
                slot.shutDown();
                Connector connector = slot.connector();
                if (connector == null) continue;
                connector.forciblyDetach();
            }
        }
        isChunkUnload = false;
        super.setRemoved();
    }

    @Override
    public int slotCount() {
        return slots.size();
    }

    @Override
    public Slot slot(int index) {
        return slots.get(index);
    }

    @Override
    public void onReceive(int index, Payload payload) {
        return;
    }

    @Override
    public void onAttached(int index) {
        if (isClientSide()) return;
        slot(index).startUp();

        if (level != null) level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), AdapterBlock.UPDATE_CLIENTS);
    }

    @Override
    public void onDetached(int index) {
        if (isClientSide()) return;

        if (level != null) level.blockUpdated(getBlockPos(), getBlockState().getBlock());
    }

    @Override
    public Vec3 getPos() {
        Vec3 pos = Vec3.atCenterOf(getBlockPos());
//        if (Communication.hasMod(Communication.SableID)) {
//            SubLevel subLevel = Sable.HELPER.getContaining(getLevel(), pos);
//            if (subLevel == null) {
//                return pos;
//            } else {
//                return pos;
//            }
//        } else {
//            return pos;
//        }
        return pos;
    }

    public boolean isClientSide() {
        return level == null || level.isClientSide;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        CreateLang.builder()
            .add(Component.translatable("block.create_communication.adapter.tooltip.title"))
            .forGoggles(tooltip);

        CreateLang.builder()
            .add(Component.literal("端口状态："))
            .forGoggles(tooltip);

        for (int i = 0; i < slotCount(); i++) {
            CreateLang.builder()
                .add(Component.literal(slot(i).getType().getName().getString())
                    .append(Component.literal("#" + i + ": ")
                        .withStyle(ChatFormatting.GRAY)
                        .append(slot(i).isOccupied()
                        ? Component.translatable("block.create_communication.adapter.tooltip.slot.occupied").withStyle(ChatFormatting.GREEN)
                        : Component.translatable("block.create_communication.adapter.tooltip.slot.free").withStyle(ChatFormatting.YELLOW)
                        )))
                .forGoggles(tooltip);
        }
        return true;
    }
}
