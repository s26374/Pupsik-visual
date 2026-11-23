package com.example.pvpvisuals.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    @Inject(
        method = "renderFirstPersonItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
        )
    )
    private void onRenderBow(
            AbstractClientPlayerEntity player,
            float tickDelta,
            float pitch,
            Hand hand,
            float swingProgress,
            ItemStack item,
            float equipProgress,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            CallbackInfo ci
    ) {
        if (item.getItem() instanceof BowItem && player.isUsingItem() && player.getActiveHand() == hand) {
            int useTicks = item.getMaxUseTime(player) - player.getItemUseTimeLeft();
            float pullProgress = MathHelper.clamp((float) useTicks / 1.0f, 0.0f, 1.0f);

            int side = (hand == Hand.MAIN_HAND ? 1 : -1) * (player.getMainArm() == Arm.RIGHT ? 1 : -1);
            matrices.translate(side * 0.05f * pullProgress, -0.22f * pullProgress, 0.0f);
            float scale = 1.0f - (0.15f * pullProgress);
            matrices.scale(scale, scale, scale);
        }
    }
      }
