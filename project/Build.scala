import sbt._

object env {
  val target: String = sys.props.getOrElse("env", "dev") match {
    case str =>
      str match {
        case "dev" => str
        case "test" => str
        case "qa" => str
        case "prod" => str
        case _ => "dev"
      }
  }
}

