package com.example.notefirebase

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class UpdateMainActivity2 : AppCompatActivity() {

    private lateinit var noteId: String
    private lateinit var noteTitle: String
    private lateinit var noteContent: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_main2)

        // Retrieve data from the Intent
        noteId = intent.getStringExtra("NOTE_ID") ?: ""
        noteTitle = intent.getStringExtra("NOTE_TITLE") ?: ""
        noteContent = intent.getStringExtra("NOTE_CONTENT") ?: ""

        // Initialize UI elements
        val editTextTitle = findViewById<EditText>(R.id.editTextTitle)
        val editTextContent = findViewById<EditText>(R.id.editTextContent)
        val buttonUpdate = findViewById<Button>(R.id.buttonUpdate)

        // Set the retrieved data to the EditTexts
        editTextTitle.setText(noteTitle)
        editTextContent.setText(noteContent)

        // Handle the update button click
        buttonUpdate.setOnClickListener {
            val updatedTitle = editTextTitle.text.toString().trim()
            val updatedContent = editTextContent.text.toString().trim()

            if (updatedTitle.isNotEmpty() && updatedContent.isNotEmpty()) {
                updateNote(noteId, updatedTitle, updatedContent)
            } else {
                Toast.makeText(this, "Please enter both title and content", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateNote(noteId: String, title: String, content: String) {
        val noteRef = FirebaseDatabase.getInstance().getReference("notes").child(noteId)

        noteRef.child("title").setValue(title)
        noteRef.child("content").setValue(content)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity after updating
                } else {
                    Toast.makeText(this, "Failed to update note", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
