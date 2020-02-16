package pl.mrozok.sensorsreader.presentation

import android.content.Context
import androidx.appcompat.app.AlertDialog
import pl.mrozok.sensorsreader.R

object ProgressDialog {

    fun show(
        context: Context,
        onCancelAction: () -> Unit
    ): AlertDialog = AlertDialog.Builder(context, R.style.ProgressDialog)
        .setView(R.layout.progress_dialog)
        .setOnCancelListener { onCancelAction.invoke() }
        .show()

}
