package cn.oillusions.communication.foundation.core.graph;

import cn.oillusions.communication.foundation.core.Connector;
import cn.oillusions.communication.foundation.core.Envelope;
import cn.oillusions.communication.foundation.core.type.MediumType;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class Medium {
    protected ArrayList<Connector> caches = new ArrayList<>();

    public static final MapCodec<Medium> CODEC = MapCodec.unit(Medium::new);
    public static final MediumType<Medium> TYPE = new MediumType<>(CODEC, Medium::new);

    @NotNull
    public MediumType<?> getType() {
        return TYPE;
    }

    public void put(@NotNull Connector connector) {
        if (contains(connector)) return;
        if (!canCache(connector)) return;

        caches.add(connector);
    }

    @Nullable
    public Connector remove(@NotNull Connector connector) {
        if (!contains(connector)) return null;

        return caches.remove(caches.indexOf(connector));
    }

    public void propagate(
        @NotNull Connector source,
        @NotNull Envelope envelope
        ) {
        for (Connector connector : caches) {
            if (connector == source) continue;
            connector.receive(envelope);
        }
    }

    public boolean canCache(Connector connector) {
        return true;
    }

    public int cacheCount() {
        return caches.size();
    }

    public boolean contains(@NotNull Connector connector) {
        return caches.contains(connector);
    }

    protected ArrayList<Connector> caches() {
        return caches;
    }
}
