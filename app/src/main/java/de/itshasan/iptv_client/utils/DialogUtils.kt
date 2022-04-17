package de.itshasan.iptv_client.utils

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog

object DialogUtils {

    private var alertDialog: AlertDialog? = null

    fun showAlertDialog(
        context: Context,
        cancelable: Boolean,
        @Nullable title: String? = null,
        message: String? = null,
        positiveButtonText: String?,
        @Nullable positiveButtonListener: DialogInterface.OnClickListener? = null,
        negativeButtonText: String? = null,
        @Nullable negativeButtonListener: DialogInterface.OnClickListener? = null
    ) {
        if (alertDialog == null || !alertDialog?.isShowing!!) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            if (title != null) builder.setTitle(title)
            builder.setMessage(message)
            if (positiveButtonText != null) {
                if (positiveButtonListener != null) {
                    builder.setPositiveButton(positiveButtonText, positiveButtonListener)
                } else {
                    builder.setPositiveButton(
                        positiveButtonText
                    ) { dialog, id -> alertDialog?.dismiss() }
                }
            }
            if (negativeButtonText != null) {
                if (negativeButtonListener != null) {
                    builder.setNegativeButton(negativeButtonText, negativeButtonListener)
                } else {
                    builder.setNegativeButton(negativeButtonText,
                        { dialog, id -> alertDialog?.dismiss() })
                }
            }
            alertDialog = builder.create()
            alertDialog!!.setCancelable(cancelable)
            alertDialog!!.show()
        }
    }

    fun hideAlertDialog() {
        if (alertDialog != null && alertDialog?.isShowing!!) {
            alertDialog!!.dismiss()
        }
    }
}