package cn.oillusions.communication.foundation.core;

import cn.oillusions.communication.Communication;
import cn.oillusions.communication.foundation.communication.ServerGraphHolder;
import cn.oillusions.communication.foundation.core.graph.EdgeID;
import cn.oillusions.communication.foundation.core.type.ConnectorType;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Connector {
    protected final EdgeID edgeID;
    protected boolean isStartUp = false;
    protected Slot slot;

    public static final MapCodec<Connector> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        EdgeID.CODEC.fieldOf("edgeID").forGetter(Connector::edgeID)
    ).apply(i, Connector::new));
    public static final ConnectorType<Connector> TYPE = new ConnectorType<>(
        CODEC,
        Communication.modLoc("base"),
        Set.of()
    );

    @NotNull
    public ConnectorType<?> getType() {
        return TYPE;
    }

    public Connector(@NotNull EdgeID edgeID) {
        this.edgeID = edgeID;
        this.slot = null;
    }

    public boolean isAttached() {
        return slot != null;
    }

    public boolean attachTo(@NotNull Slot slot) {
        if (isAttached()) return false;
        if (slot.isClientSide()) return false;
        if (slot.isOccupied()) return false;
        if (!canAttachTo(slot)) return false;

        this.slot = slot;
        if (!slot.attach(this)) {
            this.slot = null;
            return false;
        }
        onAttach();
        return true;
    }

    public boolean detach() {
        if (isClientSide()) return false;
        if (!isAttached()) return true;
        if (!canDetach()) return false;

        onDetach();
        if (!slot.detach()) return false;
        this.slot = null;
        return true;
    }

    public void forciblyDetach() {
        if (isClientSide()) return;
        if (!isAttached()) return;

        onDetach();
        slot.forciblyDetach();
        this.slot = null;
    }

    public void startUp() {
        if (isClientSide()) return;
        if (!isAttached()) return;
        if (isStartUp()) return;

        isStartUp = true;
        onStartUp();
    }

    public void shutDown() {
        if (isClientSide()) return;
        if (!isAttached()) return;
        if (!isStartUp()) return;

        isStartUp = false;
        onShutDown();
    }

    public void send(Payload payload) {
        if (isClientSide()) return;
        if (!isAttached()) return;

        onSendPayload(payload);
    }

    public void receive(Envelope envelope) {
        if (isClientSide()) return;
        if (!isAttached()) return;

        onReceiveEnvelope(envelope);
        slot.receive(envelope.payload());
    }

    @NotNull
    public EdgeID edgeID() {
        return edgeID;
    }

    public boolean isClientSide() {
        if (!isAttached()) return false;
        return slot.isClientSide();
    }

    public boolean canAttachTo(Slot slot) {
        if (isAttached()) return false;

        return getType().supports(slot);
    }

    public boolean canDetach() { return true; }

    public boolean isStartUp() {
        if (!isAttached()) return false;

        return isStartUp;
    }

    protected void onAttach() { return; }
    protected void onDetach() { return; }

    protected void onStartUp() {
        ServerGraphHolder.get().registerInstance(this);
    }
    protected void onShutDown() {
        ServerGraphHolder.get().deregisterInstance(this);
    }

    protected void onSendPayload(Payload payload) { return; }
    protected void onReceiveEnvelope(Envelope envelope) { return; }


    protected void setSlot(Slot slot) {
        this.slot = slot;
    }
}
