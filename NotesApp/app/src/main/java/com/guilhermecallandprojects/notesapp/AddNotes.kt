package com.guilhermecallandprojects.notesapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*

class AddNotes : AppCompatActivity() {

    var createOrUpdate = "create"
    var noteID = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        var bundle : Bundle? = intent.extras
        if(bundle != null){
            createOrUpdate = "update"
            noteID = bundle.getInt("id")
            et_title.setText(bundle.getString("name"))
            et_description.setText(bundle.getString("description"))
        }else{
            Log.i("app", "bundle was a null value")
        }

    }

    fun buAdd(view: View){
        val dbManager = DbManager(this)

        val values = ContentValues()
        values.put("title", et_title.text.toString())
        values.put("description", et_description.text.toString())


        if (createOrUpdate == "create"){
            val ID = dbManager.Insert(values)
            if(ID>0){
                Toast.makeText(this, "note is added", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "note cannot be added", Toast.LENGTH_LONG).show()
            }
        }else if (createOrUpdate == "update"){
            val selectionArgs = arrayOf(noteID.toString())
            val ID = dbManager.Update(values, "id=?", selectionArgs)
            if(ID > 0){
                Toast.makeText(this, "note is updated", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "note could not be updated", Toast.LENGTH_LONG).show()
            }
        }

        finish()

    }
}