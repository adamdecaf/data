package data

package object common {
  def UUID(): String = java.util.UUID.randomUUID.toString
}
