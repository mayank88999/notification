package com.example.notification

import android.app.Notification
import android.content.Context
import android.media.MediaPlayer
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import android.util.Log

class MyNotificationService: NotificationListenerService(), TextToSpeech.OnInitListener {
    private val TAG = "NotificationListener"
    private lateinit var tts: TextToSpeech
    private var mediaPlayer: MediaPlayer? = null
    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(applicationContext, this)
    }
    override fun onDestroy() {
        super.onDestroy()
        if (tts.isSpeaking) {
            tts.stop()
        }
        tts.shutdown()
    }
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        val notification = sbn.notification
        val packageName = sbn.packageName

        if (packageName == "com.whatsapp") {

            playNotificationSound()

            speakNotification(notification)
        }
        speakNotification(notification)
    }
    private fun playNotificationSound() {
        try {
            mediaPlayer = MediaPlayer.create(applicationContext, R.raw.ringtone) // You can replace R.raw.ntification_sound with your sound resource
            mediaPlayer?.setOnCompletionListener {
                mediaPlayer?.release()
                mediaPlayer = null
            }
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun speakNotification(notification: Notification) {
        val text = notification.extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
        if (text != null) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {

        } else {
            Log.e(TAG, "TTS initialization failed")
        }
    }
}