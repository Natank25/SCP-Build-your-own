package io.github.natank25.scp_byo.block.entity;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.natank25.scp_byo.Scp_byo;
import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

public enum ModBlocksEntities {
	;
	
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Scp_byo.MOD_ID, RegistryKeys.BLOCK_ENTITY_TYPE);
	
	public static void registerAllBlockEntity() {
		BLOCK_ENTITIES.register();
	}
	
	public static <T extends BlockEntity> RegistrySupplier<BlockEntityType<T>> registerBlockEntity(String name, Supplier<BlockEntityType<T>> blockEntity) {
		return BLOCK_ENTITIES.register(Utils.newIdentifier(name), blockEntity);
	}

	@ExpectPlatform
	public static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityType.BlockEntityFactory<? extends T> factory, Block block) {
		// Just throw an error, the content should get replaced at runtime by Architectury API
		// Something is terribly wrong if this is not replaced.
		throw new AssertionError();
	}

	public static final RegistrySupplier<BlockEntityType<SlidingDoorBlockEntity>> SLIDING_DOOR_BLOCK_ENTITY = registerBlockEntity("sliding_door_entity", () -> createBlockEntityType(SlidingDoorBlockEntity::new, ModBlocks.SLIDING_DOOR.get()));
	public static final RegistrySupplier<BlockEntityType<KeycardReaderBlockEntity>> KEYCARD_READER_BLOCK_ENTITY = registerBlockEntity("keycard_reader_entity",() -> createBlockEntityType(KeycardReaderBlockEntity::new, ModBlocks.KEYCARD_READER.get()));

}
