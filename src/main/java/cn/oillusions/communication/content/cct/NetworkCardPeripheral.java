package cn.oillusions.communication.content.cct;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class NetworkCardPeripheral implements IPeripheral {
    private final Queue<byte[]> inputBuffers = new ArrayDeque<>();
    private final Set<IComputerAccess> attachedComputers = new HashSet<>();

    public NetworkCardPeripheral(@NotNull CCTweakedAdapterBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
        blockEntity.setInputBuffer(this::inputBuffer);
    }
    private final CCTweakedAdapterBlockEntity blockEntity;

    @Override
    @NotNull
    public String getType() {
        return "network_card";
    }

    @Override
    public void attach(@NonNull IComputerAccess computer) {
        this.attachedComputers.add(computer);
    }

    @Override
    public void detach(@NotNull IComputerAccess computer) {
        this.attachedComputers.remove(computer);
    }

    @LuaFunction(mainThread = false)
    public final int connectorCount() {
        return blockEntity.connectorNames().size();
    }

    @LuaFunction(mainThread = false)
    public final ArrayList<String> connectorNames() {
        return blockEntity.connectorNames();
    }

    @LuaFunction(mainThread = false)
    public final int packetCount() {
        return inputBuffers.size();
    }

    @LuaFunction(mainThread = false)
    @NotNull
    public final String readPacket() {
        if (inputBuffers.isEmpty()) return "";
        return new String(inputBuffers.poll(), StandardCharsets.ISO_8859_1);
    }

    @LuaFunction(mainThread = false)
    public final void sendPacket(int index, @NotNull String bytes) {
        blockEntity.sendBytes(index, bytes.getBytes(StandardCharsets.ISO_8859_1));
    }

    @Override
    public boolean equals(@org.jspecify.annotations.Nullable IPeripheral other) {
        if (this == other) return true;
        if (!(other instanceof NetworkCardPeripheral p)) return false;
        return p.blockEntity == this.blockEntity;
    }

    protected void inputBuffer(byte[] bytes) {
        inputBuffers.add(bytes);
        for (IComputerAccess computer : attachedComputers) {
            computer.queueEvent("network_packet");
        }
    }
}
