package com.guilhermecallandprojects.notesapp

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*


class MainActivity : AppCompatActivity() {

    var listNotes = ArrayList<Note>()
    var myNotesAdapter: MyNotesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        myNotesAdapter = MyNotesAdapter(this, listNotes)
        lv_notes.adapter = myNotesAdapter

        //load from database
        loadQuery("%")

    }

    override fun onResume(){
        Log.i("app", "onResume")
        loadQuery("%")
        super.onResume()
    }

    fun loadQuery(title: String){
        val dbManager =DbManager(this)
        val projection = arrayOf("id", "title", "description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(projection, "title like ?", selectionArgs, "title")
        listNotes.clear()
        if(cursor.moveToFirst()){
            do{
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val description = cursor.getString(cursor.getColumnIndex("description"))
                listNotes.add(Note(id, title, description))
            }while(cursor.moveToNext())
        }
        myNotesAdapter!!.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?) : Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val sv = menu?.findItem(R.id.search_item)?.actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
                loadQuery("%$query%" )
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                loadQuery("%")
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_item -> {
                val intent = Intent(this, AddNotes::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class MyNotesAdapter(var context: Context, var listNotesAdapter: ArrayList<Note>) : BaseAdapter() {
        override fun getCount(): Int {
            return listNotesAdapter.size
        }

        override fun getItem(position: Int): Any {
            return listNotesAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return listNotesAdapter[position].noteID!!.toLong()
        }

        override fun getView(position: Int, p1: View?, viewGroup: ViewGroup?): View {
            val myView = layoutInflater.inflate(R.layout.ticket, null)
            val myNote = listNotesAdapter[position]
            myView.tv_title.text = myNote.noteName
            myView.tv_content.text = myNote.noteDes
            myView.iv_delete.setOnClickListener( View.OnClickListener{
                val dbManager = DbManager(this.context)
                val selectionArgs = arrayOf(myNote.noteID.toString())
                dbManager.Delete("id=?", selectionArgs);
                loadQuery("%")
            })

            myView.iv_edit.setOnClickListener {
                gotoUpdate(myNote)
            }

            return myView
        }
    }

    private fun gotoUpdate(note: Note) {
        var intent = Intent(this, AddNotes::class.java)
        intent.putExtra("id", note.noteID)
        intent.putExtra("name", note.noteName)
        intent.putExtra("des", note.noteDes)
        startActivity(intent)
    }
}