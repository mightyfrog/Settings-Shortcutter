package org.mightyfrog.android.settingsshortcutter

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * @author Shigehiro Soejima
 */
class MainActivity : AppCompatActivity() {

    @TargetApi(26)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<RecyclerView>(R.id.rv).let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = ItemAdapter(supportFragmentManager, this)
        }

        if (Build.VERSION.SDK_INT >= 26) {
            createTestChannel("test_id_1", "Test Channel 1")
            createTestChannel("test_id_2", "Test Channel 2")
        }
    }

    @TargetApi(26)
    private fun createTestChannel(id: String, name: String) {
        val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}
