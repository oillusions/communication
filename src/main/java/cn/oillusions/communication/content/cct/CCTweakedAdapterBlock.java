package cn.oillusions.communication.content.cct;

import cn.oillusions.communication.AllBlockEntities;
import cn.oillusions.communication.foundation.communication.AttachConnectorHandler;
import cn.oillusions.communication.foundation.core.AdapterBlock;
import cn.oillusions.communication.foundation.core.AdapterBlockEntity;
import cn.oillusions.communication.foundation.core.ConnectorItem;
import cn.oillusions.communication.foundation.core.Slot;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CCTweakedAdapterBlock extends AdapterBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public CCTweakedAdapterBlock(Properties properties) {
        super(properties);

        registerDefaultState(getStateDefinition().any()
            .setValue(FACING, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return this.defaultBlockState()
            .setValue(FACING, context.getClickedFace());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new CCTweakedAdapterBlockEntity(AllBlockEntities.CC_TWEAKED_ADAPTER_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    @NotNull
    protected ItemInteractionResult useItemOn(
        @NotNull ItemStack stack,
        @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
        @NotNull Player player, @NotNull InteractionHand hand,
        @NotNull BlockHitResult hit
    ) {
        if (!level.isClientSide) {
            Item item = stack.getItem();
            BlockEntity entity = level.getBlockEntity(pos);
            if (item instanceof ConnectorItem<?> connectorItem) {
                if (entity instanceof AdapterBlockEntity counterEntity) {
                    Vec3 relative = hit.getLocation().subtract(Vec3.atLowerCornerOf(pos)).scale(0.99);
                    List<VoxelShape> portShapes = slotShapes(state);
                    for (int i = 0; i < portShapes.size(); i++) {
                        if (!portShapes.get(i).bounds().contains(relative)) continue;

                        Slot slot = counterEntity.slot(i);
                        if (slot.isOccupied()) continue;

                        AttachConnectorHandler.connectorAttach(player, slot, connectorItem);
                        break;
                    }
                }
            }
        }
        return ItemInteractionResult.FAIL;
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos supportBlockPos = pos.relative(facing.getOpposite());

        return canSupportCenter(level, supportBlockPos, facing);
    }

    @Override
    @NotNull
    protected BlockState updateShape(
        @NotNull BlockState state, @NotNull Direction direction,
        @NotNull BlockState neighborState, @NotNull LevelAccessor level,
        @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (!canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return state;
    }

    public static final VoxelShape SHAPE = Shapes.or(
        Block.box(4, 0, 4, 12, 1, 12),
        Block.box(5, 1, 5, 11, 3, 11)
    );

    public static final VoxelShaper SHAPE_UP = VoxelShaper.forDirectional(SHAPE, Direction.UP);

    @Override
    @NotNull
    protected VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE_UP.get(state.getValue(FACING));
    }

    @Override
    public ArrayList<VoxelShape> slotShapes(BlockState state) {
        return new ArrayList<>(List.of(SHAPE_UP.get(state.getValue(FACING))));
    }
}
