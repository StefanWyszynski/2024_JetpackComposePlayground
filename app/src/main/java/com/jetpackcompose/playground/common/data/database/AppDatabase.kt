package com.jetpackcompose.playground.common.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jetpackcompose.playground.task_room.data.TaskDao
import com.jetpackcompose.playground.task_room.domain.data.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

}
//@Database(entities = [Task::class], version = 1, exportSchema = false)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun taskDao(): TaskDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getDatabase(context: Context): AppDatabase {
//            if (INSTANCE == null) {
//                synchronized(this) {
//                    val instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        AppDatabase::class.java,
//                        "app_database"
//                    ).build()
//
//                    INSTANCE = instance
//                    instance
//                }
//            }
//            return INSTANCE!!
//        }
//    }
//}