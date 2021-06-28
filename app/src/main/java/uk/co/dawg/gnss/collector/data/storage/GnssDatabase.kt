package uk.co.dawg.gnss.collector.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GnssEntity::class], version = 2, exportSchema = false)
abstract class GnssDatabase : RoomDatabase() {

    abstract fun gnssDao(): GnssDao
}