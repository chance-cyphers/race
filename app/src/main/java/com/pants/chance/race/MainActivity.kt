package com.pants.chance.race

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.AuthCallback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth0: Auth0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth0 = Auth0(this)
        auth0.isOIDCConformant = true

        loginButton.setOnClickListener { login() }
        distanceTravelledButton.setOnClickListener {
            startActivity(Intent(this, DistanceTravelledActivity::class.java))
        }

    }

    private fun login() {
        WebAuthProvider.init(auth0)
            .withScheme("demo")
            .withAudience(
                String.format(
                    "https://%s/userinfo",
                    getString(R.string.com_auth0_domain)
                )
            )
            .start(this@MainActivity, object : AuthCallback {
                override fun onFailure(dialog: Dialog) {
                    runOnUiThread {
                        helloText.text = """failure: $dialog"""
                    }
                }

                override fun onFailure(exception: AuthenticationException) {
                    runOnUiThread {
                        helloText.text = """failure: ${exception.message}"""
                    }
                }

                override fun onSuccess(credentials: Credentials) {
                    runOnUiThread {
                        helloText.text = """success!: ${credentials.accessToken}"""
                    }
                }
            })
    }

}
