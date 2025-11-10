package org.fossify.keyboard.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.fossify.keyboard.interfaces.IClip

@Entity(tableName = "clips", indices = [(Index(value = ["id"], unique = true))])
data class TextClip(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "value") private val value: String
) : ListItem(), IClip {
    override val content: Any
        get() = value
}
