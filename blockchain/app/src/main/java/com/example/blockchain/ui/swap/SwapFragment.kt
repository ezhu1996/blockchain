package com.example.blockchain.ui.swap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.blockchain.R

class SwapFragment : Fragment() {

    private lateinit var swapViewModel: SwapViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        swapViewModel =
            ViewModelProviders.of(this).get(SwapViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_swap, container, false)
        val textView: TextView = root.findViewById(R.id.text_swap)
        swapViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}