package io.github.natank25.scp_byo.fabric.item.custom;

import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.fabric.item.client.SlidingDoorItemRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.BlockItem;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.github.natank25.scp_byo.item.ModItems.defaultSettings;

public class SlidingDoorItem extends BlockItem implements GeoItem {
    
    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);
    
    public SlidingDoorItem() {
        super(ModBlocks.SLIDING_DOOR.get(), defaultSettings());
        //SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }
    
    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private SlidingDoorItemRenderer renderer;
            
            
            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new SlidingDoorItemRenderer();
                
                return this.renderer;
            }
        });
    
    }
    
    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }
    
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, SlidingDoorItem::predicate));
        
    }
    
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }
    
    private static <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        return PlayState.CONTINUE;
    }
	
}
