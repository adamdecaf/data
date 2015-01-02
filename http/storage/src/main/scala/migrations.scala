package data.http
import data.postgres.SchemaMigration

object HttpStorageMigrations extends SchemaMigration {
  lazy val migrationFolder = "http-storage-migrations"
  def migrate(): Unit = {
    run("001_schema.sql")
  }
}
