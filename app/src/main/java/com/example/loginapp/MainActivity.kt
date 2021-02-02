package com.example.loginapp
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.room.Room
import com.example.loginapp.database.LoginDatabase
import com.example.loginapp.database.LoginEntity
import com.example.loginapp.screens.forgotPassword.ForgotPasswordActivity
import com.example.loginapp.screens.home.HomeActivity
import com.example.loginapp.screens.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.*

@Suppress("SameParameterValue")
open class MainActivity : AppCompatActivity() {
    private var viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)
    open lateinit var db :LoginDatabase
    //Defining a scope for the coroutine to run
    override fun onDestroy() {
        super.onDestroy()
        viewModelJob.cancel()
    }
    lateinit var sharedPreferences:SharedPreferences
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences=getSharedPreferences(myPrefs, Context.MODE_APPEND)
        prepopulate(sharedPreferences)
        db=Room.databaseBuilder(this.application,LoginDatabase::class.java, "login_app_database").build()
        forgotPassword.setOnClickListener{
            startActivity(Intent(this@MainActivity, ForgotPasswordActivity::class.java))
        }
        createAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        loginButton.setOnClickListener {
            val x:Boolean= email.text.toString().errorEmail()
            val y:Boolean =password.text.toString().errorPassword()
            if( !x && !y ){
                uiScope.launch {
                    val foundMatch=checkBeforeLogin(email.text.toString(),password.text.toString())
                    if(foundMatch!=null){
                        val intent= Intent(this@MainActivity, HomeActivity::class.java)
                        val bundle=Bundle()
                        bundle.putString("email", foundMatch.email)
                        intent.putExtra("args", bundle)
                        rememberUser(sharedPreferences)
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
               if(x){
                       emailLoginLayout.error=getString(R.string.error_invalidEmail)
                       email.doOnTextChanged { _, _, _, _ ->
                           if(!email.text.toString().errorEmail()){
                               emailLoginLayout.error=null
                               email.error = null
                           }
                           else{
                               email.error=getString(R.string.error_invalidEmail)
                               emailLoginLayout.error=null
                           }
                       }
                   }
                else if(y){
                   password_LoginLayout.error=getString(R.string.error_invalidEmail)
                   password.doOnTextChanged { _, _, _, _ ->
                       if(!password.text.toString().errorEmail()){
                           password_LoginLayout.error=null
                           password.error = null
                       }
                       else{
                           password_LoginLayout.error=getString(R.string.error_invalidEmail)
                       }
                   }
               }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        prepopulate(sharedPreferences)
    }

    private fun prepopulate(sharedPreferences: SharedPreferences) {
        val prefsEmail:String = sharedPreferences.getString("email","").toString()
        val prefsPassword:String= sharedPreferences.getString("password","").toString()
        if(prefsEmail.isNotEmpty() && prefsPassword.isNotEmpty()){
            email.setText(prefsEmail)
            password.setText(prefsPassword)
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun rememberUser(sharedPreferences: SharedPreferences){
        val sh= sharedPreferences.edit()
        if(rememberMe.isChecked){
            sh.putString("email",email.text.toString())
            sh.putString("password",password.text.toString())
            sh.apply()
        }
        else{
            sh.remove("email")
            sh.remove("password")
            sh.apply()
        }
    }

    @SuppressLint("CommitPrefEdits")
    fun clearSharedPreferences(sharedPreferences: SharedPreferences){
        val sh= sharedPreferences.edit()
        sh.clear()
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
}