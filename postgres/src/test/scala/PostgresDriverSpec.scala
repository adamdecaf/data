package data.postgres
import org.specs2.specification.Scope
import org.specs2.mutable.Specification
import org.squeryl.annotations.Column
import org.squeryl.{Table, Schema, KeyedEntity, PrimitiveTypeMode}

object PostgresDriverSpec extends Specification {
  sequential

  "create test_table" in new context {
    import TestTablesSchema._
    driver.withSession {
      Migration.migrate()
    }
    ok
  }

  "clear out the table" in new context {
    import TestTablesSchema._
    driver.withSession {
      TestTablesSchema.Tables.test_table.deleteWhere(_.id === "1")
    }
  }

  "find us a connection to use" in new context {
    import TestTablesSchema._
    driver.withSession {
      val findNothing =
        from(TestTablesSchema.Tables.test_table)(t =>
          where(t.id === "1")
          select(t)).toList

      findNothing must beEmpty
    }
  }

  "insert and read out some data" in new context {
    import TestTablesSchema._
    driver.withSession {
      TestTablesSchema.Tables.test_table.insert(TestTable("1", "something"))

      val query =
        from(TestTablesSchema.Tables.test_table)(t =>
          where(t.id === "1")
          select(t)).toList

      query must contain(exactly(TestTable("1", "something")))
    }
  }

  case class TestTable(
    @Column("row_id") id: String,
    data: String
  ) extends KeyedEntity[String]

  object TestTablesSchema extends PrimitiveTypeMode {
    object Tables extends Schema {
      lazy val test_table = table[TestTable]("test_table")
    }
  }

  val Migration = new SchemaMigration {
    protected lazy val migrationFolder = "test-migrations"
    def migrate(): Unit = {
      run("001_base_schema.sql")
    }
  }

  trait context extends Scope {
    lazy val driver = new PostgresDriver{}
  }
}
