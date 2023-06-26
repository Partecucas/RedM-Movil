package com.example.intranetredm.ui.views.Capacitacion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.intranetredm.R
import com.example.intranetredm.databinding.FragmentCapacitacionBinding

class CapacitacionFragment : Fragment() {
    private val videoResources = intArrayOf(
        R.raw.video2,
        R.raw.video2,
        R.raw.video3
    )

    private lateinit var videoView1: VideoView
    private lateinit var videoView2: VideoView
    private lateinit var videoView3: VideoView
    private lateinit var mediaController1: MediaController
    private lateinit var mediaController2: MediaController
    private lateinit var mediaController3: MediaController
    private lateinit var progressBar: ProgressBar
    private var currentVideoIndex = 0

    private var _binding: FragmentCapacitacionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(CapacitacionViewModel::class.java)

        _binding = FragmentCapacitacionBinding.inflate(inflater, container, false)
        val root: View = binding.root


        videoView1 = binding.videoView1
        videoView2 = binding.videoView2
        videoView3 = binding.videoView3

        mediaController1 = MediaController(requireContext())
        mediaController2 = MediaController(requireContext())
        mediaController3 = MediaController(requireContext())

        videoView1.setMediaController(mediaController1)
        videoView2.setMediaController(mediaController2)
        videoView3.setMediaController(mediaController3)

        progressBar = binding.progressBar2
        progressBar.max = videoResources.size

        playNextVideo()

        videoView1.setOnCompletionListener {
            currentVideoIndex++
            if (currentVideoIndex < videoResources.size) {
                playNextVideo()
            } else {
                mostrarPreguntas()
            }
        }

        videoView2.setOnCompletionListener {
            currentVideoIndex++
            if (currentVideoIndex < videoResources.size) {
                playNextVideo()
            } else {
                mostrarPreguntas()
            }
        }

        videoView3.setOnCompletionListener {
            currentVideoIndex++
            if (currentVideoIndex < videoResources.size) {
                playNextVideo()
            } else {
                mostrarPreguntas()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun playNextVideo() {
        when (currentVideoIndex) {
            0 -> {
                val videoPath =
                    "android.resource://" + requireActivity().packageName + "/" + videoResources[currentVideoIndex]
                videoView1.setVideoPath(videoPath)
                videoView1.start()
            }
            1 -> {
                val videoPath =
                    "android.resource://" + requireActivity().packageName + "/" + videoResources[currentVideoIndex]
                videoView2.setVideoPath(videoPath)
                videoView2.start()
            }
            2 -> {
                val videoPath =
                    "android.resource://" + requireActivity().packageName + "/" + videoResources[currentVideoIndex]
                videoView3.setVideoPath(videoPath)
                videoView3.start()
            }
        }
    }

    private fun mostrarPreguntas() {
        // Aquí puedes mostrar las preguntas para los videos
        // Puedes crear una nueva actividad para mostrar las preguntas o utilizar un diálogo
        // Ejemplo:
        // val intent = Intent(requireContext(), RegisterActivity::class.java)
       // startActivity(intent)
    }
}
