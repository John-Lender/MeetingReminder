package com.lazzlepazzle.meetingremainder.Model

import android.R
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.lazzlepazzle.meetingremainder.FragmentRemind

class ReminderBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent!!.extras
        val builder = NotificationCompat.Builder(context!!,
            FragmentRemind.CHANNEL
        )
            .setSmallIcon(R.drawable.presence_away)
            .setContentTitle("Meeting")
            .setContentText("You have a meeting with ${bundle.getString("FULL_NAME")} in an hour")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            with(NotificationManagerCompat.from(context)) {
                notify(bundle.getInt("ID"), builder.build())
            }


    }

}