/*
 * Copyright (C) 2005, The Beangle Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.beangle.sashub.service

import org.beangle.cdi.bind.BindModule
import org.beangle.sashub.service.config.DefaultArtifactVersionRefresher
import org.beangle.sashub.service.impl.{DefaultProfileService, MenuServiceImpl}
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler
import org.springframework.scheduling.config.{CronTask, ScheduledTaskRegistrar}

class DefaultModule extends BindModule {

  protected override def binding(): Unit = {
    bind(classOf[DefaultProfileService])
    bind(classOf[MenuServiceImpl])

    bind(classOf[ConcurrentTaskScheduler]).lazyInit(false)
    bind(classOf[ScheduledTaskRegistrar]).nowire("triggerTasks", "triggerTasksList").lazyInit(false)

    bind(classOf[DefaultArtifactVersionRefresher]).lazyInit(false)
    bindTask(classOf[DefaultArtifactVersionRefresher], "0 0 10,13,16,19,23 * * *")
  }

  protected def bindTask[T <: Runnable](clazz: Class[T], expression: String): Unit = {
    val taskName = clazz.getName
    bind(taskName + "Task", classOf[CronTask]).constructor(ref(taskName), expression).lazyInit(false)
  }
}
