package de.ndhbr.mytank.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import de.ndhbr.mytank.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeUi()
    }

    private fun initializeUi() {
        btn_login.setOnClickListener {
            val email = et_login_email.text.toString().trim { it <= ' ' }
            val password = et_login_password.text.toString().trim { it <= ' ' }

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
                    println("Moin moin")
                }
            }
        }
    }
}