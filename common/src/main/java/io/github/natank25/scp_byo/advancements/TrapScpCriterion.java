package io.github.natank25.scp_byo.advancements;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class TrapScpCriterion extends AbstractCriterion<TrapScpCriterion.Conditions> {

	@Override
	public Codec<Conditions> getConditionsCodec() {
		return Conditions.CODEC;
	}

	public void trigger(ServerPlayerEntity player) {
		trigger(player, Conditions::requirementsMet);
	}

	public record Conditions(Optional<LootContextPredicate> playerPredicate) implements AbstractCriterion.Conditions {

		public static Codec<Conditions> CODEC = LootContextPredicate.CODEC.optionalFieldOf("player")
				.xmap(Conditions::new, Conditions::player).codec();

		@Override
		public Optional<LootContextPredicate> player() {
			return playerPredicate;
		}

		public boolean requirementsMet() {
			return true; // AbstractCriterion#trigger helpfully checks the playerPredicate for us.
		}
	}
}
