import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.blaze._
import org.http4s.client.blaze._
import org.http4s.circe._
import io.circe.syntax._
import org.jsoup.Jsoup

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {
  implicit val decoder: EntityDecoder[IO, Seq[Uri]] = jsonOf[IO, Seq[Uri]]

  def getTitle(uri: Uri): IO[(String, String)] = {
    BlazeClientBuilder[IO](global)
      .resource
      .use(_.expect[String](uri).map(html => Jsoup.parse(html).title()))
      .handleErrorWith(_ => IO.pure("Не получилось найти title"))
      .map(title => uri.renderString -> title)
  }

  val routes = HttpRoutes.of[IO] {
    case req @ POST -> Root / "crawler" => for {
      urls <- req.as[Seq[Uri]]
      titles <- urls.map(getTitle).parSequence
      resp <- Ok(titles.toMap.asJson)
    } yield resp
  }

  override def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder[IO](global)
      .bindHttp(8080, "localhost")
      .withHttpApp(routes.orNotFound)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
