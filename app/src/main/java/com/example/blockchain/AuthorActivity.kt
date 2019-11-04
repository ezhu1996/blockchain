package com.example.blockchain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import java.util.ArrayList

class AuthorActivity : AppCompatActivity() {
    internal lateinit var buttonAddTitle: Button
    internal lateinit var editTextTitleName: EditText
    internal lateinit var seekBarRating: SeekBar
    internal lateinit var textViewRating: TextView
    internal lateinit var textViewAuthor: TextView
    internal lateinit var listViewTitles: ListView

    internal lateinit var databaseTitles: DatabaseReference

    internal lateinit var titles: MutableList<Title>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author)

        val intent = intent
        databaseTitles = FirebaseDatabase.getInstance().getReference("titles").child(intent.getStringExtra(DashboardActivity.AUTHOR_ID))

        buttonAddTitle = findViewById<View>(R.id.buttonAddTitle) as Button
        editTextTitleName = findViewById<View>(R.id.editTextName) as EditText
        seekBarRating = findViewById<View>(R.id.seekBarRating) as SeekBar
        textViewRating = findViewById<View>(R.id.textViewRating) as TextView
        textViewAuthor = findViewById<View>(R.id.textViewAuthor) as TextView
        listViewTitles = findViewById<View>(R.id.listViewTitles) as ListView

        titles = ArrayList()

        textViewAuthor.text = intent.getStringExtra(DashboardActivity.AUTHOR_NAME)

        seekBarRating.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                textViewRating.text = i.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        buttonAddTitle.setOnClickListener { saveTitle() }
    }

    override fun onStart() {
        super.onStart()

        databaseTitles.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                titles.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val title = postSnapshot.getValue<Title>(Title::class.java)
                    titles.add(title!!)
                }
                val titleListAdapter = TitleList(this@AuthorActivity, titles)
                listViewTitles.adapter = titleListAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun saveTitle() {
        val titleName = editTextTitleName.text.toString().trim { it <= ' ' }
        val rating = seekBarRating.progress
        if (!TextUtils.isEmpty(titleName)) {
            val id = (databaseTitles.push()).key.toString()
            val title = Title(id, titleName, rating)
            databaseTitles.child(id).setValue(title)
            Toast.makeText(this, "Title saved", Toast.LENGTH_LONG).show()
            editTextTitleName.setText("")
        } else {
            Toast.makeText(this, "Please enter title name", Toast.LENGTH_LONG).show()
        }
    }
}
