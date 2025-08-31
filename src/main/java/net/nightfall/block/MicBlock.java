package net.nightfall.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class MicBlock extends Block {
    // เพิ่ม facing property
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    // กำหนด collision/visual shape ของ block สำหรับแต่ละทิศทาง
    private static final VoxelShape SHAPE_NORTH = VoxelShapes.union(
            // Base
            createCuboidShape(2, 0, 2, 14, 2, 14),
            // Stand vertical
            createCuboidShape(7, 2, 7, 9, 12, 9),
            // Mic head
            createCuboidShape(5, 10, 5, 11, 16, 11)
    );

    private static final VoxelShape SHAPE_SOUTH = VoxelShapes.union(
            // Base
            createCuboidShape(2, 0, 2, 14, 2, 14),
            // Stand vertical
            createCuboidShape(7, 2, 7, 9, 12, 9),
            // Mic head
            createCuboidShape(5, 10, 5, 11, 16, 11)
    );

    private static final VoxelShape SHAPE_EAST = VoxelShapes.union(
            // Base
            createCuboidShape(2, 0, 2, 14, 2, 14),
            // Stand vertical
            createCuboidShape(7, 2, 7, 9, 12, 9),
            // Mic head
            createCuboidShape(5, 10, 5, 11, 16, 11)
    );

    private static final VoxelShape SHAPE_WEST = VoxelShapes.union(
            // Base
            createCuboidShape(2, 0, 2, 14, 2, 14),
            // Stand vertical
            createCuboidShape(7, 2, 7, 9, 12, 9),
            // Mic head
            createCuboidShape(5, 10, 5, 11, 16, 11)
    );

    public MicBlock(Settings settings) {
        super(settings);
        // ตั้งค่า default state
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        // ตั้งทิศทางตามที่ผู้เล่นหันหน้า (opposite direction)
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShapeForState(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShapeForState(state);
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return getShapeForState(state);
    }

    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShapeForState(state);
    }

    // Helper method เพื่อได้ shape ตามทิศทาง
    private VoxelShape getShapeForState(BlockState state) {
        Direction facing = state.get(FACING);
        switch (facing) {
            case NORTH:
                return SHAPE_NORTH;
            case SOUTH:
                return SHAPE_SOUTH;
            case EAST:
                return SHAPE_EAST;
            case WEST:
                return SHAPE_WEST;
            default:
                return SHAPE_NORTH;
        }
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView worldView, BlockPos pos) {
        return 0.2f; // ทำให้มีเงา
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return false;
    }
}