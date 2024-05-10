package com.jetpackcompose.playground.task_room.data

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.jetpackcompose.playground.main.data.database.AppDatabase
import com.jetpackcompose.playground.di.modules.DatabaseModule
import com.jetpackcompose.playground.task_room.domain.data.Priority
import com.jetpackcompose.playground.task_room.domain.data.Task
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date
import javax.inject.Inject


@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
@SmallTest
class TaskDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDatabase: AppDatabase

    private lateinit var taskDao: TaskDao

    @Before
    fun setUp() {
        hiltRule.inject()
        taskDao = appDatabase.taskDao()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun insertTaskTest() = runTest {

        val task1 = Task(
            taskId = 1,
            date = Date().toString(),
            name = "TestTask1",
            priority = Priority.HIGH
        )
        taskDao.insertTask(task1)
        val task2 = Task(
            taskId = 2,
            date = Date().toString(),
            name = "TestTask2",
            priority = Priority.HIGH
        )
        taskDao.insertTask(task2)
        // Observe the tasks
        val tasks = taskDao.getAllTasks().first()

        // Check if data is correct
        Truth.assertThat(tasks).contains(task1)
        Truth.assertThat(tasks).contains(task2)
    }

    @Test
    fun removeTaskTest() = runTest {

        val task = Task(
            taskId = 1,
            date = Date().toString(),
            name = "TestTask1",
            priority = Priority.HIGH
        )
        taskDao.insertTask(task)
        taskDao.deleteTask(task)

        // Observe the tasks
        val tasks = taskDao.getAllTasks().first()
        // Check if data is correct
        Truth.assertThat(tasks).isEmpty()
    }
}