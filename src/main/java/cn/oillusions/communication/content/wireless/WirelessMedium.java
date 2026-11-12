package cn.oillusions.communication.content.wireless;

import cn.oillusions.communication.foundation.core.Connector;
import cn.oillusions.communication.foundation.core.Envelope;
import cn.oillusions.communication.foundation.core.graph.Medium;
import cn.oillusions.communication.foundation.core.type.MediumType;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class WirelessMedium extends Medium {
    public static final MapCodec<WirelessMedium> CODEC = MapCodec.unit(WirelessMedium::new);
    public static final MediumType<WirelessMedium> TYPE = new MediumType<>(CODEC, WirelessMedium::new);

    @Override
    public @NotNull MediumType<?> getType() {
        return TYPE;
    }

    public static double FSPL(double distance, double wavelength) {
        if (distance <= 0) distance = 0.01;
        if (wavelength <= 0) wavelength = 0.01;
        return 20 * Math.log10((4 * Math.PI * distance) / wavelength);
    }

    @Override
    public void propagate(@NotNull Connector source, @NotNull Envelope envelope) {
        if (source instanceof IWirelessConnector wirelessSource) {
            Vec3 sourcePos = wirelessSource.getPos();
            for (Connector target : caches) {
                if (source == target) continue;
                if (target instanceof IWirelessConnector wirelessTarget) {
                    double distance = sourcePos.distanceTo(wirelessTarget.getPos());

                    double receivePower = wirelessSource.getTransmitPower() - FSPL(
                        distance, wirelessSource.getWavelength());
                    if (wirelessTarget.getReceivedPower() < receivePower) {
                        target.receive(envelope);
                    }
                }
            }
        }
    }


    @Override
    public boolean canCache(Connector connector) {
        return connector instanceof IWirelessConnector;
    }
}
