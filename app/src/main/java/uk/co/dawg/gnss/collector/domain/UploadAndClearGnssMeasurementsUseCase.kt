package uk.co.dawg.gnss.collector.domain

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import uk.co.dawg.gnss.collector.data.storage.GnssDao
import uk.co.dawg.gnss.collector.di.FirestoreDB
import java.util.*
import javax.inject.Inject

class UploadAndClearGnssMeasurementsUseCase @Inject constructor(
    @FirestoreDB(FirestoreDB.Type.MEASUREMENTS)
    private val gnssFirestore: CollectionReference,
    private val fireStore: FirebaseFirestore,
    private val gnssDao: GnssDao
) {

    suspend fun run() = withContext(Dispatchers.IO) {
        try {
            val measures = gnssDao.getAll()
            gnssDao.deleteAll()

            val docRef = gnssFirestore.document(UUID.randomUUID().toString())

            fireStore.runBatch { batch ->
                batch.set(docRef, mapOf("data" to measures))
            }.await()

        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}