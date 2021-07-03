package uk.co.dawg.gnss.collector.di

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class OverrideLocationPrefs(val value: String = "")