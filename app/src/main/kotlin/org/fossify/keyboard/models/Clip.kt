package org.fossify.keyboard.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.fossify.keyboard.interfaces.IList

@Entity(tableName = "clips", indices = [(Index(value = ["id"], unique = true))])
class Clip(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "value") val dbValue: String,
    @ColumnInfo(name = "type") override val itemViewType: Int
) : IList {
    val text: String
        get() = dbValue
}
