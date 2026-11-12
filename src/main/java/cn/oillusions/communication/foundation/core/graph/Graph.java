package cn.oillusions.communication.foundation.core.graph;

import cn.oillusions.communication.Communication;
import cn.oillusions.communication.foundation.core.Connector;
import cn.oillusions.communication.foundation.core.Envelope;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class Graph extends SavedData {
    private final Map<EdgeID, EdgeMeta> edges;
    private final IDAllocator edgeIdAllocator;

    private final Queue<Task> tasks = new ArrayDeque<>();

    public static final MapCodec<Graph> CODEC = codec(Graph::new);

    @NotNull
    public static <G extends Graph> MapCodec<G> codec(
        @NotNull BiFunction<Map<EdgeID, EdgeMeta>, IDAllocator, G> factory
    ) {
        return RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.unboundedMap(EdgeID.KEY_CODEC, EdgeMeta.CODEC.codec()).fieldOf("edges").forGetter(Graph::edges),
            IDAllocator.CODEC.fieldOf("id_allocator").forGetter(Graph::idAllocator)
        ).apply(i, factory));
    }

    public static final Factory<Graph> FACTORY =
        new Factory<>(
            Graph::new,
            (tag, provider) -> decode(tag, Graph.CODEC, Graph::new),
            null);


    public Graph() {
        this.edges = new HashMap<>();
        this.edgeIdAllocator = new IDAllocator(0);
    }

    public Graph(
        @NotNull Map<EdgeID, EdgeMeta> edges,
        @NotNull IDAllocator allocator
    ) {
        this.edges = new HashMap<>(edges);
        this.edgeIdAllocator = new IDAllocator(allocator.current());
    }

    public void registerInstance(@NotNull Connector connector) {
        EdgeID edgeID = connector.edgeID();
        if (!edges.containsKey(edgeID)) return;
        Medium medium = edges.get(edgeID).medium();

        medium.put(connector);
    }

    @Nullable
    public Connector deregisterInstance(@NotNull Connector connector) {
        EdgeID edgeID = connector.edgeID();
        if (!edges.containsKey(edgeID)) return null;
        Medium medium = edges.get(edgeID).medium();

        return medium.remove(connector);
    }

    public EdgeID put(@NotNull String edgeName, @NotNull Medium medium) {
        EdgeID edgeID = new EdgeID(edgeIdAllocator.allocId());
        edges.put(edgeID, new EdgeMeta(edgeID, edgeName, medium));
        setDirty();
        return edgeID;
    }

    public void setName(@NotNull EdgeID edgeID, @NotNull String edgeName) {
        if (!edges.containsKey(edgeID)) return;
        EdgeMeta edgeMeta = edges.get(edgeID);

        edgeMeta.setEdgeName(edgeName);
        setDirty();
    }

    public boolean remove(@NotNull EdgeID edgeID) {
        if (!edges.containsKey(edgeID)) return true;
        Medium medium = edges.get(edgeID).medium();

        if (medium.cacheCount() != 0) return false;

        edges.remove(edgeID);
        setDirty();
        return true;
    }

    public void forciblyRemove(@NotNull EdgeID edgeID) {
        if (!edges.containsKey(edgeID)) return;

        edges.remove(edgeID);
        setDirty();
    }

    @Nullable
    public String getName(@NotNull EdgeID edgeID) {
        if (!edges.containsKey(edgeID)) return null;

        return edges.get(edgeID).edgeName();
    }

    public ArrayList<Pair<String, EdgeID>> getNameAndIDs() {
        return new ArrayList<>(
            edges.values().stream()
                .map(EdgeMeta::edgeNameAndID)
                .toList()
        );
    }

    public boolean contains(@NotNull EdgeID edgeID) {
        return edges.containsKey(edgeID);
    }

    public void propagate(@NotNull Connector connector, @NotNull Envelope envelope) {
        tasks.add(new Task(connector, envelope, connector.edgeID()));
    }

    public void tick() {
        Task task;
        while ((task = tasks.poll()) != null) {
            if (!edges.containsKey(task.edgeID)) continue;
            Medium medium = edges.get(task.edgeID).medium();

            medium.propagate(task.source, task.envelope);
        }
    }

    @NotNull
    public static <G extends Graph> G get(@NotNull MinecraftServer server, Factory<G> factory, String fileID) {
        return server.overworld().getDataStorage()
            .computeIfAbsent(factory, fileID);
    }

    @Override
    @NotNull
    public CompoundTag save(@NotNull CompoundTag compoundTag, @NotNull HolderLookup.Provider provider) {
        encodeInto(codec(), this, compoundTag);
        return compoundTag;
    }

    @SuppressWarnings("unchecked")
    protected static <G extends Graph> void encodeInto(
        MapCodec<? extends Graph> codec, G graph, CompoundTag tag
    ) {
        ((MapCodec<G>) codec).codec()
            .encodeStart(NbtOps.INSTANCE, graph)
            .resultOrPartial(err -> Communication.LOGGER.error("我cy怎么图序列化炸了, 呃啊 {}", err))
            .ifPresent(nbt -> tag.put("graph", nbt));
    }

    @NotNull
    protected static <G> G decode(@NotNull CompoundTag tag, @NotNull MapCodec<G> codec, @NotNull Supplier<G> fallback) {
        if (!tag.contains("graph")) return fallback.get();

        return codec.codec()
            .parse(NbtOps.INSTANCE, tag.get("graph"))
            .resultOrPartial(err -> Communication.LOGGER.error("我cy怎么图反序列化炸了, 呃啊 {}", err))
            .orElseGet(fallback);
    }

    protected MapCodec<? extends Graph> codec() { return CODEC;}

    protected Map<EdgeID, EdgeMeta> edges() { return edges; }

    protected IDAllocator idAllocator() { return edgeIdAllocator; }

    private record Task(Connector source, Envelope envelope, EdgeID edgeID) {}
}
