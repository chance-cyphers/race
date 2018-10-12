package com.pants.chance.race

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.callback.BaseCallback
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import android.content.Intent
import android.util.Log
import com.pants.chance.race.home.MainActivity
import kotlinx.android.synthetic.main.login.*


class LoginActivity : AppCompatActivity() {

    companion object {
        const val KEY_CLEAR_CREDENTIALS = "com.auth0.CLEAR_CREDENTIALS"
        const val EXTRA_ACCESS_TOKEN = "com.auth0.ACCESS_TOKEN"
        const val EXTRA_ID_TOKEN = "com.auth0.ID_TOKEN"
    }

    private lateinit var auth0: Auth0
    private lateinit var credentialsManager: SecureCredentialsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth0 = Auth0(this)
        auth0.isOIDCConformant = true
        credentialsManager = SecureCredentialsManager(this, AuthenticationAPIClient(auth0), SharedPreferencesStorage(this))

        if (intent.getBooleanExtra(KEY_CLEAR_CREDENTIALS, false)) {
            credentialsManager.clearCredentials()
        }

        if (!credentialsManager.hasValidCredentials()) {
            setContentView(R.layout.login)
            loginButton.setOnClickListener { login() }
        } else {
            credentialsManager.getCredentials(object : BaseCallback<Credentials, CredentialsManagerException> {
                override fun onFailure(error: CredentialsManagerException?) {
                    finish()
                }
                override fun onSuccess(payload: Credentials?) {
                    Log.i("token", payload?.idToken)
                    showNextActivity(payload)
                }
            })
        }
    }

    private fun showNextActivity(credentials: Credentials?) {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.putExtra(EXTRA_ACCESS_TOKEN, credentials?.accessToken)
        intent.putExtra(EXTRA_ID_TOKEN, credentials?.idToken)
        startActivity(intent)
        finish()
    }

    private fun login() {
        WebAuthProvider.init(auth0)
            .withScheme("demo")
            .withAudience(String.format("https://%s/userinfo", getString(R.string.com_auth0_domain)))
            .withScope("openid offline_access email profile read:current_user")
            .start(this@LoginActivity, object : AuthCallback {
                override fun onFailure(dialog: Dialog) {
                }

                override fun onFailure(exception: AuthenticationException) {
                }

                override fun onSuccess(credentials: Credentials) {
                    credentialsManager.saveCredentials(credentials)
                    showNextActivity(credentials)
                }
            })
    }


}