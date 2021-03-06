package de.ndhbr.mytank.ui.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import de.ndhbr.mytank.R
import de.ndhbr.mytank.databinding.ActivityLoginBinding
import de.ndhbr.mytank.ui.home.OverviewActivity
import de.ndhbr.mytank.utilities.AlarmUtils
import de.ndhbr.mytank.utilities.InjectorUtils
import de.ndhbr.mytank.utilities.ToastUtilities
import de.ndhbr.mytank.viewmodels.AuthViewModel

class LoginActivity : AppCompatActivity() {

    // View binding
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        supportActionBar?.title = getString(R.string.ab_login)
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

        initPreDrawListener(viewModel)
        buildPasswordResetButton()
        buildLoginButton(viewModel)
        buildLoginAnonymouslyButton(viewModel)
        buildRegisterButton()
        buildGoogleLoginButton(viewModel)
    }

    // Password forgot button
    private fun buildPasswordResetButton() {
        binding.tvPasswordForgot.setOnClickListener {
            val intent =
                Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    // Login button
    private fun buildLoginButton(viewModel: AuthViewModel) {
        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString().trim { it <= ' ' }
            val password = binding.etLoginPassword.text.toString().trim { it <= ' ' }

            when {
                TextUtils.isEmpty(email) -> {
                    ToastUtilities.showShortToast(
                        this@LoginActivity,
                        getString(R.string.please_enter_your_email)
                    )
                }

                TextUtils.isEmpty(password) -> {
                    ToastUtilities.showShortToast(
                        this@LoginActivity,
                        getString(R.string.please_enter_your_password)
                    )
                }

                else -> {
                    viewModel.login(email, password)
                        .addOnCompleteListener { result -> handleAuthResult(result) }
                }
            }
        }
    }

    // Without registration button
    private fun buildLoginAnonymouslyButton(viewModel: AuthViewModel) {
        val dialogClickListener =
            DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        viewModel.loginAnonymously()
                            .addOnCompleteListener { result -> handleAuthResult(result) }
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {}
                }
            }

        binding.btnAnonymous.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder
                .setTitle(R.string.are_you_sure)
                .setMessage(getString(R.string.login_anonymously_alert_warning))
                .setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.cancel, dialogClickListener)
                .show()
        }
    }

    // Auth Result handler (for login)
    private fun handleAuthResult(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            // Send to main screen
            val intent =
                Intent(this@LoginActivity, OverviewActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            ToastUtilities.showLongToast(
                this@LoginActivity,
                task.exception!!.message.toString()
            )
        }
    }

    // Register button
    private fun buildRegisterButton() {
        binding.tvRegister.setOnClickListener {
            val intent = Intent(
                this,
                RegisterActivity::class.java
            )

            startActivity(intent)
        }
    }

    // Google login button
    private fun buildGoogleLoginButton(viewModel: AuthViewModel) {
        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)

                    try {
                        val account = task.getResult(ApiException::class.java)!!
                        viewModel.loginWithGoogle(account.idToken!!)
                            .addOnCompleteListener { authResult -> handleAuthResult(authResult) }
                    } catch (e: ApiException) {
                        ToastUtilities.showShortToast(this,
                            "Error! Please try again later")
                    }
                }
            }

        binding.cvGoogleLogin.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.request_id_token))
                .requestEmail()
                .build()
            val signInClient = GoogleSignIn.getClient(this, gso)
            val signInIntent: Intent = signInClient.signInIntent

            resultLauncher.launch(signInIntent)
        }
    }

    // Set up OnPreDrawListener to redirect directly to the home
    // screen; if the user is already logged in
    private fun initPreDrawListener(viewModel: AuthViewModel) {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    content.viewTreeObserver.removeOnPreDrawListener(this)

                    // Check if the initial data is ready.
                    return if (viewModel.isLoggedIn()) {
                        // The user is logged in; jump to OverviewActivity
                        val intent = Intent(this@LoginActivity, OverviewActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        true
                    } else {
                        // The user is not logged in; just display
                        true
                    }
                }
            }
        )
    }
}