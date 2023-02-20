/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codelab.android.datastore.data

import android.content.Context
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.*
import java.io.IOException

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val SORT_ORDER_KEY = "sort_order"

enum class SortOrder {
    NONE,
    BY_DEADLINE,
    BY_PRIORITY,
    BY_DEADLINE_AND_PRIORITY
}

/** App.prefs.getString(Contant.auto_login) **/

private object PreferencesKeys {
    val SORT_ORDER = stringPreferencesKey("sort_order")
    val SHOW_COMPLETED = booleanPreferencesKey("show_completed")
}

data class UserPreferences(
    val showCompleted: Boolean,
    val sortOrder: SortOrder
)

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>, context: Context) {
    private val sharedPreferences = context.applicationContext.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)

    /** 읽기 **/
    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if(exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val showCompleted = preferences[PreferencesKeys.SHOW_COMPLETED] ?: false
            val sortOrder = SortOrder.valueOf(preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name)
            UserPreferences(showCompleted, sortOrder)
        }

    /** 쓰기 **/
    suspend fun enableSortByDeadline(enable: Boolean) {
        dataStore.edit { preferences ->
            val currentOrder = SortOrder.valueOf(preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name)
            val newSortOrder = when(enable) {
                true -> if(currentOrder == SortOrder.BY_PRIORITY) SortOrder.BY_DEADLINE_AND_PRIORITY else SortOrder.BY_DEADLINE
                false -> if(currentOrder == SortOrder.BY_DEADLINE_AND_PRIORITY) SortOrder.BY_PRIORITY else SortOrder.NONE
            }
            preferences[PreferencesKeys.SORT_ORDER] = newSortOrder.name
            updateSortOrder(newSortOrder)
        }
    }

    suspend fun enableSortByPriority(enable: Boolean) {
        dataStore.edit { preferences ->
            val currentOrder = SortOrder.valueOf(preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name)
            val newSortOrder = when(enable) {
                true -> if(currentOrder == SortOrder.BY_DEADLINE) SortOrder.BY_DEADLINE_AND_PRIORITY else SortOrder.BY_PRIORITY
                false -> if(currentOrder == SortOrder.BY_DEADLINE_AND_PRIORITY) SortOrder.BY_DEADLINE else SortOrder.NONE
            }
            preferences[PreferencesKeys.SORT_ORDER] = newSortOrder.name
            updateSortOrder(newSortOrder)
        }
    }

    private fun updateSortOrder(sortOrder: SortOrder) {
        sharedPreferences.edit {
            putString(SORT_ORDER_KEY, sortOrder.name)
        }
    }

    suspend fun updateShowCompleted(showCompleted: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.SHOW_COMPLETED] = showCompleted
        }
    }
}
