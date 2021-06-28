package uk.co.dawg.gnss.collector.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface GnssDao {

    @Query("SELECT * FROM gnss_data")
    suspend fun getAll(): List<GnssEntity>

    @Insert
    fun insertAll(vararg gnss: GnssEntity)

    @Query("DELETE FROM gnss_data")
    fun deleteAll()
}