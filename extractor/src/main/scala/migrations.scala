package data.extractor

trait PostgresMigrations {
  def runMigrations(): Unit = {
    println("Running Migrations")

  }
}
