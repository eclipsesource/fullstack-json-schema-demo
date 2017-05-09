package filters

import javax.inject.Inject

import play.api.mvc.{EssentialAction, EssentialFilter}
import play.filters.csrf.CSRFFilter

import scala.concurrent.ExecutionContext

// https://dominikdorn.com/2016/06/playframework-2-5-global-csrf-protection-disable-csrf-selectively/
class RouteCommentExcludingCSRFFilterFacade @Inject()(filter: CSRFFilter)(implicit ec: ExecutionContext) extends EssentialFilter {

  override def apply(nextFilter: EssentialAction): EssentialAction = new EssentialAction {

    import play.api.mvc._

    override def apply(rh: RequestHeader) = {
      if (rh.tags.getOrElse("ROUTE_COMMENTS", "").contains("NOCSRF")) {
        // this is required for GET/HEAD requests with no prior HTTP-Request (like bingbot)
        // so they are missing a context.
        // if the rendering template is using the CSRF-token to render a form, it would blow
        // up if we're not processing it through the CSRF filter
        val copy: RequestHeader = rh.copy(headers = rh.headers.add(("Csrf-Token", "my-secret-csrf-off-switch")))
        filter.apply(nextFilter)(copy)
      } else {
        filter.apply(nextFilter)(rh)
      }
    }
  }
}