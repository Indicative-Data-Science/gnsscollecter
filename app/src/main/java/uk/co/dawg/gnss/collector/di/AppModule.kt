package uk.co.dawg.gnss.collector.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import uk.co.dawg.gnss.collector.data.storage.GnssDao
import uk.co.dawg.gnss.collector.data.storage.GnssDatabase
import javax.inject.Singleton

@Module
object AppModule {

    @Provides
    fun provide(context: Context): SharedPreferences {
        return context.getSharedPreferences("gnss_general_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): GnssDatabase {
        return Room.databaseBuilder(
            context,
            GnssDatabase::class.java,
            "gnss-database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideGnssDao(database: GnssDatabase): GnssDao {
        return database.gnssDao()
    }

    @Provides
    fun provideFusedLocation(context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }
}