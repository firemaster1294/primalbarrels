package com.im1294.primalbarrels.block;

import com.im1294.primalbarrels.tile.PrimalBarrelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.IntStream;

@SuppressWarnings("deprecation")
public class PrimalBarrelBlock extends BarrelBlock {

  protected final BlockEntityType.BlockEntitySupplier<BlockEntity> tileEntitySupplier;

  public PrimalBarrelBlock(Properties properties, BlockEntityType.BlockEntitySupplier<BlockEntity> tileEntitySupplier) {
    super(properties);
    this.tileEntitySupplier = tileEntitySupplier;
  }

  @Override
  public void onRemove(BlockState state, @Nonnull Level worldIn,@Nonnull BlockPos pos,@Nonnull BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      BlockEntity tileentity = worldIn.getBlockEntity(pos);
      if (tileentity instanceof PrimalBarrelBlockEntity) {
        dropItems((PrimalBarrelBlockEntity)tileentity,worldIn, pos);
        worldIn.updateNeighbourForOutputSignal(pos, this);
      }
      super.onRemove(state, worldIn, pos, newState, isMoving);
    }
  }

  public static void dropItems(PrimalBarrelBlockEntity barrel, Level world, BlockPos pos) {
    IntStream.range(0, barrel.handler.getSlots()).mapToObj(barrel.handler::getStackInSlot)
            .filter(stack -> !stack.isEmpty()).forEach(stack -> Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack));
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos,
                               Player player, InteractionHand hand, BlockHitResult result) {
    if (!world.isClientSide) {
      MenuProvider tileEntity = getMenuProvider(state,world,pos);
      if (tileEntity != null) {
        PrimalBarrelBlockEntity primalBarrelBlockEntity = (PrimalBarrelBlockEntity)tileEntity;
        world.setBlock(pos, state.setValue(BarrelBlock.OPEN, true), 3);
        if (primalBarrelBlockEntity.players == 0) {
          primalBarrelBlockEntity.soundStuff(state, SoundEvents.BARREL_OPEN);
          primalBarrelBlockEntity.changeState(state, true);
        }
        primalBarrelBlockEntity.players++;
        player.openMenu(tileEntity);
        player.awardStat(Stats.OPEN_BARREL);
      }
      return InteractionResult.CONSUME;

    } else {
      return InteractionResult.SUCCESS;
    }
  }
  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos,BlockState state) {
    return tileEntitySupplier.create(pos, state);
  }

  /**
   * @deprecated call via {@link BlockState#hasAnalogOutputSignal()} ()} whenever possible. Implementing/overriding
   * is fine.
   */
  @Override
  public boolean hasAnalogOutputSignal(BlockState state) {
    return true;
  }

  /**
   * @deprecated call via {@link BlockState#getAnalogOutputSignal(Level, BlockPos)} (World,BlockPos)} whenever possible.
   * Implementing/overriding is fine.
   */
  @Override
  public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos) {
    BlockEntity barrel = world.getBlockEntity(pos);
    return barrel instanceof PrimalBarrelBlockEntity ? ItemHandlerHelper.calcRedstoneFromInventory(((PrimalBarrelBlockEntity) barrel).handler) : 0;
  }

  public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
    if (pStack.hasCustomHoverName()) {
      BlockEntity blockentity = pLevel.getBlockEntity(pPos);
      if (blockentity instanceof PrimalBarrelBlockEntity primalBarrelBlock) {
        primalBarrelBlock.setCustomName(pStack.getHoverName());
      }
    }
  }
}
