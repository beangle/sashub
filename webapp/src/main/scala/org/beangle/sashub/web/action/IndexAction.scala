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

package org.beangle.sashub.web.action

import org.beangle.ems.app.web.NavContext
import org.beangle.security.Securities
import org.beangle.security.realm.cas.{Cas, CasConfig}
import org.beangle.security.session.cache.CacheSessionRepo
import org.beangle.webmvc.annotation.action
import org.beangle.webmvc.context.ActionContext
import org.beangle.webmvc.support.{ActionSupport, ServletSupport}
import org.beangle.webmvc.view.View

@action("")
class IndexAction extends ActionSupport with ServletSupport {

  var casConfig: CasConfig = _
  var sessionRepo: CacheSessionRepo = _

  def index(): View = {
    put("nav", NavContext.get(request))
    put("locale",ActionContext.current.locale)
    forward()
  }

  def logout(): View = {
    Securities.session foreach { s =>
      sessionRepo.evict(s.id)
    }
    redirect(to(Cas.cleanup(casConfig, ActionContext.current.request, ActionContext.current.response)), null)
  }
}
