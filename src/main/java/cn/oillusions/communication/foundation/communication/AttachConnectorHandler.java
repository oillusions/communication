package cn.oillusions.communication.foundation.communication;

import cn.oillusions.communication.AllRegistries;
import cn.oillusions.communication.foundation.core.Connector;
import cn.oillusions.communication.foundation.core.ConnectorItem;
import cn.oillusions.communication.foundation.core.Slot;
import cn.oillusions.communication.foundation.core.graph.*;
import cn.oillusions.communication.foundation.core.graph.IDAllocator;
import cn.oillusions.communication.content.menu.ChannelSelectMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

import java.util.*;


public class AttachConnectorHandler {
    protected static final Map<Long, Task> tasks = new HashMap<>();
    protected static final IDAllocator taskIDAllocator = new IDAllocator(0);

    public static void connectorAttach(@NotNull Player player, @NotNull Slot slot, @NotNull ConnectorItem<?> connectorItem) {
        Graph graph = ServerGraphHolder.get();

        ArrayList<Pair<String, EdgeID>> nameAndIDs = graph.getNameAndIDs();
        List<ChannelSelectMenu.ChannelEntry> channelEntries = nameAndIDs.stream()
            .map(nameAndID -> new ChannelSelectMenu.ChannelEntry(nameAndID.getB().value(), nameAndID.getA()))
            .toList();

        long taskID = taskIDAllocator.allocId();
        tasks.put(taskID, new Task(player, slot, connectorItem));

        player.openMenu(
            new SimpleMenuProvider(
                (windowID, playerInventory, player_param) -> new ChannelSelectMenu(windowID, playerInventory, player, taskID),
                Component.literal("选择频道")),
            buf -> ChannelSelectMenu.ExtraData.CODEC.encode(buf, new ChannelSelectMenu.ExtraData(taskID, channelEntries))
        );
    }

    public static void continueConnectorAttach(long taskID) {
        if (!tasks.containsKey(taskID)) return;
        Task task = tasks.get(taskID);

        connectorAttach(task.player, task.slot, task.ConnectorItem);
        tasks.remove(taskID);
    }

    public static void closeChannelSelect(long taskID) {
        if (!tasks.containsKey(taskID)) return;
        tasks.remove(taskID);
    }

    public static void returnChannelSelectResult(long taskID, long rawEdgeID) {
        if (!tasks.containsKey(taskID)) return;
        EdgeID edgeID = new EdgeID(rawEdgeID);
        Task task = tasks.get(taskID);

        Graph graph = ServerGraphHolder.get();
        if (graph == null) return;

        if (!graph.contains(edgeID)) return;

        Connector connector = task.ConnectorItem.newConnector(edgeID);
        connector.attachTo(task.slot);
        tasks.remove(taskID);
    }

    public static void returnChannelCreateResult(long selectTaskID, @NotNull String edgeName) {
        if (!tasks.containsKey(selectTaskID)) return;

        Task task = tasks.get(selectTaskID);

        Medium medium = AllRegistries.MEDIUMS.get(task.ConnectorItem.type().supportMediumID());
        if (medium == null) return;

        ServerGraphHolder.get().put(edgeName, medium);

        continueConnectorAttach(selectTaskID);
    }

    public static boolean contains(long taskID) {
        return tasks.containsKey(taskID);
    }

    protected record Task(Player player, Slot slot, ConnectorItem<?> ConnectorItem) {};
}
