package com.example.data.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import java.io.File
import java.io.FileInputStream

private const val TAG = "AudioRecorderHelper"

class AudioRecorderHelper(private val context: Context) {

    private var mediaRecorder: MediaRecorder? = null
    private var currentOutputFile: File? = null
    private var recordingStartTime: Long = 0L

    var isRecording: Boolean = false
        private set

    fun startRecording(): Boolean {
        if (isRecording) return false
        try {
            val cacheDir = context.cacheDir
            val outputFile = File(cacheDir, "solace_voice_${System.currentTimeMillis()}.m4a")
            currentOutputFile = outputFile

            val recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }

            recorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(64000)
                setAudioSamplingRate(44100)
                setOutputFile(outputFile.absolutePath)
                prepare()
                start()
            }

            mediaRecorder = recorder
            isRecording = true
            recordingStartTime = System.currentTimeMillis()
            Log.d(TAG, "Audio recording started: ${outputFile.absolutePath}")
            return true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start audio recording", e)
            cleanUp()
            return false
        }
    }

    data class RecordingResult(
        val audioBytes: ByteArray,
        val durationSeconds: Int,
        val mimeType: String = "audio/mp4"
    )

    fun stopRecording(): RecordingResult? {
        if (!isRecording || mediaRecorder == null) return null
        return try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            isRecording = false

            val durationSeconds = ((System.currentTimeMillis() - recordingStartTime) / 1000).toInt().coerceAtLeast(1)
            val file = currentOutputFile
            if (file != null && file.exists() && file.length() > 0) {
                val bytes = file.readBytes()
                Log.d(TAG, "Recording completed: ${bytes.size} bytes, ${durationSeconds}s")
                file.delete()
                RecordingResult(audioBytes = bytes, durationSeconds = durationSeconds)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping audio recording", e)
            cleanUp()
            null
        }
    }

    fun cancelRecording() {
        cleanUp()
    }

    private fun cleanUp() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
        } catch (_: Exception) {}
        mediaRecorder = null
        isRecording = false
        currentOutputFile?.delete()
        currentOutputFile = null
    }
}
