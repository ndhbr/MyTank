package de.ndhbr.mytank.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import de.ndhbr.mytank.ui.home.OverviewActivity
import de.ndhbr.mytank.databinding.ActivityRegisterBinding
import de.ndhbr.mytank.utilities.AlarmUtils
import de.ndhbr.mytank.utilities.InjectorUtils
import de.ndhbr.mytank.viewmodels.AuthViewModel

class RegisterActivity : AppCompatActivity() {

    // View binding
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = InjectorUtils.provideAuthViewModelFactory()
        val viewModel =
            ViewModelProvider(this@RegisterActivity, factory).get(AuthViewModel::class.java)

        initializeUi(viewModel)
    }

    private fun initializeUi(viewModel: AuthViewModel) {
        // Login button
        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        // Register button
        binding.btnRegister.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString().trim { it <= ' ' }
            val password = binding.etRegisterPassword.text.toString().trim { it <= ' ' }

            when {
                TextUtils.isEmpty(email) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter a valid email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(password) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter a strong password",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    viewModel.register(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser = task.result!!.user!!

                                Toast.makeText(
                                    this@RegisterActivity,
                                    "You are registered successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Send to main screen
                                val intent =
                                    Intent(this@RegisterActivity, OverviewActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", firebaseUser.uid)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }
}