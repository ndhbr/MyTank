package de.ndhbr.mytank.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import de.ndhbr.mytank.ui.home.OverviewActivity
import de.ndhbr.mytank.R
import de.ndhbr.mytank.uitilities.InjectorUtils
import de.ndhbr.mytank.viewmodels.AuthViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val factory = InjectorUtils.provideAuthViewModelFactory()
        val viewModel =
            ViewModelProvider(this@RegisterActivity, factory).get(AuthViewModel::class.java)

        initializeUi(viewModel)
    }

    private fun initializeUi(viewModel: AuthViewModel) {
        btn_register.setOnClickListener {
            val email = et_register_email.text.toString().trim { it <= ' ' }
            val password = et_register_email.text.toString().trim { it <= ' ' }

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