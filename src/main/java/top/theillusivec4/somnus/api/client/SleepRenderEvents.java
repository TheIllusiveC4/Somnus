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

package top.theillusivec4.somnus.api.client;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.AbstractClientPlayerEntity;

/**
 * Provides render events related to sleeping.
 */
public class SleepRenderEvents {

  /**
   * Called when a {@link net.minecraft.client.network.OtherClientPlayerEntity} is rendered while
   * sleeping. This is used to properly translate vertical offsets for players being rendered who
   * are not the client's own player.
   */
  public static final Event<PlayerVerticalTranslation> PLAYER_VERTICAL_TRANSLATION =
      EventFactory.createArrayBacked(PlayerVerticalTranslation.class, (listeners) -> (player) -> {

        for (PlayerVerticalTranslation listener : listeners) {
          float translation = listener.getTranslation(player);

          if (translation != 0.0F) {
            return translation;
          }
        }
        return 0.0F;
      });

  @FunctionalInterface
  public interface PlayerVerticalTranslation {

    /**
     * @param player The sleeping player
     * @return The vertical translation to apply the sleeping pose being rendered to the player
     */
    float getTranslation(AbstractClientPlayerEntity player);
  }
}
