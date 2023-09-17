package pt.ulusofona.deisi.cm2223.g22004883_22007130.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [MovieRoom::class], version = 1, exportSchema = false)
@TypeConverters(PhotosTypeConverter::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        private var instance: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase {
            synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, MovieDatabase::class.java, "movie_db")
                        .fallbackToDestructiveMigration().build()
                }
                return instance as MovieDatabase
            }
        }
    }

}