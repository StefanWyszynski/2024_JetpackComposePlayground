package com.jetpackcompose.playground.task_realm.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.jetpackcompose.playground.task_room.domain.data.Priority
import com.jetpackcompose.playground.task_room.domain.data.RealmTask
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
@SmallTest
class RealmTaskRepositoryImplTest {

    private lateinit var realm: Realm
    private lateinit var repository: RealmTaskRepositoryImpl

    @Before
    fun setUp() {
        val config = RealmConfiguration.Builder(schema = setOf(RealmTask::class))
            .inMemory()
            .name("test-realm")
            .build()
        realm = Realm.open(config)
        repository = RealmTaskRepositoryImpl(realm)
    }

    @After
    fun tearDown() {
        realm.close()
    }

    @Test
    fun testInsertAndGetAllTasks() = runTest {
        // Given
        val task1 = RealmTask().apply {
            date = Date().toString()
            name = "Task1"
            priority = Priority.HIGH.ordinal
        }
        val task2 = RealmTask().apply {
            date = Date().toString()
            name = "Task2"
            priority = Priority.HIGH.ordinal
        }

        // When
        repository.insertTask(task1)
        repository.insertTask(task2)

        // Then
        val tasks = repository.getAllTasks().first()
        Truth.assertThat(tasks).hasSize(2)
        Truth.assertThat(tasks[0].name).isEqualTo(task1.name)
        Truth.assertThat(tasks[0].date).isEqualTo(task1.date)
        Truth.assertThat(tasks[0].priority).isEqualTo(task1.priority)
        Truth.assertThat(tasks[1].name).isEqualTo(task2.name)
        Truth.assertThat(tasks[1].date).isEqualTo(task2.date)
        Truth.assertThat(tasks[1].priority).isEqualTo(task2.priority)
    }

    @Test
    fun testDeleteTask() = runTest {
        // Given
        val task = RealmTask().apply {
            date = Date().toString()
            name = "Task1"
            priority = Priority.HIGH.ordinal
        }
        // When
        repository.insertTask(task)
//        advanceUntilIdle()
        val first = repository.getAllTasks().first()
        Truth.assertThat(first).isNotEmpty()
        repository.deleteTask(first.get(0))
//        advanceUntilIdle()

        // Then
        val tasks = repository.getAllTasks().first()
        Truth.assertThat(tasks).isEmpty()
    }
}