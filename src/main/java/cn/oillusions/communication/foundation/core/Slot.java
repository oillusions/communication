package cn.oillusions.communication.foundation.core;

import cn.oillusions.communication.AllCodecs;
import cn.oillusions.communication.foundation.core.type.SlotType;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public class Slot {
    protected IAdapter adapter;
    protected int index;
    protected Connector connector;

    public static final MapCodec<Slot> CODEC = codec(Slot::new);

    public static <S extends Slot> MapCodec<S> codec(Supplier<S> supplier) {
        return RecordCodecBuilder.mapCodec(i -> i.group(
            AllCodecs.CONNECTOR_CODEC.optionalFieldOf("connector").forGetter(Slot::getConnector)
        ).apply(i, (connector) -> {
            S slot = supplier.get();
            connector.ifPresent(c -> {
                c.setSlot(slot);
                slot.setConnector(c);
            });
            return slot;
        }));
    }

    public static final SlotType<Slot> TYPE = new SlotType<>(CODEC);

    @NotNull
    public SlotType<?> getType() {
        return TYPE;
    }

    public Slot() {
        this.adapter = null;
        this.index = 0;
        this.connector = null;
    }

    public Slot(IAdapter adapter, int index) {
        this.adapter = adapter;
        this.index = index;
        this.connector = null;
    }

    public boolean attach(@NotNull Connector connector) {
        if (isClientSide()) return false;
        if (isOccupied()) return false;

        onAttach(connector);
        this.connector = connector;
        adapter.onAttached(index);
        return true;
    }

    public boolean detach() {
        if (isClientSide()) return false;
        if (isFree()) return true;
        if (isLocked()) return false;

        onDetach();
        setConnector(null);
        adapter.onDetached(index);
        return true;
    }

    public void forciblyDetach() {
        if (isClientSide()) return;
        if (isFree()) return;

        onDetach();
        setConnector(null);
    }

    public void startUp() {
        if (isClientSide()) return;
        if (!isOccupied()) return;
        if (isStartUp()) return;

        connector.startUp();
        onStartUp();
    }

    public void shutDown() {
        if (isClientSide()) return;
        if (!isOccupied()) return;
        if (!isStartUp()) return;

        connector.shutDown();
        onShutDown();
    }

    public void send(@NotNull Payload payload) {
        if (isClientSide()) return;
        if (isFree()) return;

        connector.send(payload);
    }

    public void receive(@NotNull Payload payload) {
        if (isClientSide()) return;

        adapter.onReceive(index, payload);
    }

    @Nullable
    public Connector connector() {
        return connector;
    }

    public boolean isFree() {
        return connector == null;
    }

    public boolean isOccupied() {
        return connector != null;
    }

    public boolean isLocked() {
        return isOccupied() && !connector.canDetach();
    }

    public boolean isClientSide() {
        if (adapter == null) return true;

        return adapter.isClientSide();
    }

    public boolean isStartUp() {
        if (!isOccupied()) return false;
        return connector.isStartUp();
    }

    protected void onAttach(@NotNull Connector connector) { return; }
    protected void onDetach() { return; }

    protected void onStartUp() { return; }
    protected void onShutDown() { return; }


    protected void setConnector(Connector connector) {
        this.connector = connector;

    }

    @Nullable
    public IAdapter adapter() {
        return adapter;
    }

    public Optional<Connector> getConnector() {
        return Optional.ofNullable(connector);
    }

    protected void setAdapter(IAdapter adapter, int index) {
        this.adapter = adapter;
        this.index = index;
    }
}
