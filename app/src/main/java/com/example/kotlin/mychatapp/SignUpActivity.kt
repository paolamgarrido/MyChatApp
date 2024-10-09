package com.example.kotlin.mychatapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    lateinit var emailInput: EditText
    lateinit var passwordInput: EditText
    lateinit var signUpButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Assign the inputs to the variables
        emailInput = findViewById(R.id.email_input_sign)
        passwordInput = findViewById(R.id.passwd_input_sign)
        signUpButton = findViewById(R.id.signup_button)

        // Set onClickListener for the sign-up button
        signUpButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            // Validate input fields
            if (email.isEmpty() || password.isEmpty()) {
                showError("Please fill in both email and password.")
                return@setOnClickListener
            }

            if (!isValidEmail(email)) {
                showError("Invalid email format.")
                return@setOnClickListener
            }

            if (password.length < 6) {
                showError("Password should be at least 6 characters.")
                return@setOnClickListener
            }

            // Register the user using Firebase
            signUp(email, password)
        }
    }

    // Function to validate email format
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    // Function to show error message in a notification banner
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Function to register the user using Firebase
    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign-up successful, navigate back to login screen
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Close the sign-up screen
                } else {
                    // If sign-up fails, display a message to the user.
                    showError("Registration failed: ${task.exception?.message}")
                }
            }
    }
}
