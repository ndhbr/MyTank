package de.ndhbr.mytank.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import de.ndhbr.mytank.databinding.ActivityLoginBinding
import de.ndhbr.mytank.ui.home.OverviewActivity
import de.ndhbr.mytank.utilities.AlarmUtils
import de.ndhbr.mytank.utilities.InjectorUtils
import de.ndhbr.mytank.viewmodels.AuthViewModel

class LoginActivity : AppCompatActivity() {

    // View binding
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAlarm()
        initializeUi()
    }

    private fun initAlarm() {
        val alarmUtils = AlarmUtils(this);
        if (!alarmUtils.isAlarmUpAndRunning()) {
            alarmUtils.setAlarm()
        }
    }

    private fun initializeUi() {
        val factory = InjectorUtils.provideAuthViewModelFactory()
        val viewModel =
            ViewModelProvider(this@LoginActivity, factory).get(AuthViewModel::class.java)

        // Set up OnPreDrawListener to redirect directly to the home
        // screen; if the user is already logged in
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object: ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check if the initial data is ready.
                    return if (viewModel.isLoggedIn()) {
                        // The user is logged in; jump to OverviewActivity
                        val intent = Intent(this@LoginActivity, OverviewActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)

                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The user is not logged in; just display
                        true
                    }
                }
            }
        )

        // Register button
        binding.tvRegister.setOnClickListener {
            val intent = Intent(
                this,
                RegisterActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

            startActivity(intent)
        }

        // Login button
        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString().trim { it <= ' ' }
            val password = binding.etLoginPassword.text.toString().trim { it <= ' ' }

            when {
                TextUtils.isEmpty(email) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter your email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(password) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter your password",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    viewModel.login(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser = task.result!!.user!!

                                Toast.makeText(
                                    this@LoginActivity,
                                    "You are signed in successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Send to main screen
                                val intent =
                                    Intent(this@LoginActivity, OverviewActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", firebaseUser.uid)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
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