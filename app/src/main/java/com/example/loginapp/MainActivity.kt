package com.example.loginapp
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.example.loginapp.database.LoginEntity
import com.example.loginapp.screens.forgotPassword.ForgotPasswordActivity
import com.example.loginapp.screens.home.HomeActivity
import com.example.loginapp.screens.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

open class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListenerOnWidgets()
        prepopulate(sharedPreferences)
    }
    private fun setListenerOnWidgets() {
        forgotPassword?.setOnClickListener{
            startActivity(Intent(this@MainActivity, ForgotPasswordActivity::class.java))
        }
        createAccount?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        loginButton?.setOnClickListener {
            val isAValidEmail:Boolean= email.text.toString().isAValidEmail()
            val isAValidPassword:Boolean =password.text.toString().isAValidPassword()
            if( isAValidEmail && isAValidPassword ){
                uiScope.launch {
                    val foundMatch=checkBeforeLogin(email.text.toString(),password.text.toString())
                    if(foundMatch!=null){
                        val intent= Intent(this@MainActivity, HomeActivity::class.java)
                        val bundle=Bundle()
                        bundle.putString(getString(R.string.bundleArgEmail), foundMatch.email)
                        intent.putExtra(getString(R.string.bundleKey), bundle)
                        rememberUser(sharedPreferences)
                        clearErrorOnFields()
                        clearFields()
                        showDialog({ _, _ -> startActivity(intent)},{ dialog, _ -> dialog.cancel() },
                                R.string.dialog_login_title,R.string.loginSuccessful,
                                R.string.dialogPositive,R.string.dialogNegative)?.show()
                    }
                    else{
                        showDialog({ dialog, _ -> dialog.cancel() },
                                R.string.dialog_login_title,R.string.invalidCredentials,
                                R.string.dialogPositive)?.show()
                    }
                }
            }
            else{
                if(!isAValidEmail){
                    emailLoginLayout.error=getString(R.string.error_invalidEmail)
                }
                else if(!isAValidPassword){
                    password_LoginLayout.error=getString(R.string.error_password)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        prepopulate(sharedPreferences)
    }

    private fun prepopulate(sharedPreferences: SharedPreferences) {
        val prefsEmail:String = sharedPreferences.getString(getString(R.string.sharedPrefsEmail),"").toString()
        val prefsPassword:String= sharedPreferences.getString(getString(R.string.sharedPrefsPassword),"").toString()
        if(prefsEmail.isNotEmpty() && prefsPassword.isNotEmpty()){
            email.setText(prefsEmail)
            password.setText(prefsPassword)
        }
    }

    private fun rememberUser(sharedPreferences: SharedPreferences){
        val sh= sharedPreferences.edit()
        if(rememberMe.isChecked){
            sh.putString(getString(R.string.sharedPrefsEmail),email.text.toString())
            sh.putString(getString(R.string.sharedPrefsPassword),password.text.toString())
            sh.apply()
        }
        else{
            sh.remove(getString(R.string.sharedPrefsEmail))
            sh.remove(getString(R.string.sharedPrefsPassword))
            sh.apply()
        }
    }

    private suspend fun checkBeforeLogin(email:String, password:String):LoginEntity?{
         return withContext(Dispatchers.IO){
             val res:LoginEntity?=db.loginDatabaseDao.checkCredentials(email,password)
             res
         }
    }

    private fun clearFields(){
        email.text?.clear()
        password.text?.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelJob.cancel()
    }

    private fun clearErrorOnFields(){
        emailLoginLayout.error=null
        password_LoginLayout.error=null
    }
}