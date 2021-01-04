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

package top.theillusivec4.somnus;

import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import top.theillusivec4.somnus.api.PlayerSleepEvents;
import top.theillusivec4.somnus.api.WorldSleepEvents;

public class MixinHooks {

  public static boolean canSleepNow(PlayerEntity player) {
    return canSleepNow(player, player.getSleepingPosition().orElse(player.getBlockPos()));
  }

  public static boolean canSleepNow(PlayerEntity player, BlockPos pos) {
    TriState state = PlayerSleepEvents.CAN_SLEEP_NOW.invoker().canSleepNow(player, pos);
    return state == TriState.DEFAULT ? !player.world.isDay() : state.get();
  }

  public static PlayerEntity.SleepFailureReason trySleep(ServerPlayerEntity player, BlockPos pos) {
    return PlayerSleepEvents.TRY_SLEEP.invoker().trySleep(player, pos);
  }

  public static long getWorldWakeTime(ServerWorld world, long newTime, long minTime) {
    return WorldSleepEvents.GET_WORLD_WAKE_TIME.invoker().getWorldWakeTime(world, newTime, minTime);
  }
}
