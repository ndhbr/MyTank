package de.ndhbr.mytank.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import de.ndhbr.mytank.R
import de.ndhbr.mytank.data.Database
import de.ndhbr.mytank.databinding.FragmentMoreBinding
import de.ndhbr.mytank.ui.auth.LoginActivity
import de.ndhbr.mytank.utilities.InjectorUtils
import de.ndhbr.mytank.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

class MoreFragment : Fragment() {

    // View binding
    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        activity?.title = getString(R.string.ab_more)

        initializeUi()

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initializeUi() {
        val factory = InjectorUtils.provideAuthViewModelFactory()
        val viewModel =
            ViewModelProvider(this@MoreFragment, factory).get(AuthViewModel::class.java)

        binding.btnLogout.setOnClickListener {
            viewModel.logout()

            // Send to login screen
            val intent = Intent(
                activity,
                LoginActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        binding.btnTest.setOnClickListener {
            val database = Database.getInstance()
            viewModel.viewModelScope.launch {
                Log.d("DB", database.itemAlarmDao.getCurrentAlarmsList().toString())
            }
        }
    }
}