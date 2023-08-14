package com.im1294.primalbarrels.util;

import com.mojang.datafixers.types.Type;
import com.im1294.primalbarrels.PrimalBarrels;
import com.im1294.primalbarrels.tile.PrimalBarrelBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.models.blockstates.PropertyDispatch.TriFunction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.inventory.ContainerLevelAccess;

import java.util.Set;
import java.util.function.Supplier;

public class PrimalBarrelBlockEntityType<T extends BlockEntity> extends BlockEntityType<T> {

	public final int width;
	public final int height;
	public final TriFunction<Integer,  Inventory, ContainerLevelAccess,AbstractContainerMenu> containerFactory;

	public PrimalBarrelBlockEntityType(BlockEntityType.BlockEntitySupplier<T> factoryIn, Set<Block> validBlocksIn, Type dataFixerType, int width, int height,
																		TriFunction<Integer, Inventory, ContainerLevelAccess, AbstractContainerMenu> containerFactory) {
		super(factoryIn, validBlocksIn, dataFixerType);
		this.width = width;
		this.height = height;
		this.containerFactory = containerFactory;
	}

	public static BlockEntityType.BlockEntitySupplier<BlockEntity> primal() {
		return ((pPos, pState) -> new PrimalBarrelBlockEntity(PrimalBarrels.ObjectHolders.PRIMAL_TILE,pPos,pState));
	}
	public static BlockEntityType.BlockEntitySupplier<BlockEntity> advanced_primal() {
		return ((pPos, pState) -> new PrimalBarrelBlockEntity(PrimalBarrels.ObjectHolders.ADVANCED_PRIMAL_TILE,pPos,pState));
	}
	public static BlockEntityType.BlockEntitySupplier<BlockEntity> developed_primal() {
		return ((pPos, pState) -> new PrimalBarrelBlockEntity(PrimalBarrels.ObjectHolders.DEVELOPED_PRIMAL_TILE,pPos,pState));
	}
}
