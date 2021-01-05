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

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import top.theillusivec4.somnus.api.client.SleepRenderEvents;

public class ClientMixinHooks {

  public static void sleepingTranslate(AbstractClientPlayerEntity player, MatrixStack matrixStack) {

    if (player instanceof OtherClientPlayerEntity && player.getPose() == EntityPose.SLEEPING) {
      float translate = SleepRenderEvents.PLAYER_VERTICAL_TRANSLATION.invoker().getTranslation(player);

      if (translate != 0.0F) {
        matrixStack.translate(0.0F, translate, 0.0F);
      }
    }
  }

  public static void resetSleepingTranslate(AbstractClientPlayerEntity player,
                                            MatrixStack matrixStack) {

    if (player instanceof OtherClientPlayerEntity && player.getPose() == EntityPose.SLEEPING) {
      float translate = SleepRenderEvents.PLAYER_VERTICAL_TRANSLATION.invoker().getTranslation(player);

      if (translate != 0.0F) {
        matrixStack.translate(0.0F, -translate, 0.0F);
      }
    }
  }
}
