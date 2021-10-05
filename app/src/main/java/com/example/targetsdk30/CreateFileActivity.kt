package com.example.targetsdk30

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import android.widget.Toast

import android.provider.MediaStore

import android.os.Environment

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import java.io.File
import java.io.IOException
import java.io.OutputStream


class CreateFileActivity : AppCompatActivity() {
    val fileContent: String =
        "0;0000000000000000;0000000000000000;U.%&,,#) )\"-)!\".;ACMS Version 4.0;CAP Index File;@TS:04:20:50 PM, 11/22/19;;F\n" +
                "10;1;1;F010-G001-P0001;Allegion BlR;~;AD200Test;01000100.D01;F;00000000;0;\n" +
                "10;1;2;F010-G001-P0002;Allegion BlR;~;front door;01000100.D02;F;00000000;0;"
    val uplinklog: String = "[Programmed Locks]\n" +
            "F004-G001-P0001=1352107566,1606299847859\n" +
            "[F004-G001-P0001]\n" +
            "MaxUsers=5000\n" +
            "MaxAudits=5000\n" +
            "ProgrammedUsers=5000\n" +
            "CardReader=1\n";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_file)
        val edittext = findViewById<TextInputEditText>(R.id.textinput)
        val textview = findViewById<TextView>(R.id.textview)
        val okButton = findViewById<Button>(R.id.okbutton)
       // listFiles(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!)
        okButton.setOnClickListener { createFileInDirectory(edittext, textview) }
    }

    private fun createFileInDirectory(edittext: TextInputEditText, textView: TextView) {
        val filename = edittext.text.toString()
        if (filename.contains("*") || TextUtils.isEmpty(filename)) {
            textView.setText("File name cannot be empty or cannot contain special characters.")
            return;
        } else if (isFileAlreadyExists(filename)) {
          textView.setText("File already exists")
        }
        try {
            //Capindex creation
            val values = ContentValues()
            values.put(
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                filename
            ) //file name
            values.put(
                MediaStore.Files.FileColumns.MIME_TYPE,
                "*/*"
            ) //file extension, will automatically add to file
            values.put(
                MediaStore.Files.FileColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS + "/SUS/"
            ) //end "/" is not mandatory
            val uri: Uri? = contentResolver.insert(
                MediaStore.Files.getContentUri("external"),
                values
            ) //important!
            val outputStream: OutputStream? = contentResolver.openOutputStream(uri!!)
            outputStream!!.write(fileContent.toByteArray())
            outputStream.close()
            Toast.makeText(this, "File created successfully", Toast.LENGTH_SHORT)
                .show()

            //Uplink log creation
            val uplinkValues = ContentValues()
            uplinkValues.put(
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                "uplink.log"
            ) //file name
            uplinkValues.put(
                MediaStore.Files.FileColumns.MIME_TYPE,
                "*/*"
            ) //file extension, will automatically add to file
            uplinkValues.put(
                MediaStore.Files.FileColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS + "/SUS/"
            ) //end "/" is not mandatory
            val uriUplink: Uri? = contentResolver.insert(
                MediaStore.Files.getContentUri("external"),
                uplinkValues
            ) //important!
            val uplinkOutputStream: OutputStream? = contentResolver.openOutputStream(uriUplink!!)
            uplinkOutputStream!!.write(uplinklog.toByteArray())
            uplinkOutputStream.close()
            Toast.makeText(this, "File created successfully", Toast.LENGTH_SHORT)
                .show()
        } catch (e: IOException) {
            Toast.makeText(this, "Fail to create file", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isFileAlreadyExists(fileName: String): Boolean {
        val contentUri = MediaStore.Files.getContentUri("external")
        val selection =
            MediaStore.Files.FileColumns.RELATIVE_PATH + "= ? and " + MediaStore.Files.FileColumns.DISPLAY_NAME + "= ? "
        val selectionArgs = arrayOf(Environment.DIRECTORY_DOWNLOADS + "/SUS/", fileName)
        val cursor = contentResolver.query(contentUri, null, selection, selectionArgs, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val file: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME));
                if (file.equals(fileName)) {
                    return true
                }
            }
        }
        return false
    }

    private fun listFiles(directory: File) {
        val files = directory.listFiles()
        if (files != null) {
            for (file in files) {
                if (file != null) {
                    Log.d("Media store api:: ", "Files #####" + file.name)
                }
            }
        }
    }
}
