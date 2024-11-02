package com.example.notefirebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Note(
    val id: String = "",
    var title: String = "",
    var content: String = "",
    val creationDate: String = getCurrentDate()
) {
    companion object {
        private fun getCurrentDate(): String {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            return dateFormat.format(Date())
        }
    }
}
