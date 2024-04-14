package io.github.natank25.scp_byo.advancements;

import com.google.gson.JsonObject;
import io.github.natank25.scp_byo.entity.custom.ScpEntity;
import io.github.natank25.scp_byo.utils.Utils;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.EntityType;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class TrapScpCriterion extends AbstractCriterion<TrapScpCriterion.Conditions> {
	
	static final Identifier ID = Utils.newIdentifier("trap_scp");
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@Override
	protected TrapScpCriterion.Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		EntityType<? extends ScpEntity> scpType = (EntityType<? extends ScpEntity>) Registries.ENTITY_TYPE.get(new Identifier(obj.get("scp_type").getAsString()));
		return new Conditions(scpType);
	}
	
	public void trigger(ServerPlayerEntity player,EntityType<? extends ScpEntity> scpType){
		this.trigger(player, conditions -> conditions.requirementMet(scpType));
	}
	
	
	public static class Conditions extends AbstractCriterionConditions {
		EntityType<? extends ScpEntity> scpType;
		
		public Conditions(EntityType<? extends ScpEntity> scpType) {
			super(ID, EntityPredicate.Extended.EMPTY);
			
			this.scpType = scpType;
		}
		
		boolean requirementMet(EntityType<? extends ScpEntity> scpType){
			return this.scpType == scpType;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject json = super.toJson(predicateSerializer);
			json.addProperty("scp_type", Registries.ENTITY_TYPE.getId(this.scpType).toString());
			return json;
		}
	}
}
