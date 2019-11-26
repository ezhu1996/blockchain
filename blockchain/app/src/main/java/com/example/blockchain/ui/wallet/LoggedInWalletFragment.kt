package com.example.blockchain.ui.wallet


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.blockchain.MainActivity
import com.example.blockchain.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoggedInWalletFragment : Fragment() {
    lateinit var rootView: View
    lateinit var accountInfo: SharedPreferences
    lateinit var signoutBtn: Button
    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: FirebaseDatabase
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var editor: SharedPreferences.Editor
    lateinit var email: String
    lateinit var addresses: MutableList<String>
    lateinit var myListView: ListView
    lateinit var newAddress: Button


    @Override
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        rootView = inflater.inflate(R.layout.fragment_logged_in_wallet, container, false)

        initialize()

        // event listener for logging out
        signoutBtn.setOnClickListener {
            signoutUserAccount()
        }

        // event listener for add new address
        newAddress.setOnClickListener {
            addNewAddress()
        }

        // event listener for delete
        myListView.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { _, _, i, _ ->
                val address = addresses[i]
                showUpdateDeleteDialog(address)
                true
            }
        return rootView
    }

    private fun addNewAddress() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Enter BTC Address")
        builder.setMessage("Only BTC Supported")

        val dialogView = activity!!.layoutInflater.inflate(R.layout.address_dialog, null)
        builder.setView(dialogView)
        val editText = dialogView.findViewById<EditText>(R.id.address)


        // Set up the buttons
        builder.setPositiveButton("Add") { _, _ ->
            val address = editText.text.toString()
            //checking if the value is provided
            if (TextUtils.isEmpty(address)) {
                Toast.makeText(activity!!, "Please enter address...", Toast.LENGTH_LONG).show()
            } else {
                //Saving the address
                mDatabaseReference.child(address).setValue(true)

                //displaying a success toast
                Toast.makeText(activity!!, "Address saved", Toast.LENGTH_LONG).show()
            }
        }

        builder.setNegativeButton("Cancel") { _, _ ->

        }

        val b = builder.create()
        b.show()
    }

    private fun signoutUserAccount() {
        mAuth.signOut()
        val accountInfo: SharedPreferences =
            activity!!.getSharedPreferences("accountInfo", Context.MODE_PRIVATE)

        // read in firebase items and put into sharedpreferences
        val editor: SharedPreferences.Editor = accountInfo.edit()
        editor.putBoolean("loggedIn", false)
        editor.putString("email", null)
        editor.apply()

        // go back to main activity
        activity!!.startActivity(Intent(activity!!, MainActivity::class.java))
    }

    private fun initialize() {
        accountInfo = activity!!.getSharedPreferences("accountInfo", Context.MODE_PRIVATE)

        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        email = mAuth.currentUser!!.uid
        mDatabaseReference = mDatabase.getReference("users").child(email)
        myListView = rootView.findViewById(R.id.addresses)
        addresses = ArrayList()
        editor = accountInfo.edit()
        signoutBtn = rootView.findViewById(R.id.signout)
        mDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //clearing the previous addresses list
                addresses.clear()
                // getting addresses only for the current email
                // iterating through all the nodes
                for (postSnapshot in dataSnapshot.children) {
                    val address = postSnapshot.key
                    addresses.add(address!!)
                }
                //creating adapter using AddressList
                val addressListAdapter = AddressList(activity!!, addresses)
                //attaching adapter to the listview
                myListView.adapter = addressListAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
        newAddress = rootView.findViewById(R.id.new_wallet)
    }

    private fun deleteAddress(address: String): Boolean {
        //getting the specified author reference
        //removing address
        mDatabaseReference.child(address).removeValue()
        return true
    }

    private fun showUpdateDeleteDialog(address: String) {
        val dialogBuilder = AlertDialog.Builder(activity!!)
        val dialogView = activity!!.layoutInflater.inflate(R.layout.update_delete, null)
        dialogBuilder.setView(dialogView)

        val dialog = dialogView.findViewById<TextView>(R.id.confirm)
        dialog.text = Editable.Factory.getInstance()
            .newEditable("Confirm Deletion of Address: $address")

        val buttonDelete = dialogView.findViewById<Button>(R.id.deleteAddress)

        dialogBuilder.setTitle("Delete Address")
        val b = dialogBuilder.create()
        b.show()

        buttonDelete.setOnClickListener {
            deleteAddress(address)
            b.dismiss()
        }
    }
}