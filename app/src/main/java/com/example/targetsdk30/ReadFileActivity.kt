package com.example.targetsdk30

import android.content.ContentUris
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.io.IOException

class ReadFileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_file)
        val edittext = findViewById<TextInputEditText>(R.id.read_textinput)
        val okButton = findViewById<Button>(R.id.submitbutton)
        val textview = findViewById<TextView>(R.id.read_text_view)
        okButton.setOnClickListener { readFileContents(textview, edittext) }
    }

    private fun readFileContents(textview: TextView?, edittext: TextInputEditText) {
        val filename = edittext.text.toString()
        val contentUri = MediaStore.Files.getContentUri("external")
        val selection =
            MediaStore.Files.FileColumns.RELATIVE_PATH + "= ? and " + MediaStore.Files.FileColumns.DISPLAY_NAME + "= ? "
        val selectionArgs = arrayOf(Environment.DIRECTORY_DOWNLOADS + "/SUS/", filename)
        val cursor = contentResolver.query(contentUri, null, selection, selectionArgs, null)
        var uri: Uri? = null
        if (cursor != null && cursor.moveToFirst()) {
                val id: Long = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
                uri = ContentUris.withAppendedId(contentUri, id)
        }

        try {
            val filetext: String =
                contentResolver.openInputStream(uri!!)!!.bufferedReader().use { it.readText() }
            textview!!.setText(filetext)
        } catch (e: IOException) {
            Toast.makeText(this, "Fail to read file", Toast.LENGTH_SHORT).show();
        }

    }
}

