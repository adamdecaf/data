package data.crawler
import data.postgres.SchemaMigration

trait PostgresMigrations {
  def runMigrations(): Unit = {
    println("Running Migrations")
    HttpStorageMigrations.migrate()
  }

  private object HttpStorageMigrations extends SchemaMigration {
    lazy val migrationFolder = "http-storage-migrations"
    def migrate(): Unit = {
      run("001_schema.sql")
    }
  }
}
