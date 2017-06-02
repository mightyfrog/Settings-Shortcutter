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
import android.support.v7.widget.Toolbar

/**
 * @author Shigehiro Soejima
 */
class MainActivity : AppCompatActivity() {

    @TargetApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val rv = findViewById(R.id.rv) as RecyclerView
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = ItemAdapter(this)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createTestChannel("test_id_1", "Test Channel 1")
//            createTestChannel("test_id_2", "Test Channel 2")
//        }
        createTestChannel("test_id_1", "Test Channel 1")
        createTestChannel("test_id_2", "Test Channel 2")
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createTestChannel(id: String, name: String) {
        val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}
