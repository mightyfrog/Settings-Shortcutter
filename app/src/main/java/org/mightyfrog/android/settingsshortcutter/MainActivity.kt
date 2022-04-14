package org.mightyfrog.android.settingsshortcutter

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.mightyfrog.android.settingsshortcutter.databinding.ActivityMainBinding

/**
 * @author Shigehiro Soejima
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @TargetApi(26)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = ItemAdapter(supportFragmentManager, this)

        if (Build.VERSION.SDK_INT >= 26) {
            createTestChannel("test_id_1", "Test Channel 1")
            createTestChannel("test_id_2", "Test Channel 2")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        android.util.Log.e(
            MainActivity::class.java.simpleName,
            "requestCode: $requestCode, resultCode: $resultCode, data: $data"
        )
        super.onActivityResult(requestCode, resultCode, data)
    }

    @TargetApi(26)
    private fun createTestChannel(id: String, name: String) {
        val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}
