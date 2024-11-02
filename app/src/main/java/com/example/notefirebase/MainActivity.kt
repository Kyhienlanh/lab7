package com.example.notefirebase

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private val notes = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Set up edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerView)
        noteAdapter = NoteAdapter(notes, onNoteClicked = { note ->
            onNoteClicked(note)
        }, onDeleteNote = { note ->
            // Handle delete note
            deleteNote(note)
        })

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = noteAdapter
        }

        // Load notes from Firebase
        loadNotes()

        // Set up ImageView click listener to open AddMainActivity2
        val imageViewAddNote = findViewById<ImageView>(R.id.fabAddNote
        )
        imageViewAddNote.setOnClickListener {
            val intent = Intent(this, AddMainActivity2::class.java)
            startActivity(intent)
        }
    }

    private fun loadNotes() {
        val database = FirebaseDatabase.getInstance().getReference("notes")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notes.clear() // Clear existing notes to avoid duplication
                for (dataSnapshot in snapshot.children) {
                    val note = dataSnapshot.getValue(Note::class.java)
                    note?.let { notes.add(it) }
                }
                noteAdapter.notifyDataSetChanged() // Update adapter with new data
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load notes: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun onNoteClicked(note: Note) {
        val intent = Intent(this, UpdateMainActivity2::class.java)


        intent.putExtra("NOTE_ID", note.id)
        intent.putExtra("NOTE_TITLE", note.title)
        intent.putExtra("NOTE_CONTENT", note.content)

        startActivity(intent)
    }
    private fun deleteNote(note: Note) {
        // Create an AlertDialog builder
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Note")
        builder.setMessage("Are you sure you want to delete this note?")

        // Set the positive button for confirmation
        builder.setPositiveButton("Yes") { dialog, _ ->
            // Proceed with deleting the note
            val database = FirebaseDatabase.getInstance().getReference("notes").child(note.id)
            database.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
                    notes.remove(note)
                    noteAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Failed to delete note", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.dismiss()
        }

        // Set the negative button to cancel
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss() // Close the dialog if the user cancels
        }

        // Show the AlertDialog
        val dialog = builder.create()
        dialog.show()
    }
}
