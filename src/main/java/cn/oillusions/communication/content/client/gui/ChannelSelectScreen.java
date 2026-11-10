package cn.oillusions.communication.content.client.gui;

import cn.oillusions.communication.content.client.gui.widget.CandidateBox;
import cn.oillusions.communication.content.menu.ChannelSelectMenu;
import cn.oillusions.communication.foundation.communication.network.payload.CloseChannelSelectPayload;
import cn.oillusions.communication.foundation.communication.network.payload.OpenChannelCreateMenuPayload;
import cn.oillusions.communication.foundation.communication.network.payload.ReturnChannelSelectResultPayload;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class ChannelSelectScreen extends AbstractSimiContainerScreen<ChannelSelectMenu> {
    private EditBox searchBox;
    public CandidateBox candidateBox;
    private final long taskID;
    private final ArrayList<ChannelSelectMenu.ChannelEntry> allChannels;

    public ChannelSelectScreen(ChannelSelectMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.taskID = menu.taskID();
        this.allChannels = new ArrayList<>(menu.allChannels());
        setWindowSize(200, 150);
    }

    @Override
    protected void init() {
        super.init();
        int x = getGuiLeft() + 20, y = getGuiTop() + 30;

        searchBox = new EditBox(this.font, x, y, 120, 18, Component.literal("搜索频道"));
        searchBox.setResponder(this::onSearchChanged);
        addRenderableWidget(searchBox);

        candidateBox = new CandidateBox(x + 2, y + 20, List.of());
        addRenderableWidget(candidateBox);
        if (!allChannels.isEmpty()) {
            candidateBox.setCandidates(allChannels.stream()
                .<Component>map(entry -> Component.literal(entry.edgeName() + "#" + entry.edgeID()))
                .toList());
        }

        IconButton confirm = new IconButton(x + 125, y, AllIcons.I_CONFIRM);
        confirm.withCallback(() -> {
            onConfirm(searchBox.getValue());
        });
        addRenderableWidget(confirm);

        IconButton createNew = new IconButton(x + 150, y, AllIcons.I_ADD);
        createNew.withCallback(() -> {
            PacketDistributor.sendToServer(new OpenChannelCreateMenuPayload(taskID));
        });
        addRenderableWidget(createNew);

        this.setFocused(searchBox);
    }

    protected void onSearchChanged(String text) {
        if (allChannels.isEmpty()) return;
        candidateBox.onSearchChanged(text);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {

    }

    protected void onConfirm(String edge) {
        int index = edge.lastIndexOf("#");
        if (index == -1) return;
        long edgeID = Long.parseLong(edge.substring(index + 1));
        if (allChannels.stream().filter(entry -> entry.edgeID() == edgeID).findFirst().isEmpty()) return;
        PacketDistributor.sendToServer(new ReturnChannelSelectResultPayload(taskID, edgeID));
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        boolean isEnter = pKeyCode == GLFW.GLFW_KEY_ENTER || pKeyCode == GLFW.GLFW_KEY_KP_ENTER;

        if (isEnter && this.getFocused() == searchBox) {
            Component primary = candidateBox.primary();
            if (primary == null) return super.keyPressed(pKeyCode, pScanCode, pModifiers);
            if (!searchBox.getValue().equals(primary.getString())) {
                searchBox.setValue(primary.getString());
            }
        }

        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public void onClose() {
        PacketDistributor.sendToServer(new CloseChannelSelectPayload(taskID));
        super.onClose();
    }
}
