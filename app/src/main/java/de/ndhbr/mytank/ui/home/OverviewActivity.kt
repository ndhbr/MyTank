package de.ndhbr.mytank.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.ndhbr.mytank.R
import de.ndhbr.mytank.ui.auth.LoginActivity
import kotlinx.android.synthetic.main.activity_overview.*

class OverviewActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        // Intent extras
        val userId = intent.getStringExtra("user_id")

        // Initialize Firebase Auth
        auth = Firebase.auth

        tv_user_id.text = "USER ID: $userId"

        btn_logout.setOnClickListener {
            auth.signOut()

            // Send user back
            startActivity(Intent(this@OverviewActivity, LoginActivity::class.java))
            finish()
        }
    }
}