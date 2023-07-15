package br.pucpr.appdev.schuhspizzaria.shared

import android.app.AlertDialog
import android.content.Context
import br.pucpr.appdev.schuhspizzaria.R
import java.util.concurrent.CompletableFuture

object Functions {
    fun showConfirmationDialog(context: Context, message: String) : CompletableFuture<Boolean> {
        val result = CompletableFuture<Boolean>()
        val alertDialogBuilder = AlertDialog.Builder(context)

        alertDialogBuilder.setTitle("ATENÇÃO")
        alertDialogBuilder.setMessage(message)

        alertDialogBuilder.setPositiveButton("Ok") { dialog, which ->
            result.complete(true)
            dialog.dismiss()
        }

        alertDialogBuilder.setNegativeButton("Cancelar") { dialog, which ->
            result.complete(false)
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        return result
    }
}