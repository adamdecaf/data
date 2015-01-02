package data.crawler
import data.http.HttpStorageMigrations

trait PostgresMigrations {
  def runMigrations(): Unit = {
    println("Running Migrations")
    HttpStorageMigrations.migrate()
  }
}
