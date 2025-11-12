package org.fossify.keyboard.providers

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import androidx.core.content.MimeTypeFilter
import org.fossify.keyboard.BuildConfig
import org.fossify.keyboard.databases.ClipsDatabase
import org.fossify.keyboard.extensions.getCurrentClip
import org.fossify.keyboard.extensions.getCurrentImageClipType
import org.fossify.keyboard.helpers.CURR_IMAGE_CLIP_URI_CODE
import org.fossify.keyboard.helpers.PINNED_IMAGE_CLIP_URI_CODE

const val AUTHORITY = BuildConfig.APPLICATION_ID + ".provider"

private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
    // TODO: use a Contract class for uri and mimetype constants
    addURI(AUTHORITY, "imageClip/curr", CURR_IMAGE_CLIP_URI_CODE)
    addURI(AUTHORITY, "imageClip/#", PINNED_IMAGE_CLIP_URI_CODE)
}

class ClipboardContentProvider : ContentProvider() {
    init { Log.d("clipProvider", "init") }

    private val database: ClipsDatabase
        get() = ClipsDatabase.getInstance(context!!)

    override fun getType(uri: Uri): String? {
        Log.d("clipProvider", "getting type of $uri")
        Log.d("clipProvider", "called by ${Thread.currentThread().stackTrace.joinToString()}")
        return when (uriMatcher.match(uri)) {
            CURR_IMAGE_CLIP_URI_CODE -> context!!.getCurrentImageClipType().apply { Log.d("clipProvider", "MIME type is $this") }
            PINNED_IMAGE_CLIP_URI_CODE -> TODO("pinned img clip uri mimetype unimplemented")
            else -> null
        }
    }

    override fun getStreamTypes(uri: Uri, mimeTypeFilter: String): Array<out String?>? {
        val uriMimeType = when (uriMatcher.match(uri)) {
            CURR_IMAGE_CLIP_URI_CODE -> context!!.getCurrentImageClipType()
            PINNED_IMAGE_CLIP_URI_CODE -> TODO("pinned img clip uri mimetype unimplemented")
            else -> null
        }
        Log.d("clipProvider", "getting stream types ($uriMimeType)")

        return if (MimeTypeFilter.matches(uriMimeType, mimeTypeFilter)) arrayOf(uriMimeType) else null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String?>?,
        selection: String?,
        selectionArgs: Array<out String?>?,
        sortOrder: String?
    ): Cursor? {
        Log.d("clipProvider", "querying $uri")
        return when (uriMatcher.match(uri)) {
            CURR_IMAGE_CLIP_URI_CODE -> {
                val cursor = MatrixCursor(arrayOf("imageClips"), 1)
                cursor.addRow(arrayOf(context!!.getCurrentClip(requestImage = true)?.bytes ?: return null))
                cursor
            }
            PINNED_IMAGE_CLIP_URI_CODE -> database.query("SELECT value FROM clips WHERE id = ?", arrayOf(uri.lastPathSegment))
            else -> null
        }
    }

    override fun onCreate(): Boolean {
        Log.d("clipProvider", "onCreate")
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String?>?
    ): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String?>?): Int = 0
}
