package org.fossify.keyboard.models

import android.util.Base64
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.fossify.keyboard.helpers.ITEM_IMAGE_CLIP
import org.fossify.keyboard.helpers.ITEM_TEXT_CLIP
import org.fossify.keyboard.interfaces.IList

@Entity(tableName = "clips", indices = [(Index(value = ["id"], unique = true))])
class Clip(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "value", typeAffinity = ColumnInfo.BLOB) val bytes: ByteArray,
    @ColumnInfo(name = "type") override val itemType: Int
) : IList {
    constructor(text: String) : this(0, text.encodeToByteArray(), ITEM_TEXT_CLIP)
    constructor(imgData: ByteArray) : this(0, imgData, ITEM_IMAGE_CLIP)

    val asText: String
        get() = when (itemType) {
            ITEM_TEXT_CLIP -> bytes.decodeToString()
            ITEM_IMAGE_CLIP -> Base64.encodeToString(bytes, Base64.DEFAULT)
            else -> "ERR: unrecognized clipboard type"
        }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Clip || other.itemType != itemType) return false

        return other.bytes.contentEquals(bytes)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + itemType
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
