package io.github.natank25.scp_byo.block.entity;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.natank25.scp_byo.Scp_byo;
import io.github.natank25.scp_byo.block.ModBlocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public enum ModBlocksEntities {
	;
	
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Scp_byo.MOD_ID, RegistryKeys.BLOCK_ENTITY_TYPE);
	
	public static void registerAllBlockEntity() {
		BLOCK_ENTITIES.register();
	}
	
	public static <T extends BlockEntity> RegistrySupplier<BlockEntityType<T>> registerBlockEntity(String name, Supplier<BlockEntityType<T>> blockEntity) {
		return BLOCK_ENTITIES.register(new Identifier(Scp_byo.MOD_ID, name), blockEntity);
	}
	
	public static final RegistrySupplier<BlockEntityType<SlidingDoorBlockEntity>> SLIDING_DOOR_BLOCK_ENTITY = registerBlockEntity("sliding_door_entity", () -> BlockEntityType.Builder.create(SlidingDoorBlockEntity::new, ModBlocks.SLIDING_DOOR.get()).build(null));
	public static final RegistrySupplier<BlockEntityType<KeycardReaderBlockEntity>> KEYCARD_READER_BLOCK_ENTITY = registerBlockEntity("keycard_reader_entity", () -> BlockEntityType.Builder.create(KeycardReaderBlockEntity::new, ModBlocks.KEYCARD_READER.get()).build(null));
	
}
