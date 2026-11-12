package cn.oillusions.communication.content.wireless;

import cn.oillusions.communication.AllMediums;
import cn.oillusions.communication.AllSlots;
import cn.oillusions.communication.foundation.communication.ServerGraphHolder;
import cn.oillusions.communication.foundation.core.Connector;
import cn.oillusions.communication.foundation.core.Envelope;
import cn.oillusions.communication.foundation.core.Payload;
import cn.oillusions.communication.foundation.core.graph.EdgeID;
import cn.oillusions.communication.foundation.core.type.ConnectorType;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class WirelessConnector extends Connector implements IWirelessConnector {
    public static final MapCodec<WirelessConnector> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        Connector.CODEC.forGetter(WirelessConnector::asBase)
    ).apply(i, (base) -> new WirelessConnector(base.edgeID())));
    public static final ConnectorType<WirelessConnector> TYPE = new ConnectorType<>(
        CODEC,
        AllMediums.WIRELESS_MEDIUM.getId(),
        Set.of(AllSlots.BASIC_PORT.getId())
    );

    public WirelessConnector(@NotNull EdgeID edgeID) {
        super(edgeID);
    }

    @Override
    public @NotNull ConnectorType<?> getType() {
        return TYPE;
    }

    @Override
    protected void onSendPayload(@NotNull Payload payload) {
        ServerGraphHolder.get().propagate(this, new Envelope(payload, null));
    }

    private Connector asBase() {
        return this;
    }

    @Override
    public Vec3 getPos() {
        if (!isAttached()) return new Vec3(0, 0, 0);
        return slot.adapter().getPos();
    }

    @Override
    public double getWavelength() {
        return 0.125;
    }

    @Override
    public double getTransmitPower() {
        return 20.0;
    }

    @Override
    public double getReceivedPower() {
        return -70.0;
    }
}
