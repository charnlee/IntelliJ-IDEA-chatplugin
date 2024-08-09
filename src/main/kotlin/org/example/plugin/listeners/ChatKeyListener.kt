package org.example.plugin.listeners


import org.example.plugin.services.MessageSender
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JTextField

class ChatKeyListener(private val messageSender: MessageSender) : KeyAdapter() {
    override fun keyPressed(e: KeyEvent) {
        if (e.keyCode == KeyEvent.VK_ENTER) {
            messageSender.sendMessage()
        }
    }
}
