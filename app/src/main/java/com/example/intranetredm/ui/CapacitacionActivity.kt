package com.example.intranetredm.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.intranetredm.R

class CapacitacionActivity : AppCompatActivity() {
    private val videoResources = intArrayOf( // R.raw.video1 es el primer video
        R.raw.video2, // R.raw.video2 es el segundo video
        R.raw.video3  // R.raw.video3 es el tercer video
    )

    private lateinit var videoView: VideoView
    private lateinit var mediaController: MediaController
    private lateinit var progressBar: ProgressBar
    private var currentVideoIndex = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capacitacion)

        videoView = findViewById(R.id.videoView)
        mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        progressBar = findViewById<ProgressBar>(R.id.progressBar2)
        progressBar.max = videoResources.size

        playNextVideo()

        videoView.setOnCompletionListener {
            // Se ejecuta cuando un video ha terminado de reproducirse
            currentVideoIndex++
            if (currentVideoIndex < videoResources.size) {
                playNextVideo()
            } else {
                // Todos los videos se han reproducido, realizar alguna acción
                // Aquí puedes iniciar la actividad que desees
                // Ejemplo:
                // val intent = Intent(this, OtraActividad::class.java)
                 //startActivity(intent)
            }
        }

        videoView.setOnPreparedListener { mp ->
            // Se ejecuta cuando el video está listo para ser reproducido
            val duration = mp.duration
            progressBar.progress = currentVideoIndex
            progressBar.max = videoResources.size
            progressBar.secondaryProgress = 0
            progressBar.progressDrawable = resources.getDrawable(R.drawable.custom_progress_bar)

            Thread {
                var progress = 0
                while (progress <= duration) {
                    progressBar.secondaryProgress = currentVideoIndex
                    progress = videoView.currentPosition
                    progressBar.progress = progress
                }
            }.start()
        }
    }

    private fun playNextVideo() {
        val videoPath =
            "android.resource://" + packageName + "/" + videoResources[currentVideoIndex]
        videoView.setVideoPath(videoPath)
        videoView.start()
    }
}