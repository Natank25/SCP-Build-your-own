package io.github.natank25.scp_byo.fabric.data;

import io.github.natank25.scp_byo.Scp_byo;
import io.github.natank25.scp_byo.advancements.ModCriteria;
import io.github.natank25.scp_byo.advancements.TrapScpCriterion;
import io.github.natank25.scp_byo.entity.ModEntities;
import io.github.natank25.scp_byo.item.ModItems;
import io.github.natank25.scp_byo.utils.Utils;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.*;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementsProvider extends FabricAdvancementProvider {
	
	
	private static final Identifier BACKGROUND = Utils.newIdentifier("textures/gui/advancements/background.png");

	public ModAdvancementsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(output, registryLookup);
	}

	@Override
	public void generateAdvancement(RegistryWrapper.WrapperLookup wrapperLookup, Consumer<AdvancementEntry> consumer) {
		AdvancementEntry encounterFirstScp = Advancement.Builder.create()
				.display(
						ModItems.SCP_096_SPAWN_EGG.get(),
						Text.literal("Your first SCP"),
						Text.literal("Encounter your first scp"),
						BACKGROUND,
						AdvancementFrame.GOAL,
						true,
						true,
						true
				)
				.criterion("1st_scp", Criteria.IMPOSSIBLE.create(new ImpossibleCriterion.Conditions()))
				.build(consumer, createAdvId("see_1st_scp"));
		
		AdvancementEntry trap096 = Advancement.Builder.create()
				.parent(encounterFirstScp)
				.display(
						Blocks.IRON_BLOCK,
						Text.literal("Trap SCP 096"),
						Text.literal("Trap SCP 096 in his cage"),
						BACKGROUND,
						AdvancementFrame.CHALLENGE,
						true,
						true,
						true
				)
				.criterion("trap_scp", ModCriteria.TRAP_SCP.create(new TrapScpCriterion.Conditions(Optional.empty())))
				.rewards(AdvancementRewards.Builder.experience(50))
				.build(consumer, createAdvId("trap_096"));
	}
	
	private static String createAdvId(String advId){
		return Scp_byo.MOD_ID+"/"+advId;
	}

	@Override
	public CompletableFuture<?> run(DataWriter writer) {
		return null;
	}
	
	@Override
	public String getName() {
		return "";
	}
	
	/*
	List of advancements to do:
	- Secure, Contain, Protect: Discover an SCP Foundation in the wild
	
	
	 */
}
