package cn.oillusions.communication.foundation.core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AdapterBlock extends Block implements EntityBlock {
    public AdapterBlock(Properties properties) {
        super(properties);
    }

    public ArrayList<VoxelShape> slotShapes(BlockState state) {
        return new ArrayList<>(List.of());
    }

    @Override
    @Nullable
    public abstract BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState);
}
