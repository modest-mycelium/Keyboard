package org.fossify.keyboard.models

import org.fossify.keyboard.helpers.ITEM_SECTION_LABEL
import org.fossify.keyboard.interfaces.IList

data class ClipsSectionLabel(val value: String, val isCurrent: Boolean) : IList {
    override val itemType: Int
        get() = ITEM_SECTION_LABEL
}
