package uk.co.dawg.gnss.collector.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides

@Module
class StorageModule {

    @Provides
    fun provideFireStore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @FirestoreDB(FirestoreDB.Type.MEASUREMENTS)
    fun provideGnssMeasurementsDB(storage: FirebaseFirestore): CollectionReference {
        return storage.collection("measurements")
    }

    @Provides
    @FirestoreDB(FirestoreDB.Type.ANTENNA)
    fun provideGnssAntennaDB(storage: FirebaseFirestore): CollectionReference {
        return storage.collection("antenna")
    }
}