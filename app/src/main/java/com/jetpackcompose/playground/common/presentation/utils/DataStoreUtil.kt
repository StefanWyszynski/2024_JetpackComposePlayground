package com.jetpackcompose.playground.common.presentation.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.InvalidObjectException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Copyright 2024
 *
 * @author Stefan Wyszyński
 *
 * you can use it like this:
 *     val boolKey = booleanPreferencesKey("boolKey")
 *     val stringKey = stringPreferencesKey("stringKey")
 *
 *     dataStoreUtil.modify {
 *         boolKey.setValue(true)
 *         stringKey.setValue("dada")
 *         var stringRes = stringKey.getValue()
 *         var boolRes = boolKey.getValue()
 *     }
 */
@Singleton
class DataStoreUtil @Inject constructor(val dataStore: DataStore<Preferences>) {

    suspend fun <T> get(keyName: Preferences.Key<T>): T? {
        return dataStore.data.map {
            it[keyName]
        }.firstOrNull()
    }

    suspend fun <T> set(keyName: Preferences.Key<T>, value: T) {
        dataStore.edit { preference ->
            preference[keyName] = value
        }
    }

    suspend fun <T> Preferences.Key<T>.getValue(): T? {
        return get(this)
    }

    suspend fun <T> Preferences.Key<T>.setValue(value: T) {
        set(this, value)
    }

    fun observe(): Flow<Preferences> {
        return dataStore.data
    }

    suspend fun modify(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        block: suspend DataStoreUtil.() -> (Unit)
    ) {
        withContext(dispatcher) {
            block()
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }
}

@Suppress("UNCHECKED_CAST")
class DataStoreUtilProperty<T>(
    var dataStoreUtil: DataStoreUtil,
    val name: String,
    val defaultValue: T,
    val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) where T : Any {
    var value = when (defaultValue) {
        is Int -> intPreferencesKey(name)
        is Long -> longPreferencesKey(name)
        is Float -> floatPreferencesKey(name)
        is Double -> doublePreferencesKey(name)
        is String -> stringPreferencesKey(name)
        is Boolean -> booleanPreferencesKey(name)
        else -> throw InvalidObjectException("Invalid")
    }

    suspend fun getValue(valueCallback: (T) -> Unit) {
        withContext(coroutineDispatcher) {
            val t = dataStoreUtil.get(value) ?: defaultValue
            withContext(Dispatchers.Main) {
                valueCallback(t as T)
            }
        }
    }

    suspend fun setValue(v: T) {
        withContext(coroutineDispatcher) {
            dataStoreUtil.modify {
                (value as Preferences.Key<T>).setValue(v)
            }
        }
    }
}