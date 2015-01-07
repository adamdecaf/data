package data.parsing
import org.parboiled2._

// object URIParser {
//   def apply: new URIParser
// }

class URIParser(val input: ParserInput) extends Parser {
  case class Path(segments: Seq[String]) {
    override def toString: String = // todo: encode?
      if (segments.isEmpty) "" else segments.mkString("/","/","")
  }

  case class Query(pairs: Seq[Pair[String, Option[String]]]) {
    override def toString: String =
      if (pairs.isEmpty) "" else {
        pairs.map { case (k, v) => v.fold(k)(k + "=" + _) } mkString("?", "&", "")
      }
  }

  case class Uri(
    scheme: String,
    userInfo: String,
    host: String,
    port: Int,
    path: Path,
    query: Query,
    fragment: String
  ) {
    override def toString: String = {
      val userInfoString: String = if (userInfo.nonEmpty) "@" else ""
      scheme + userInfo + userInfoString + host + port + path.toString + query.toString + fragment
    }
  }

  object AllowedSchemes {
    final val schemes = Set("ftp", "ssh", "http", "https", "mailto")
    def contains(str: String): Boolean = schemes.contains(str)
  }

  // parsing logic

  object Token {
    import CharPredicate.{Alpha, Digit, Digit19, HexDigit}

    // root rule
    // def URI = rule { Whitespace ~ Value ~ EOI }

    // pct-encoded = "%" HEXDIG HEXDIG
    def PCT_ENCODED = rule { "%" ~ HexDigit ~ HexDigit }

    def GEN_DELIMS = rule { ":" | "/" | "?" | "#" | "[" | "]" | "@" }
    def SUB_DELIMS = rule { "!" | "$" | "&" | "'" | "(" | ")" | "*" | "+" | "," | ";" | "=" }
    def RESERVED = rule { GEN_DELIMS | SUB_DELIMS }
    def UNRESERVED = rule { Alpha | Digit | "-" | "." | "_" | "~" }

    // URI = scheme ":" hier-part [ "?" query ] [ "#" fragment ]
    // hier-part = "//" authority path-abempty / path-absolute / path-rootless / path-empty

    // scheme = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
    def SCHEME = rule { capture(Alpha ~ oneOrMore(Alpha | Digit | "+" | "-" | ".")) ~> (AllowedSchemes.contains(_)) }

    // authority = [ userinfo "@" ] host [ ":" port ]
    // userinfo = *( unreserved / pct-encoded / sub-delims / ":" )
    def AUTHORITY = rule { zeroOrMore(UNRESERVED | PCT_ENCODED | SUB_DELIMS) }

    // host = IP-literal / IPv4address / reg-name
    def HOST = rule { IPV4ADDRESS | REG_NAME } // todo: add ipv6 support
    def IPV4ADDRESS = rule { (4).times(DEC_OCTET ~ ".") }
    def DEC_OCTET = rule { capture((1 to 3).times(Digit)) ~> (_.toInt <= 255) }
    def REG_NAME = rule { zeroOrMore(UNRESERVED | PCT_ENCODED | SUB_DELIMS) }

    def PORT = rule { capture(zeroOrMore(Digit)) ~> (_.toInt < 65535) }

    def PATH = rule { PATH_ABEMPTY | PATH_ABSOLUTE | PATH_NOSCHEME | PATH_ROOTLESS | PATH_EMPTY }
    def PATH_ABEMPTY = rule { zeroOrMore("/" ~ SEGMENT) }
    def PATH_ABSOLUTE = rule { "/" ~ }
    def PATH_NOSCHEME = rule {}
    def PATH_ROOTLESS = rule {}
    def PATH_EMPTY = {}
  }
}
