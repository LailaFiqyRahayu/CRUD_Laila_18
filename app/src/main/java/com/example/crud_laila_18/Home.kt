package com.example.crud_laila_18

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crud_laila_18.db.Constant
import com.example.crud_laila_18.db.Note
import com.example.crud_laila_18.db.NoteDB
import kotlinx.android.synthetic.main.home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Home : AppCompatActivity() {
    private val db by lazy { NoteDB(this) }
    lateinit var noteAdapter: NoteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        setupView()
        setupListener()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData(){
        CoroutineScope(Dispatchers.IO).launch {
            noteAdapter.setData(db.noteDao().getNotes())
            withContext(Dispatchers.Main) {
                noteAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setupView (){
        supportActionBar!!.apply {
            title = "Daftar Catatan"
        }
    }

    private fun setupListener(){
        button_create.setOnClickListener {
            intentEdit(Constant.TYPE_CREATE, 0)
        }
    }

    private fun setupRecyclerView () {

        noteAdapter = NoteAdapter(
            arrayListOf(),
            object : NoteAdapter.OnAdapterListener {
                override fun onClick(note: Note) {
                    intentEdit(Constant.TYPE_READ, note.id)
                }

                override fun onUpdate(note: Note) {
                    intentEdit(Constant.TYPE_UPDATE, note.id)
                }

                override fun onDelete(note: Note) {
                    deleteAlert(note)
                }

            })

        list.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = noteAdapter
        }

    }

    private fun intentEdit(intent_type: Int, note_id: Int) {
        startActivity(
            Intent(this, form::class.java)
                .putExtra("intent_type", intent_type)
                .putExtra("note_id", note_id)
        )

    }

    private fun deleteAlert(note: Note){
        val dialog = AlertDialog.Builder(this)
        dialog.apply {
            setTitle("Hapus")
            setMessage("Mau Hapus Catatan ${note.title}?")
            setNegativeButton("Batal") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Hapus") { dialogInterface, i ->
                CoroutineScope(Dispatchers.IO).launch {
                    db.noteDao().deleteNote(note)
                    dialogInterface.dismiss()
                    loadData()
                }
            }
        }

        dialog.show()
    }
}