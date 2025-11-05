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

class RegisterActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var backToLoginButton: Button

    private val authViewModel by lazy { AppModule.provideAuthViewModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        registerButton = findViewById(R.id.registerButton)
        backToLoginButton = findViewById(R.id.backToLoginButton)

        setupObservers()

        setupClickListeners()
    }

    private fun setupObservers() {
        authViewModel.registerResult.observe(this, Observer { response ->
            if (response.success) {
                Toast.makeText(this, "Registro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
            }
        })

        authViewModel.isLoading.observe(this, Observer { isLoading ->
            registerButton.isEnabled = !isLoading
            backToLoginButton.isEnabled = !isLoading
        })

        authViewModel.error.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        })
    }

    private fun setupClickListeners() {
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            if (validateInput(name, email, password, confirmPassword)) {
                authViewModel.register(name, email, password)
            }
        }

        backToLoginButton.setOnClickListener { finish() }
    }

    private fun validateInput(name: String, email: String, password: String, confirmPassword: String): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            nameEditText.error = "Nome é obrigatório"
            isValid = false
        }

        if (email.isEmpty()) {
            emailEditText.error = "Email é obrigatório"
            isValid = false
        }

        if (password.isEmpty()) {
            passwordEditText.error = "Senha é obrigatória"
            isValid = false
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.error = "Confirmação de senha é obrigatória"
            isValid = false
        }

        if (password != confirmPassword) {
            confirmPasswordEditText.error = "As senhas não coincidem"
            isValid = false
        }

        return isValid
    }
}