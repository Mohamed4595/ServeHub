package com.servehub.android

import android.content.Context
import android.content.Intent
import android.net.Uri

class PhoneDialer(
    private val context: Context
) {
    fun dial(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
