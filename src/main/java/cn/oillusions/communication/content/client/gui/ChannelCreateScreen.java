package cn.oillusions.communication.content.client.gui;

import cn.oillusions.communication.content.menu.ChannelCreateMenu;
import cn.oillusions.communication.foundation.communication.network.payload.CloseChannelSelectPayload;
import cn.oillusions.communication.foundation.communication.network.payload.ReturnChannelCreateResultPayload;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public class ChannelCreateScreen extends AbstractSimiContainerScreen<ChannelCreateMenu> {
    private final long selectTaskID;
    private EditBox edgeNameInputBox;
    public ChannelCreateScreen(ChannelCreateMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);

        setWindowSize(200, 150);
        this.selectTaskID = menu.selectTaskID();

    }

    @Override
    protected void init() {
        super.init();
        int x = getGuiLeft() + 20, y = getGuiTop() + 30;

        edgeNameInputBox = new EditBox(this.font, x, y, 120, 18, Component.literal("输入频道名称"));
        addRenderableWidgets(edgeNameInputBox);

        IconButton confirm = new IconButton(x + 125, y, AllIcons.I_CONFIRM);
        confirm.withCallback(() -> {
            onCreateEdge(edgeNameInputBox.getValue());
        });
        addRenderableWidgets(confirm);

        this.setFocused(edgeNameInputBox);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {

    }

    protected void onCreateEdge(String edgeName) {
        if (edgeName.isEmpty()) return;
        PacketDistributor.sendToServer(new ReturnChannelCreateResultPayload(selectTaskID, edgeName));
        if (Minecraft.getInstance().player == null) return;
        Minecraft.getInstance().player.closeContainer();
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        boolean isEnter = pKeyCode == GLFW.GLFW_KEY_ENTER || pKeyCode == GLFW.GLFW_KEY_KP_ENTER;

        if (isEnter && this.getFocused() == edgeNameInputBox) {
            onCreateEdge(edgeNameInputBox.getValue());
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public void onClose() {
        PacketDistributor.sendToServer(new CloseChannelSelectPayload(selectTaskID));
        super.onClose();
    }
}
