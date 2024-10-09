package com.example.kotlin.mychatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    // Create the Variables for the inputs.
    lateinit var emailInput: EditText
    lateinit var passwordInput: EditText
    lateinit var loginButton: Button
    lateinit var signUpLink: TextView
    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Automatically log in if user is already signed in
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Assign the inputs to the variables.
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.passwd_input)
        loginButton = findViewById(R.id.login_button)
        signUpLink = findViewById(R.id.signup_link)

        // Set onClickListener for Login button
        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            // Validate email and password
            if (email.isEmpty() || password.isEmpty()) {
                showError("Please fill in both email and password.")
                return@setOnClickListener
            }
            if (!isValidEmail(email)) {
                showError("Invalid email format.")
                return@setOnClickListener
            }

            // Print the email and password to console (for debugging)
            Log.i("Test Credentials", "Email: $email and Password: $password")

            // Log in the user
            loginUser(email, password)
        }

        // Set onClickListener for Sign Up link to navigate to SignUpActivity
        signUpLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    // Function to validate email format
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    // Function to show error message in a notification banner
    private fun showError(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }

    // Function to log in the user using Firebase
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login successful, navigate to home activity or reload UI
                    showError("Login successful!")
                } else {
                    // If login fails, display a message to the user.
                    showError("Login failed: ${task.exception?.message}")
                }
            }
    }
}
