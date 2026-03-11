package com.iihsan.floatingtako

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val urlInput = findViewById<EditText>(R.id.urlInput)
        val btnPermission = findViewById<Button>(R.id.btnPermission)
        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnStop = findViewById<Button>(R.id.btnStop)

        btnPermission.setOnClickListener {
            openOverlayPermission()
        }

        btnStart.setOnClickListener {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "Aktifkan izin overlay dulu", Toast.LENGTH_SHORT).show()
                openOverlayPermission()
                return@setOnClickListener
            }

            val intent = Intent(this, OverlayService::class.java)
            intent.putExtra("overlay_url", urlInput.text.toString())

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent)
                } else {
                    startService(intent)
                }
                Toast.makeText(this, "Start overlay dikirim", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Gagal start service: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        btnStop.setOnClickListener {
            stopService(Intent(this, OverlayService::class.java))
            Toast.makeText(this, "Overlay dihentikan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivity(intent)
    }
}
