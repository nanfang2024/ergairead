package io.legado.app.ui.main.bookshelf.compose

import androidx.annotation.StringRes
import io.legado.app.R
import io.legado.app.data.dao.BookShelfDisplay

enum class NgShelfFilter(@StringRes val labelRes: Int) {
    All(R.string.ng_filter_all),
    Reading(R.string.ng_filter_reading),
    WantToRead(R.string.ng_filter_want),
    Finished(R.string.ng_filter_finished),
    Downloaded(R.string.ng_filter_downloaded)
}

val BookShelfDisplay.isNgWantToRead: Boolean
    get() = durChapterIndex == 0 && durChapterTime == 0L

val BookShelfDisplay.isNgFinished: Boolean
    get() = totalChapterNum > 0 && durChapterIndex >= totalChapterNum

val BookShelfDisplay.isNgReading: Boolean
    get() = !isNgWantToRead && !isNgFinished

fun BookShelfDisplay.matches(filter: NgShelfFilter): Boolean {
    return when (filter) {
        NgShelfFilter.All -> true
        NgShelfFilter.Reading -> isNgReading
        NgShelfFilter.WantToRead -> isNgWantToRead
        NgShelfFilter.Finished -> isNgFinished
        NgShelfFilter.Downloaded -> isLocal
    }
}
