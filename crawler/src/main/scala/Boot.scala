package data.crawler

object Boot extends App
  with PostgresMigrations {

  println("Crawler Started")
  runMigrations()

}
