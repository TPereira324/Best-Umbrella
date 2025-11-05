package pt.iade.ei.bestumbrella1.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import pt.iade.ei.bestumbrella1.MainActivity
import pt.iade.ei.bestumbrella1.R
import pt.iade.ei.bestumbrella1.di.AppModule
import pt.iade.ei.bestumbrella1.viewmodels.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    private val authViewModel by lazy { AppModule.provideAuthViewModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)

        setupObservers()

        setupClickListeners()
    }

    private fun setupObservers() {
        authViewModel.loginResult.observe(this, Observer { response ->
            if (response.success) {
                Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
            }
        })

        authViewModel.isLoading.observe(this, Observer { isLoading ->
            loginButton.isEnabled = !isLoading
            registerButton.isEnabled = !isLoading
        })

        authViewModel.error.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        })
    }

    private fun setupClickListeners() {
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateInput(email, password)) {

                authViewModel.login(email, password)
            }
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            emailEditText.error = "Email é obrigatório"
            isValid = false
        }

        if (password.isEmpty()) {
            passwordEditText.error = "Senha é obrigatória"
            isValid = false
        }

        return isValid
    }
}