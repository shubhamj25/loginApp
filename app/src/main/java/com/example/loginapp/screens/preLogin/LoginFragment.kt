package com.example.loginapp.screens.preLogin
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.loginapp.*
import com.example.loginapp.database.LoginDatabase
import com.example.loginapp.database.LoginEntity
import com.example.loginapp.databinding.FragmentLoginBinding
import com.example.loginapp.screens.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.*

class LoginFragment : Fragment() {
    private lateinit var sharedPreferences:SharedPreferences
    private lateinit var db:LoginDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiScope= CoroutineScope(Dispatchers.Main +  Job())
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        sharedPreferences= getSharedPreferenceInstance(requireNotNull(this.activity).application)
        db= getDatabaseInstance(requireNotNull(this.activity).application)
        val binding:FragmentLoginBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false)
        setListenerOnWidgets(binding)
        return binding.root
    }
    private fun setListenerOnWidgets(binding: FragmentLoginBinding) {
        binding.forgotPassword.setOnClickListener{
            Navigation.findNavController(constraintLayout).navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
        binding.createAccount.setOnClickListener {
            Navigation.findNavController(constraintLayout).navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.loginButton.setOnClickListener {
            val isAValidEmail:Boolean= email.text.toString().isAValidEmail()
            val isAValidPassword:Boolean =password.text.toString().isAValidPassword()
            Log.i("FunctionEcec","Login Exucutesdcs svsdvsdv")
            if( isAValidEmail && isAValidPassword ){
                Log.i("FunctionEcec","entered if condition")
                uiScope.launch {
                    Log.i("FunctionEcec","entered uiScope")
                    val foundMatch=checkBeforeLogin(email.text.toString(),password.text.toString())
                    if(foundMatch!=null){
                        val intent= Intent(requireActivity().application, HomeActivity::class.java)
                        val bundle=Bundle()
                        bundle.putString(getString(R.string.bundleArgEmail), foundMatch.email)
                        intent.putExtra(getString(R.string.bundleKey), bundle)
                        rememberUser(sharedPreferences)
                        clearErrorOnFields()
                        clearFields()
                        requireActivity().showDialog({ _, _ -> startActivity(intent)},{ dialog, _ -> dialog.cancel() },
                            R.string.dialog_login_title,R.string.loginSuccessful,
                            R.string.dialogPositive,R.string.dialogNegative)?.show()
                    }
                    else{
                        requireActivity().showDialog({ dialog, _ -> dialog.cancel() },
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

    private suspend fun checkBeforeLogin(email:String, password:String): LoginEntity?{
        return withContext(Dispatchers.IO){
            val res: LoginEntity?=db.loginDatabaseDao.checkCredentials(email,password)
            res
        }
    }

    private fun clearFields(){
        email.text?.clear()
        password.text?.clear()
    }

    private fun clearErrorOnFields(){
        emailLoginLayout.error=null
        password_LoginLayout.error=null
    }
}