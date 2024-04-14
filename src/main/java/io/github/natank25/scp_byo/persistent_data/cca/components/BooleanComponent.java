package io.github.natank25.scp_byo.persistent_data.cca.components;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;

public class BooleanComponent implements ComponentV3, AutoSyncedComponent {
	boolean value;
	
	public BooleanComponent(boolean defaultValue){
		this.value = defaultValue;
	}
	@Override
	public void readFromNbt(NbtCompound tag) {
		this.value = tag.getBoolean("boolean");
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("boolean", this.value);
	}
	
	public boolean getValue() {
		return this.value;
	}
	
	public void swap() {
		this.value = !this.value;
	}
	public void setValue(boolean newValue) {
		this.value = newValue;
	}
}
