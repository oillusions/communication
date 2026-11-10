package cn.oillusions.communication.content.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.createmod.catnip.gui.widget.AbstractSimiWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CandidateBox extends AbstractSimiWidget {
    protected ArrayList<Candidate> allCandidates;
    protected Font font;
    protected int maxDisplayCount = 10;

    public CandidateBox(int x, int y, List<Component> candidates) {
        super(x, y);
        this.allCandidates = new ArrayList<>(candidates.stream()
            .map(entry -> new Candidate(0, entry))
            .toList());

        this.font = Minecraft.getInstance().font;
    }

    public void setCandidates(List<Component> candidates) {
        allCandidates = new ArrayList<>(candidates.stream()
            .map(entry -> new Candidate(0, entry))
            .toList());
    }

    public void setMaxDisplayCount(int maxDisplayCount) {
        this.maxDisplayCount = maxDisplayCount;
    }

    public void onSearchChanged(String text) {
        if (allCandidates.isEmpty()) return;
        int[] codepoints = text.chars().toArray();
        for (Candidate candidate : allCandidates) {
            String content = candidate.value().getString();
            int score = 0;
            for (int i = 0; i < codepoints.length; i++) {
                if (content.lastIndexOf(codepoints[i], i) != -1) score++;
            }
            candidate.setScore(score);
        }
        allCandidates.sort(Comparator.comparingInt(Candidate::score).reversed());
    }

    @Nullable
    public Component primary() {
        if (allCandidates.isEmpty()) return null;
        return allCandidates.getFirst().value();
    }

    @Override
    protected void doRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (allCandidates.isEmpty()) return;
        RenderSystem.setShaderColor(1, 1, 1, 1);
        for (int i = 0; i < allCandidates.size() && i < maxDisplayCount; i++) {
            Candidate candidate = allCandidates.get(i);
            if (candidate.score() == 0) continue;
            graphics.drawString(font, candidate.value(), getX(), getY() + (i * font.lineHeight), 0xFFFFFFFF, false);
        }
    }

    public static class Candidate {
        public int score;
        public Component value;

        public Candidate(
            int score,
            Component value
        ) {
            this.score = score;
            this.value = value;
        }

        public int score() {
            return score;
        }

        public Component value() {
            return value;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public void setValue(Component value) {
            this.value = value;
        }
    }
}
