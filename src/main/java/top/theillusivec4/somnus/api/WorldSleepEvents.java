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

import java.util.function.BooleanSupplier;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.world.ServerWorld;

/**
 * Provides world events related to sleeping.
 */
public class WorldSleepEvents {

  /**
   * Called when players finish sleeping and the world needs to update the time of day.
   *
   * <p>Called once in {@link ServerWorld#tick(BooleanSupplier)}</p>
   */
  public static final Event<GetWorldWakeTime> GET_WORLD_WAKE_TIME = EventFactory
      .createArrayBacked(GetWorldWakeTime.class, (listeners) -> (serverWorld, newTime, curTime) -> {
        long time = newTime;

        for (GetWorldWakeTime listener : listeners) {
          time = listener.getWorldWakeTime(serverWorld, time, curTime);
        }
        return time;
      });

  @FunctionalInterface
  public interface GetWorldWakeTime {

    /**
     * @param world   The server world
     * @param newTime The new time of day for the world
     * @param curTime The current time of day for the world
     * @return The new time of day for the world, overriding the previous new time
     */
    long getWorldWakeTime(ServerWorld world, long newTime, long curTime);
  }
}
