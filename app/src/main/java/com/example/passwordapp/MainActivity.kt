package com.example.passwordapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.passwordapp.databinding.ActivityMainBinding
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityMainBinding

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        // Apply theme from settings
        val sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isDarkMode = sharedPrefs.getBoolean("dark_mode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding =
            ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Check if app lock is enabled
        val isAppLockEnabled = sharedPrefs.getBoolean("app_lock", false)
        if (isAppLockEnabled) {
            showBiometricPrompt()
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(
                R.id.nav_host_fragment
            ) as NavHostFragment

        val navController =
            navHostFragment.navController

        binding.bottomNavigation
            .setupWithNavController(
                navController
            )
    }

    private fun showBiometricPrompt() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    finish() // Close app if authentication fails or is cancelled
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Continue to app
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("App Locked")
            .setSubtitle("Authenticate to unlock PasswordApp")
            .setNegativeButtonText("Exit")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}