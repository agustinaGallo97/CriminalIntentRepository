import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bignerdranch.android.criminalintent.CriminalIntentApplication
import com.bignerdranch.android.criminalintent.database.CrimeDatabase

object DatabaseProvider {
  private const val DATABASE_NAME = "crime-database"
  private val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) =
      database.execSQL("ALTER TABLE Crime ADD COLUMN suspect TEXT NOT NULL DEFAULT ''")
  }

  val database = Room
    .databaseBuilder(
      CriminalIntentApplication.context,
      CrimeDatabase::class.java,
      DATABASE_NAME
    )
    .addMigrations(
      migration_1_2
    ).build()
}
