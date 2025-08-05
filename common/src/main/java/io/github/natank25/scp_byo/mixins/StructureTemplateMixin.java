package io.github.natank25.scp_byo.mixins;

import net.minecraft.structure.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(StructureTemplate.class)
public interface StructureTemplateMixin {

    @Accessor
    List<StructureTemplate.PalettedBlockInfoList> getBlockInfoLists();
}
