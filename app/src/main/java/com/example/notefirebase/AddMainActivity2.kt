package com.example.notefirebase

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase

class AddMainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_main2)

        val editTextTitle = findViewById<EditText>(R.id.editTextTitle)
        val editTextContent = findViewById<EditText>(R.id.editTextContent)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val title = editTextTitle.text.toString().trim()
            val content = editTextContent.text.toString().trim()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                addNote(title, content)
            } else {
                Toast.makeText(this, "Please enter both title and content", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addNote(title: String, content: String) {
        // Create a unique ID for each note
        val noteId = FirebaseDatabase.getInstance().getReference("notes").push().key

        // Create a Note object
        val note = Note(id = noteId ?: "", title = title, content = content)


        if (noteId != null) {
            FirebaseDatabase.getInstance().getReference("notes")
                .child(noteId)
                .setValue(note)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Note added successfully", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity after saving
                    } else {
                        Toast.makeText(this, "Failed to add note", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
