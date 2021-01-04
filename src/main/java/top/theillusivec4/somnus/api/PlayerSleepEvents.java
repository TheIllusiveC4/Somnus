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

package top.theillusivec4.somnus.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Provides player events related to sleeping.
 */
public final class PlayerSleepEvents {

  /**
   * Called when trying to determine if the player can sleep at this time of day.
   *
   * <p>Called every tick in {@link PlayerEntity#tick()} while sleeping.</p>
   *
   * <p>Also called once when first attempting to sleep in {@link ServerPlayerEntity#trySleep(BlockPos)}.
   * This occurs after {@link PlayerSleepEvents#TRY_SLEEP}.</p>
   *
   * <p>{@link TriState#DEFAULT} delegates to an inverted {@link World#isDay()} check.</p>
   */
  public static final Event<CanSleepNow> CAN_SLEEP_NOW =
      EventFactory.createArrayBacked(CanSleepNow.class, (listeners) -> (player, pos) -> {

        for (CanSleepNow listener : listeners) {
          TriState state = listener.canSleepNow(player, pos);

          if (state != TriState.DEFAULT) {
            return state;
          }
        }
        return TriState.of(!player.world.isDay());
      });

  /**
   * Called when a player attempts to sleep.
   *
   * <p>This occurs before any other sleeping logic or checks are called.</p>
   */
  public static final Event<TrySleep> TRY_SLEEP =
      EventFactory.createArrayBacked(TrySleep.class, (listeners) -> (player, pos) -> {

        for (TrySleep listener : listeners) {
          PlayerEntity.SleepFailureReason reason = listener.trySleep(player, pos);

          if (reason != null) {
            return reason;
          }
        }
        return null;
      });

  @FunctionalInterface
  public interface CanSleepNow {

    /**
     * @param player The sleeping player or player attempting to sleep
     * @param pos    The sleeping position of the player or the location of the sleep attempt
     * @return {@link TriState#DEFAULT} to delegate to another listener,
     * {@link TriState#TRUE} to allow sleeping, or
     * {@link TriState#FALSE} to prevent sleep attempts or interrupt current sleeping.
     */
    TriState canSleepNow(PlayerEntity player, BlockPos pos);
  }

  @FunctionalInterface
  public interface TrySleep {

    /**
     * @param player The player attempting to sleep
     * @param pos    The location of the sleep attempt
     * @return The reason that the sleeping attempt failed, or null to delegate to another listener.
     */
    PlayerEntity.SleepFailureReason trySleep(ServerPlayerEntity player, BlockPos pos);
  }
}
