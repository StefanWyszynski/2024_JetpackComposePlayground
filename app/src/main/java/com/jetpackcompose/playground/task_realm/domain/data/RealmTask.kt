package com.jetpackcompose.playground.task_realm.domain.data

import com.jetpackcompose.playground.task_room.domain.data.Priority
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class RealmTask : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var priority: Int = Priority.LOW.ordinal
    var date: String = ""
}