package com.srgpanov.funboxtt.other

import android.widget.Button
import androidx.annotation.StringRes
import com.github.razir.progressbutton.ProgressParams
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.srgpanov.funboxtt.R
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

fun Button.showLoading(show: Boolean, text: String, params: ProgressParams.() -> Unit = {}) {
    if (show) {
        this.showProgress(params)
    } else {
        this.hideProgress(text)
    }
}

fun Button.showLoading(
    show: Boolean,
    @StringRes text: Int,
    params: ProgressParams.() -> Unit = {}
) {
    if (show) {
        this.showProgress(params)
    } else {
        this.hideProgress(text)
    }
}

@FlowPreview
fun <T> Flow<Iterable<T>>.fromIterable(): Flow<T> = flow {
    this@fromIterable.onEach { list ->
        for (item in list) {
            emit(item)
        }
    }

}
