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
 * @deprecated Use {@link net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents}
 * Provides player events related to sleeping.
 */
@Deprecated
public final class PlayerSleepEvents {

  /**
   * Called when trying to determine if the player can sleep at this time of day.
   *
   * <p>Called every tick in {@link PlayerEntity#tick()} while sleeping.</p>
   *
   * <p>Called once when attempting to sleep in {@link ServerPlayerEntity#trySleep(BlockPos)}.
   * This occurs after invoking {@link PlayerSleepEvents#TRY_SLEEP}.</p>
   *
   * <p>Invocations that receive a {@link TriState#DEFAULT} result should delegate to an inverted
   * {@link World#isDay()} check in order to match the expected result in the
   * {@link PlayerEntity#tick()} invocation.</p>
   *
   * <p>See: {@link PlayerSleepEvents#canSleepNow(PlayerEntity, BlockPos)}</p>
   *
   * @deprecated Use {@link net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents#ALLOW_SLEEP_TIME}
   */
  @Deprecated
  public static final Event<CanSleepNow> CAN_SLEEP_NOW =
      EventFactory.createArrayBacked(CanSleepNow.class, (listeners) -> (player, pos) -> {

        for (CanSleepNow listener : listeners) {
          TriState state = listener.canSleepNow(player, pos);

          if (state != TriState.DEFAULT) {
            return state;
          }
        }
        return TriState.DEFAULT;
      });

  /**
   * Helper method for invoking {@link PlayerSleepEvents#CAN_SLEEP_NOW}.
   * This delegates a {@link TriState#DEFAULT} result to an inverted {@link World#isDay()} check in
   * order to match the invocation result in {@link PlayerEntity#tick()}.
   *
   * <p>This method should be preferred over directly invoking the event.</p>
   *
   * @param player The sleeping player or player attempting to sleep
   * @param pos    The sleeping position of the player or the location of the sleep attempt
   * @return True to allow sleeping, false to prevent sleep attempts or interrupt current sleeping
   * @deprecated See {@link net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents#ALLOW_SLEEP_TIME}
   */
  @Deprecated
  public static boolean canSleepNow(PlayerEntity player, BlockPos pos) {
    TriState state = PlayerSleepEvents.CAN_SLEEP_NOW.invoker().canSleepNow(player, pos);
    return state == TriState.DEFAULT ? !player.world.isDay() : state.get();
  }

  /**
   * Called when a player attempts to sleep.
   *
   * <p>Called once when attempting to sleep in {@link ServerPlayerEntity#trySleep(BlockPos)}.
   * This occurs before any other sleeping logic or checks are called.</p>
   *
   * @deprecated Use {@link net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents#ALLOW_SLEEPING}
   */
  @Deprecated
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

  /**
   * Called when the player wakes up.
   * This is only for listening, nothing can be changed about the result.
   *
   * <p>Called once in {@link PlayerEntity#wakeUp(boolean, boolean)}.</p>
   *
   * @deprecated Use {@link net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents#STOP_SLEEPING}
   */
  public static final Event<WakeUp> WAKE_UP =
      EventFactory.createArrayBacked(WakeUp.class, (listeners) -> (player, reset, update) -> {

        for (WakeUp listener : listeners) {
          listener.wakeUp(player, reset, update);
        }
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

  @FunctionalInterface
  public interface WakeUp {

    /**
     * @param player                The player waking up
     * @param resetSleepTimer       If the sleep timer should be reset for the player
     * @param updateSleepingPlayers If the list of sleeping players should be updated
     */
    void wakeUp(PlayerEntity player, boolean resetSleepTimer, boolean updateSleepingPlayers);
  }
}
