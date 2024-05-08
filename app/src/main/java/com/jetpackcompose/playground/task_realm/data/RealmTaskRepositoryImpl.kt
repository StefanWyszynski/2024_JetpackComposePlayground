package com.jetpackcompose.playground.task_realm.data

import com.jetpackcompose.playground.task_realm.domain.RealmTaskRepository
import com.jetpackcompose.playground.task_realm.domain.data.RealmTask
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RealmTaskRepositoryImpl @Inject constructor(private var realmDb: Realm) :
    RealmTaskRepository {
    override suspend fun getAllTasks(): Flow<List<RealmTask>> {
        return realmDb.query<RealmTask>().asFlow().map { it.list.toList() }
    }

    override suspend fun insertTask(task: RealmTask) {
        realmDb.write {
            copyToRealm(task)
        }

    }

    override suspend fun deleteTask(realmTask: RealmTask) {
        realmDb.write {
            val task = findLatest(realmTask) ?: return@write
            delete(task)
        }
    }
}