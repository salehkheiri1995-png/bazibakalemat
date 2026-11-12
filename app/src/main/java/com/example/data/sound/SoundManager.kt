package com.example.data.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.example.data.model.SoundType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

class SoundManager(private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    var isSoundEnabled: Boolean = true
    var isVibrationEnabled: Boolean = true

    fun playSound(type: SoundType) {
        if (!isSoundEnabled) return
        scope.launch {
            try {
                when (type) {
                    SoundType.CLICK -> playTone(frequency = 520.0, durationMs = 40, volume = 0.35f)
                    SoundType.LETTER_PICK -> playTone(frequency = 680.0, durationMs = 60, volume = 0.4f)
                    SoundType.WORD_FOUND -> playArpeggio(listOf(523.25, 659.25, 783.99, 1046.50), noteDurationMs = 70)
                    SoundType.LEVEL_WON -> playVictoryChime()
                    SoundType.ERROR -> playTone(frequency = 180.0, durationMs = 140, volume = 0.45f)
                    SoundType.COIN_EARNED -> playArpeggio(listOf(987.77, 1318.51), noteDurationMs = 50)
                    SoundType.WHEEL_SPIN -> playTone(frequency = 440.0, durationMs = 30, volume = 0.25f)
                }
            } catch (_: Exception) {
                // Audio failure graceful ignore
            }
        }
    }

    fun vibrate(type: SoundType) {
        if (!isVibrationEnabled) return
        val vib = vibrator ?: return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                when (type) {
                    SoundType.CLICK, SoundType.LETTER_PICK -> {
                        vib.vibrate(VibrationEffect.createOneShot(18, VibrationEffect.DEFAULT_AMPLITUDE))
                    }
                    SoundType.WORD_FOUND, SoundType.COIN_EARNED -> {
                        vib.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 30, 40, 50), -1))
                    }
                    SoundType.LEVEL_WON -> {
                        vib.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 40, 60, 80, 60, 100), -1))
                    }
                    SoundType.ERROR -> {
                        vib.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 60, 50, 70), -1))
                    }
                    SoundType.WHEEL_SPIN -> {
                        vib.vibrate(VibrationEffect.createOneShot(12, VibrationEffect.DEFAULT_AMPLITUDE))
                    }
                }
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(30)
            }
        } catch (_: Exception) {
            // ignore vibration error
        }
    }

    private fun playTone(frequency: Double, durationMs: Int, volume: Float = 0.4f) {
        val sampleRate = 22050
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val angle = 2.0 * Math.PI * i / (sampleRate / frequency)
            val envelope = (1.0 - (i.toDouble() / numSamples)).toFloat() // fade out
            buffer[i] = (sin(angle) * Short.MAX_VALUE * volume * envelope).toInt().toShort()
        }
        writeAudioTrack(buffer, sampleRate)
    }

    private fun playArpeggio(notes: List<Double>, noteDurationMs: Int) {
        val sampleRate = 22050
        val samplesPerNote = (sampleRate * (noteDurationMs / 1000.0)).toInt()
        val totalSamples = samplesPerNote * notes.size
        val buffer = ShortArray(totalSamples)

        notes.forEachIndexed { noteIdx, freq ->
            val offset = noteIdx * samplesPerNote
            for (i in 0 until samplesPerNote) {
                val angle = 2.0 * Math.PI * i / (sampleRate / freq)
                val envelope = (1.0 - (i.toDouble() / samplesPerNote)).toFloat()
                buffer[offset + i] = (sin(angle) * Short.MAX_VALUE * 0.45f * envelope).toInt().toShort()
            }
        }
        writeAudioTrack(buffer, sampleRate)
    }

    private fun playVictoryChime() {
        // C5, E5, G5, B5, C6 triumphant scale
        playArpeggio(listOf(523.25, 659.25, 783.99, 987.77, 1046.50), noteDurationMs = 85)
    }

    private fun writeAudioTrack(buffer: ShortArray, sampleRate: Int) {
        val bufferSize = buffer.size * 2
        var track: AudioTrack? = null
        try {
            track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(buffer, 0, buffer.size)
            track.play()
            Thread.sleep(buffer.size * 1000L / sampleRate + 30)
        } catch (_: Exception) {
            // Track write fail safe
        } finally {
            try {
                track?.stop()
                track?.release()
            } catch (_: Exception) {}
        }
    }
}
