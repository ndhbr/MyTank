package de.ndhbr.mytank.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.ViewModelProvider
import de.ndhbr.mytank.R
import de.ndhbr.mytank.databinding.ActivityForgotPasswordBinding
import de.ndhbr.mytank.utilities.InjectorUtils
import de.ndhbr.mytank.utilities.ToastUtilities
import de.ndhbr.mytank.viewmodels.AuthViewModel

class ForgotPasswordActivity : AppCompatActivity() {

    // View binding
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.forgot_password)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = InjectorUtils.provideAuthViewModelFactory()
        val viewModel = ViewModelProvider(
            this@ForgotPasswordActivity, factory
        ).get(AuthViewModel::class.java)

        initializeUi(viewModel)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initializeUi(viewModel: AuthViewModel) {
        binding.btnForgotPassword.setOnClickListener {
            val email = binding.etLoginEmail.text.toString().trim { it <= ' ' }

            when {
                TextUtils.isEmpty(email) -> {
                    ToastUtilities.showShortToast(
                        this,
                        resources.getString(
                            R.string.form_error_forgot_password_email_input
                        )
                    )
                }

                else -> {
                    viewModel.sendPasswordResetMail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                ToastUtilities.showShortToast(
                                    this,
                                    getString(R.string.password_reset_mail_sent)
                                )

                                finish()
                            } else {
                                ToastUtilities.showLongToast(
                                    this,
                                    getString(
                                        R.string.is_an_account_with_this_email_really_existing)
                                )
                            }
                        }
                }
            }
        }
    }
}