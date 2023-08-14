package com.im1294.primalbarrels.container;

import com.im1294.primalbarrels.PrimalBarrels;
import com.im1294.primalbarrels.tile.PrimalBarrelBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class PrimalBarrelContainer extends AbstractContainerMenu {

  protected Player playerEntity;
	private final ContainerLevelAccess callable;
	public int width;
  public int height;

  public PrimalBarrelContainer(MenuType<?> containerType,
															int id, Inventory playerInventory,
															int width, int height, int containerX, int containerY, int playerX, int playerY,ContainerLevelAccess callable) {
    super(containerType, id);
    this.width = width;
    this.height = height;
		this.playerEntity = playerInventory.player;
		this.callable = callable;

		ItemStackHandler stackHandler = callable.evaluate(Level::getBlockEntity).map(PrimalBarrelBlockEntity.class::cast)
						.map(primalBarrelBlockEntity -> primalBarrelBlockEntity.handler)
						.orElse(new ItemStackHandler(width * height));

		for (int i = 0; i < height; i++) {
			if (width == 7) {
	  for (int j = 0; j < width; j++)
        addSlot(new SlotItemHandler(stackHandler,
                j + width * i, containerX + (j+1) * 18, containerY + i * 18));
			}
			else {
      for (int j = 0; j < width; j++)
        addSlot(new SlotItemHandler(stackHandler,
                j + width * i, containerX + j * 18, containerY + i * 18));
			}}

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        this.addSlot(new Slot(playerInventory, j + i * 9 + 9, j * 18 + playerX, i * 18 + playerY));
      }
    }

    for (int i = 0; i < 9; i++) {
      this.addSlot(new Slot(playerInventory, i, i * 18 + playerX, playerY + 58));
    }
  }

	public static PrimalBarrelContainer primal(int id, Inventory playerInventory) {
		return primalS(id,playerInventory, ContainerLevelAccess.NULL);
	}

	public static PrimalBarrelContainer advanced_primal(int id, Inventory playerInventory) {
		return advanced_primalS(id,playerInventory, ContainerLevelAccess.NULL);
	}

	public static PrimalBarrelContainer developed_primal(int id, Inventory playerInventory) {
		return developed_primalS(id,playerInventory, ContainerLevelAccess.NULL);
	}

	//////////////////////////

	public static PrimalBarrelContainer primalS(int id, Inventory playerInventory,ContainerLevelAccess callable) {
		return new PrimalBarrelContainer(PrimalBarrels.ObjectHolders.PRIMAL_CONTAINER,id, playerInventory,
						9,1,8,18, 8, 50,callable);
	}
	public static PrimalBarrelContainer advanced_primalS(int id, Inventory playerInventory,ContainerLevelAccess callable) {
		return new PrimalBarrelContainer(PrimalBarrels.ObjectHolders.ADVANCED_PRIMAL_CONTAINER,id, playerInventory,
						7,2,8,18, 8, 68,callable);
	}
	public static PrimalBarrelContainer developed_primalS(int id, Inventory playerInventory,ContainerLevelAccess callable) {
		return new PrimalBarrelContainer(PrimalBarrels.ObjectHolders.DEVELOPED_PRIMAL_CONTAINER,id, playerInventory,
						9,2,8,18, 8, 68,callable);
	}

  @Override
  public boolean stillValid(@Nonnull Player playerIn) {
    return true;
  }

  @Nonnull
  @Override
  public ItemStack quickMoveStack(Player playerIn, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.slots.get(index);
    if (slot != null && slot.hasItem()) {
      ItemStack itemstack1 = slot.getItem();
      itemstack = itemstack1.copy();
      if (index < this.height * this.width) {
        if (!this.moveItemStackTo(itemstack1, this.height * this.width, this.slots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.moveItemStackTo(itemstack1, 0, this.height * this.width, false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) {
        slot.set(ItemStack.EMPTY);
      } else {
        slot.setChanged();
      }
    }
    return itemstack;
  }

  /**
   * Called when the container is closed.
   */
  public void removed(Player playerIn) {
  	super.removed(playerIn);
		this.callable.execute((world, pos) -> {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity == null) {
				PrimalBarrels.logger.warn("unexpected null on container close");
				return;
			}
			PrimalBarrelBlockEntity primalBarrelBlockEntity = (PrimalBarrelBlockEntity) tileEntity;
			if (!playerIn.isSpectator()) {
				--primalBarrelBlockEntity.players;
			}
			if (primalBarrelBlockEntity.players <= 0) {
				primalBarrelBlockEntity.soundStuff(tileEntity.getBlockState(), SoundEvents.BARREL_CLOSE);
				primalBarrelBlockEntity.changeState(playerIn.level.getBlockState(tileEntity.getBlockPos()), false);
			}
		});
	}
}

