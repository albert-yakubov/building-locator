package com.stepashka.buildinglocator2.util

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import com.stepashka.buildinglocator2.MainActivity
import okhttp3.ResponseBody
import retrofit2.Callback
import java.util.regex.Pattern

abstract class Util {



    companion object {
        fun isEmailValid(email: String): Boolean {
            return true
        }


        fun Context.startActivity(context: Context, clazz: Class<*>) {
            val intent = Intent(context, clazz)
            startActivity(intent)
        }

        fun showResetAlert(context: Context) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Empty Username")
            builder.setMessage("Please enter your username to continue")
            builder.setNegativeButton("OK") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            builder.show()
        }
    }


}