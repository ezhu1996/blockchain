package com.example.blockchain

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import java.util.ArrayList

class DashboardActivity : AppCompatActivity() {

    internal lateinit var editTextName: EditText
    internal lateinit var spinnerCountry: Spinner
    internal lateinit var buttonAddAuthor: Button
    internal lateinit var listViewAuthors: ListView

    internal lateinit var authors: MutableList<Author>

    internal lateinit var databaseAuthors: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        databaseAuthors = FirebaseDatabase.getInstance().getReference("authors").child(intent.getStringExtra(UserID))

        editTextName = findViewById<View>(R.id.editTextName) as EditText
        spinnerCountry = findViewById<View>(R.id.spinnerCountry) as Spinner
        listViewAuthors = findViewById<View>(R.id.listViewAuthors) as ListView

        buttonAddAuthor = findViewById<View>(R.id.buttonAddAuthor) as Button

        authors = ArrayList()


        buttonAddAuthor.setOnClickListener {
            //calling the method addArtist()
            //the method is defined below
            //this method is actually performing the write operation
            addAuthor()
        }

        listViewAuthors.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, i, _ ->
                val author = authors[i]

                val intent = Intent(applicationContext, AuthorActivity::class.java)

                intent.putExtra(AUTHOR_ID, author.authorId)
                intent.putExtra(AUTHOR_NAME, author.authorName)
                startActivity(intent)
            }

        listViewAuthors.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { _, _, i, _ ->
                val author = authors[i]
                showUpdateDeleteDialog(author.authorId, author.authorName)
                true
            }
    }

    @SuppressLint("InflateParams")
    private fun showUpdateDeleteDialog(authorId: String, authorName: String) {

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)

        val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)
        val spinnerCountry = dialogView.findViewById<Spinner>(R.id.spinnerCountry)
        val buttonUpdate = dialogView.findViewById<Button>(R.id.buttonUpdateAuthor)
        val buttonDelete = dialogView.findViewById<Button>(R.id.buttonDeleteAuthor)

        dialogBuilder.setTitle(authorName)
        val b = dialogBuilder.create()
        b.show()

        buttonUpdate.setOnClickListener {
            // Retrieve Values, Call Update Function
            val name = editTextName.text.toString().trim { it <= ' ' }
            val country = spinnerCountry.selectedItem.toString()
            if (!TextUtils.isEmpty(name)) {
                updateAuthor(authorId, intent.getStringExtra(UserID), name, country)
                b.dismiss()
            }
        }

        buttonDelete.setOnClickListener {
            // get Author ID call Delete Function
            deleteAuthor(authorId)
            b.dismiss()
        }
    }

    private fun updateAuthor(id: String, uid: String, name: String, country: String): Boolean {
        //getting the specified author reference
        //updating author
        val author = Author(id, name, country)
        databaseAuthors.child(id).setValue(author)
        Toast.makeText(applicationContext, "Author successfully updated", Toast.LENGTH_LONG).show()
        return true
    }

    private fun deleteAuthor(id: String): Boolean {
        //getting the specified author reference
        //removing author
        databaseAuthors.child(id).removeValue()
        //getting the titles reference for the specified author
        //removing all titles
        FirebaseDatabase.getInstance().getReference("titles").child(id).removeValue()
        Toast.makeText(applicationContext, "Author successfully deleted", Toast.LENGTH_LONG).show()

        return true
    }

    override fun onStart() {
        super.onStart()
        databaseAuthors.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //clearing the previous artist list
                authors.clear()
                // getting authors only for the Current User
                //iterating through all the nodes
                for (postSnapshot in dataSnapshot.children) {
                    val author = postSnapshot.getValue<Author>(Author::class.java)
                    authors.add(author!!)
                }
                //creating adapter using AuthorList
                val authorListAdapter = AuthorList(this@DashboardActivity, authors)
                //attaching adapter to the listview
                listViewAuthors.adapter = authorListAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun addAuthor() {
        //getting the values to save
        val name = editTextName.text.toString()
        val country = spinnerCountry.selectedItem.toString()
        //checking if the value is provided
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(applicationContext, "Please enter author...", Toast.LENGTH_LONG).show()
            return
        }
        //getting a unique id using push().getKey() method
        val id = databaseAuthors.push().key
        //it will create a unique id and we will use it as the Primary Key for our Author
        //creating an Author Object
        val author = Author(id.toString(), name, country)
        //Saving the Author
        databaseAuthors.child(id.toString()).setValue(author)
        //setting edittext to blank again
        editTextName.setText("")
        //displaying a success toast
        Toast.makeText(this, "Author saved", Toast.LENGTH_LONG).show()
        //if the value is not given displaying a toast

    }

    companion object {
        val AUTHOR_NAME = "com.example.tesla.myhomelibrary.authorname"
        val AUTHOR_ID = "com.example.tesla.myhomelibrary.authorid"
        val UserID = "com.example.tesla.myhomelibrary.UID"
    }
}



