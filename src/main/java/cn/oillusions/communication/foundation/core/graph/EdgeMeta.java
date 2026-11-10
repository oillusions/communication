package cn.oillusions.communication.foundation.core.graph;

import cn.oillusions.communication.AllCodecs;
import cn.oillusions.communication.AllRegistries;
import cn.oillusions.communication.foundation.core.Connector;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.ArrayList;

public class EdgeMeta {
    private final EdgeID edgeID;
    private String edgeName;
    public final Medium medium;

    ArrayList<Connector> caches = new ArrayList<>();

    public static final MapCodec<EdgeMeta> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        EdgeID.CODEC.fieldOf("edgeID").forGetter(EdgeMeta::edgeID),
        Codec.STRING.fieldOf("edgeName").forGetter(EdgeMeta::edgeName),
        ResourceLocation.CODEC.fieldOf("mediumID").forGetter(edgeMeta -> edgeMeta.medium().id())
    ).apply(i, (edgeID, edgeName, mediumID) -> {
        Medium medium = AllRegistries.MEDIUMS.get(mediumID);
        if (medium == null) medium = new Medium();

        return new EdgeMeta(
            edgeID,
            edgeName,
            medium
        );
    }));

    public EdgeMeta(
        @NotNull EdgeID edgeID,
        @NotNull String edgeName,
        @NotNull Medium medium
        ) {
        this.edgeID = edgeID;
        this.edgeName= edgeName;
        this.medium = medium;
    }

    public void put(@NotNull Connector connector) {
        if (contains(connector)) return;

        caches.add(connector);
    }

    @Nullable
    public Connector remove(@NotNull Connector connector) {
        if (!contains(connector)) return null;

        return caches.remove(caches.indexOf(connector));
    }

    public int cacheCount() {
        return caches.size();
    }

    public void setEdgeName(@NotNull String edgeName) {
        this.edgeName = edgeName;
    }

    public boolean contains(@NotNull Connector connector) {
        return caches.contains(connector);
    }

    public EdgeID edgeID() {
        return edgeID;
    }

    public String edgeName() {
        return edgeName;
    }

    public Pair<String, EdgeID> edgeNameAndID() {
        return new Pair<>(edgeName, edgeID);
    }

    protected Medium medium() {
        return medium;
    }

    protected ArrayList<Connector> caches() {
        return caches;
    }
}
