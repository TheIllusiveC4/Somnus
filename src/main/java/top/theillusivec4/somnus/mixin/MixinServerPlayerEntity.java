/*
 * Copyright (c) 2021 C4
 *
 * This file is part of Somnus, a mod made for Minecraft.
 *
 * Somnus is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * Somnus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Somnus.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.somnus.mixin;

import com.mojang.datafixers.util.Either;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.somnus.MixinHooks;

@SuppressWarnings({"unused", "ConstantConditions"})
@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity {

  @Redirect(at = @At(value = "INVOKE", target = "net/minecraft/world/World.isDay()Z"), method = "trySleep")
  public boolean somnus$isDay(World world, BlockPos pos) {
    return !MixinHooks.canSleepNow((PlayerEntity) (Object) this, pos);
  }

  @Inject(at = @At("HEAD"), method = "trySleep", cancellable = true)
  public void somnus$trySleep(BlockPos pos,
                               CallbackInfoReturnable<Either<PlayerEntity.SleepFailureReason, Unit>> ci) {
    PlayerEntity.SleepFailureReason reason =
        MixinHooks.trySleep((ServerPlayerEntity) (Object) this, pos);

    if (reason != null) {
      ci.setReturnValue(Either.left(reason));
    }
  }
}
