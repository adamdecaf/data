package data.extractor

object Boot extends App
  with PostgresMigrations {

  println("Extractor Started")
  runMigrations()

}
