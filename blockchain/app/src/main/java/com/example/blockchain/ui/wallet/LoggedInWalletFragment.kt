package com.example.blockchain.ui.wallet


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
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
import java.net.URL

class LoggedInWalletFragment : Fragment() {
    private lateinit var rootView: View
    private lateinit var accountInfo: SharedPreferences
    private lateinit var signoutBtn: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var email: String
    private lateinit var addresses: MutableList<String>
    private lateinit var myListView: ListView
    private lateinit var newAddress: Button


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

        // event listener for selecting address
        myListView.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, _ ->
                // unhighlight previous selected
                for (j in 0..adapterView.childCount) {
                    val child = adapterView.getChildAt(j)
                    child?.setBackgroundColor(Color.TRANSPARENT)
                }

                // update selected Address
                val editor: SharedPreferences.Editor = accountInfo.edit()
                editor.putString("selectedAddress", addresses[i])
                editor.apply()
                // set selected
                view.setBackgroundColor(Color.parseColor("#9e7367"))
            }
        return rootView
    }

    @SuppressLint("InflateParams")
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
                Thread {
                    try {
                        // only update if valid address
                        URL("https://blockchain.info/q/addressbalance/$address").readText()

                        //Saving the address
                        mDatabaseReference.child(address).setValue(true)

                        activity!!.runOnUiThread {
                            // displaying a success toast
                            Toast.makeText(activity!!, "Address saved!", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        activity!!.runOnUiThread {
                            Toast.makeText(
                                activity!!,
                                "Invalid Address: $address!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }.start()
            }
        }

        builder.setNegativeButton("Cancel") { _, _ ->

        }

        val b = builder.create()
        b.show()
    }

    private fun signoutUserAccount() {
        mAuth.signOut()
        val editor: SharedPreferences.Editor = accountInfo.edit()
        editor.putString("selectedAddress", "")
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
                val selected = accountInfo.getString("selectedAddress", "")
                //creating adapter using AddressList
                val addressListAdapter = AddressList(activity!!, addresses, selected!!)
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

    @SuppressLint("InflateParams")
    private fun showUpdateDeleteDialog(address: String) {
        val dialogBuilder = AlertDialog.Builder(activity!!)
        val dialogView = activity!!.layoutInflater.inflate(R.layout.update_delete, null)
        dialogBuilder.setView(dialogView)

        val dialog = dialogView.findViewById<TextView>(R.id.confirm)
        dialog.text = Editable.Factory.getInstance()
            .newEditable("Confirm Deletion of Address: $address")

        val buttonDelete = dialogView.findViewById<Button>(R.id.deleteAddress)
        val buttonCancel = dialogView.findViewById<Button>(R.id.cancel)

        dialogBuilder.setTitle("Delete Address")
        val b = dialogBuilder.create()
        b.show()

        buttonDelete.setOnClickListener {
            deleteAddress(address)
            b.dismiss()
        }

        buttonCancel.setOnClickListener {
            b.dismiss()
        }
    }
}