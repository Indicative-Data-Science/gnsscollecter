package uk.co.dawg.gnss.collector.domain

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class HasSeenOnboardingUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun get(): Boolean {
        return sharedPreferences.getBoolean(HAS_SEEN_ONBOARDING, false)
    }

    fun set(hasSeenOnboarding: Boolean) {
        sharedPreferences.edit {
            putBoolean(HAS_SEEN_ONBOARDING, hasSeenOnboarding)
        }
    }

    companion object {
        const val HAS_SEEN_ONBOARDING = "HAS_SEEN_ONBOARDING"
    }
}