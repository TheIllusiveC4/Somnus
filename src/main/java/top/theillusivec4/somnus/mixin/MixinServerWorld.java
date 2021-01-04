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

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.theillusivec4.somnus.MixinHooks;

@Mixin(ServerWorld.class)
public class MixinServerWorld {

  @Redirect(at = @At(value = "INVOKE", target = "net/minecraft/server/world/ServerWorld.setTimeOfDay(J)V"), method = "tick")
  public void _somnus_getWorldWakeTime(ServerWorld world, long time) {
    world.setTimeOfDay(MixinHooks.getWorldWakeTime(world, time, world.getTimeOfDay()));
  }
}
