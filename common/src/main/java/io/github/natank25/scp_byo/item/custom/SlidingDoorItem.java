package io.github.natank25.scp_byo.item.custom;

import io.github.natank25.scp_byo.block.ModBlocks;
import io.github.natank25.scp_byo.item.client.SlidingDoorItemRenderer;
import net.minecraft.item.BlockItem;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class SlidingDoorItem extends BlockItem implements GeoItem {
    
    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    public SlidingDoorItem(Settings settings) {
        super(ModBlocks.SLIDING_DOOR.get(), settings);
        GeoItem.registerSyncedAnimatable(this);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private SlidingDoorItemRenderer renderer;

            @Override
            public @NotNull GeoItemRenderer<?> getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new SlidingDoorItemRenderer();

                return this.renderer;
            }

        });

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>("controller", 0, SlidingDoorItem::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }
    
    private static <T extends GeoAnimatable> PlayState predicate(AnimationTest<T> tAnimationState) {
        return PlayState.CONTINUE;
    }
	
}
