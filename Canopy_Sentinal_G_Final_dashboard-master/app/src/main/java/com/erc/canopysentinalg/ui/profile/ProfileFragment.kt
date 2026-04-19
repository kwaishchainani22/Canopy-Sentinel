package com.erc.canopysentinalg.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.erc.canopysentinalg.R
import com.erc.canopysentinalg.ui.viewmodel.AuthViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class ProfileFragment : Fragment() {
    private val authViewModel: AuthViewModel by viewModels({ requireActivity() })
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val btnSignOut = view.findViewById<MaterialButton>(R.id.btnSignOut)
        val tvUserName = view.findViewById<MaterialTextView>(R.id.tvUserName)
        val tvUserEmail = view.findViewById<MaterialTextView>(R.id.tvUserEmail)
        
        authViewModel.currentUser.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                tvUserName.text = it.name ?: "Guest User"
                tvUserEmail.text = it.email ?: "Guest Mode"
            }
        })
        
        btnSignOut.setOnClickListener {
            authViewModel.signOut()
            // Navigate to auth activity
        }
    }
}
