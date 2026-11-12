package cn.oillusions.communication.foundation.core.graph;

import cn.oillusions.communication.AllCodecs;
import cn.oillusions.communication.AllRegistries;
import cn.oillusions.communication.foundation.core.Connector;
import cn.oillusions.communication.foundation.core.type.MediumType;
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

    public static final MapCodec<EdgeMeta> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        EdgeID.CODEC.fieldOf("edgeID").forGetter(EdgeMeta::edgeID),
        Codec.STRING.fieldOf("edgeName").forGetter(EdgeMeta::edgeName),
        ResourceLocation.CODEC.fieldOf("mediumID").forGetter(edgeMeta -> edgeMeta.medium().getType().id()),
        AllCodecs.MEDIUM_CODEC.fieldOf("medium").forGetter(EdgeMeta::medium)
    ).apply(i, (edgeID, edgeName, mediumID, medium) -> {
//        MediumType<?> mediumType = AllRegistries.MEDIUMS.get(mediumID);
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

    public EdgeID edgeID() {
        return edgeID;
    }

    public void setEdgeName(@NotNull String edgeName) {
        this.edgeName = edgeName;
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
}
