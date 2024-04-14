package io.github.natank25.scp_byo.data;

import io.github.natank25.scp_byo.advancements.TrapScpCriterion;
import io.github.natank25.scp_byo.entity.ModEntities;
import io.github.natank25.scp_byo.item.ModItems;
import io.github.natank25.scp_byo.scp_byo;
import io.github.natank25.scp_byo.utils.Utils;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModAdvancementsProvider extends FabricAdvancementProvider {
	public ModAdvancementsProvider(FabricDataOutput output) {
		super(output);
	}
	
	private static final Identifier BACKGROUND = Utils.newIdentifier("textures/gui/advancements/background.png");
	
	@Override
	public void generateAdvancement(Consumer<Advancement> consumer) {
		Advancement encounterFirstScp = Advancement.Builder.create()
				.display(
						ModItems.SCP_096_SPAWN_EGG,
						Text.literal("Your first SCP"),
						Text.literal("Encounter your first scp"),
						BACKGROUND,
						AdvancementFrame.GOAL,
						true,
						true,
						true
				)
				.criterion("1st_scp", new ImpossibleCriterion.Conditions())
				.build(consumer, createAdvId("see_1st_scp"));
		
		Advancement trap096 = Advancement.Builder.create()
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
				.criterion("trap_scp", new TrapScpCriterion.Conditions(ModEntities.SCP_096))
				.rewards(AdvancementRewards.Builder.experience(50))
				.build(consumer, createAdvId("trap_096"));
	}
	
	private static String createAdvId(String advId){
		return scp_byo.MOD_ID+"/"+advId;
	}
	
	/*
	List of advancements to do:
	- Secure, Contain, Protect: Discover an SCP Fondation in the wild
	
	
	 */
}
