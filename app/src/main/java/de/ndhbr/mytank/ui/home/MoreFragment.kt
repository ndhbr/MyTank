package de.ndhbr.mytank.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.ndhbr.mytank.R
import de.ndhbr.mytank.databinding.FragmentMoreBinding
import de.ndhbr.mytank.ui.auth.LoginActivity
import de.ndhbr.mytank.utilities.BrightnessUtils
import de.ndhbr.mytank.utilities.InjectorUtils
import de.ndhbr.mytank.utilities.ToastUtilities
import de.ndhbr.mytank.viewmodels.AuthViewModel

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

        with(binding) {
            // Brightness state
            if (spBrightnessState.adapter == null) {
                val brightnessUtils = BrightnessUtils()
                val arrayAdapter = ArrayAdapter(
                    context!!,
                    android.R.layout.simple_spinner_dropdown_item,
                    BrightnessUtils.BrightnessState.values()
                )
                spBrightnessState.adapter = arrayAdapter
                spBrightnessState.setSelection(
                    brightnessUtils.getBrightnessState(context!!).ordinal
                )
                spBrightnessState.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val state = BrightnessUtils.BrightnessState.values()[position]

                        // If under android version 10 -> fallback dark mode
                        if (state == BrightnessUtils.BrightnessState.System &&
                            android.os.Build.VERSION.SDK_INT <
                            android.os.Build.VERSION_CODES.Q
                        ) {
                            ToastUtilities.showLongToast(
                                context!!,
                                getString(R.string.error_brightness_system_follow_under_android_q)
                            )
                            spBrightnessState.setSelection(
                                BrightnessUtils.BrightnessState.Dark.ordinal
                            )

                        } else {
                            brightnessUtils.setBrightnessState(
                                context!!,
                                BrightnessUtils.BrightnessState.values()[position]
                            )
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                }
            }

            // User state
            tvLoggedInAs.text =
                String.format(resources.getString(R.string.logged_in_as), viewModel.isLoggedInAs())
            btnLogout.setOnClickListener {
                viewModel.logout()

                // Send to login screen
                val intent = Intent(
                    activity,
                    LoginActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
    }
}