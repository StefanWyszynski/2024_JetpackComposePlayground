package com.jetpackcompose.playground.task_room.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import com.jetpackcompose.playground.common.data.database.AppDatabase
import com.jetpackcompose.playground.task_room.domain.data.Priority
import com.jetpackcompose.playground.task_room.domain.data.Task
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
@SmallTest
class TaskDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var taskDao: TaskDao

    @Before
    fun setUp() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        taskDao = appDatabase.taskDao()
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
        val tasks = taskDao.getAllTask().first()

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
        val tasks = taskDao.getAllTask().first()
        // Check if data is correct
        Truth.assertThat(tasks).isEmpty()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }
}