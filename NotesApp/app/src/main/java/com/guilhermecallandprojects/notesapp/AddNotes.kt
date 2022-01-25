package com.guilhermecallandprojects.notesapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*

class AddNotes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)
    }

    fun buAdd(view: View){
        val dbManager = DbManager(this)

        val values = ContentValues()
        values.put("title", et_title.text.toString())
        values.put("description", et_description.text.toString())

        val ID = dbManager.Insert(values)
        if(ID>0){
            Toast.makeText(this, "note is added", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "note cannot be added", Toast.LENGTH_LONG).show()
        }

        finish()
    }
}