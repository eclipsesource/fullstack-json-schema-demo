package filters

import javax.inject.Inject

import play.api.http.HttpFilters
import filters.RouteCommentExcludingCSRFFilterFacade
import play.filters.gzip.GzipFilter

class Filters @Inject()(
                         routeCommentExcludingCSRFFilterFacade: RouteCommentExcludingCSRFFilterFacade,
                         gzipFilter: GzipFilter
                       ) extends HttpFilters {

  val _filters = Seq(
    gzipFilter,
    routeCommentExcludingCSRFFilterFacade
  )

  override def filters = _filters
}