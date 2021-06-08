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

import net.minecraft.block.BedBlock;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fix for vanilla bug where bed block doesn't check for null messages before sending
 */
@SuppressWarnings("unused")
@Mixin(BedBlock.class)
public class MixinBedBlock {

  @Inject(at = @At(value = "INVOKE", target = "net/minecraft/entity/player/PlayerEntity.sendMessage(Lnet/minecraft/text/Text;Z)V"), method = "method_19283(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/player/PlayerEntity$SleepFailureReason;)V", cancellable = true)
  private static void somnus$sendMessage(PlayerEntity unused,
                                         PlayerEntity.SleepFailureReason reason, CallbackInfo ci) {

    if (reason.toText() == null) {
      ci.cancel();
    }
  }
}
