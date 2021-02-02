package com.example.loginapp.screens.register
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.room.Room
import com.example.loginapp.*
import com.example.loginapp.database.LoginDatabase
import com.example.loginapp.database.LoginEntity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.*

@Suppress("SameParameterValue")
class RegisterActivity:AppCompatActivity() {

    //Initializing a Job for the coroutine
    private var viewModelJob = Job()
    private lateinit var db:LoginDatabase
    //Defining a scope for the coroutine to run
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    override fun onDestroy() {
        super.onDestroy()
        viewModelJob.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setView()
        db=Room.databaseBuilder(this.application,LoginDatabase::class.java, "login_app_database").build()
    }

    private fun isResidentialFieldValidated(firstName:TextInputEditText, lastName:TextInputEditText, email:TextInputEditText, phone:TextInputEditText, password:TextInputEditText, confirmPassword:TextInputEditText): Boolean {

        val countOfEmptyFields=countEmptyFields(arrayListOf(firstName.text.toString(),lastName.text.toString(),
                email.text.toString(),phone.text.toString(),password.text.toString(),confirmPassword.text.toString()))
        when {
            countOfEmptyFields>=2 -> {
                showErrorSnackBar(registerScrollViewLayout, R.string.error_emptyFields).show()
                return false
            }
            countOfEmptyFields==1 -> {
                setInLineEmptyError(firstNameLayout,firstName,R.string.error_emptyField)
                setInLineEmptyError(lastNameLayout,lastName,R.string.error_emptyField)
                setInLineEmptyError(email_registerLayout,email_register,R.string.error_emptyField)
                setInLineEmptyError(phoneLayout,phone,R.string.error_emptyField)
                setInLineEmptyError(password_registerLayout,password_register,R.string.error_emptyField)
                setInLineEmptyError(confirmPasswordLayout,confirmPassword,R.string.error_emptyField)
                return false
            }
            countOfEmptyFields==0 -> {
                if(email.text.toString().errorEmail()){
                    email_registerLayout.error=getString(R.string.error_invalidEmail)
                    email.doOnTextChanged { _, _, _, _ ->
                        if(!email.text.toString().errorEmail()){
                            email_registerLayout.error=null
                            email_register.error = null
                        }
                        else{
                            email_register.error=getString(R.string.error_invalidEmail)
                            email_registerLayout.error=null
                        }
                    }
                    return false
                }
                if(phone.text.toString().errorPhone()){
                    phoneLayout.error=getString(R.string.error_invalidPhone)
                    phone.doOnTextChanged { _, _, _, _ ->
                        if(!phone.text.toString().errorPhone()){
                            phoneLayout.error=null
                            phone.error = null
                        }
                        else{
                            phone.error=getString(R.string.error_invalidPhone)
                            phoneLayout.error=null
                        }
                    }
                    return false
                }
                if(password.text.toString().errorPassword()){
                    password_registerLayout.error=getString(R.string.error_weakPassword)
                    password.doOnTextChanged { _, _, _, _ ->
                        if(!password.text.toString().errorPassword()){
                            password_registerLayout.error=null
                            password.error = null
                        }
                        else{
                            password_registerLayout.error=getString(R.string.error_weakPassword)
                        }
                    }
                    return false
                }
                if(!confirmPassword.text.toString().equals(password.text.toString(),ignoreCase = false)){
                    showErrorSnackBar(registerScrollViewLayout,R.string.error_passwordsDoNoMatch).show()
                    return false
                }
                return true
            }
            else -> return false
        }
    }

    private fun isCommercialFieldValidated(businessName:TextInputEditText, email:TextInputEditText,cin:TextInputEditText, phone:TextInputEditText, password:TextInputEditText, confirmPassword:TextInputEditText): Boolean {

        val countOfEmptyFields=countEmptyFields(arrayListOf(businessName.text.toString(),
                email.text.toString(),cin.text.toString(),phone.text.toString(),password.text.toString(),confirmPassword.text.toString()))
        when {
            countOfEmptyFields>=2 -> {
                showErrorSnackBar(registerScrollViewLayout, R.string.error_emptyFields).show()
                return false
            }
            countOfEmptyFields==1 -> {
                setInLineEmptyError(businessNameLayout,businessName,R.string.error_emptyField)
                setInLineEmptyError(email_registerLayout,email_register,R.string.error_emptyField)
                setInLineEmptyError(cin_registerLayout,cin_register,R.string.error_emptyField)
                setInLineEmptyError(phoneLayout,phone,R.string.error_emptyField)
                setInLineEmptyError(password_registerLayout,password_register,R.string.error_emptyField)
                setInLineEmptyError(confirmPasswordLayout,confirmPassword,R.string.error_emptyField)
                return false
            }
            countOfEmptyFields==0 -> {
                if(email.text.toString().errorEmail()){
                    email_registerLayout.error=getString(R.string.error_invalidEmail)
                    email.doOnTextChanged { _, _, _, _ ->
                        if(!email.text.toString().errorEmail()){
                            email_registerLayout.error=null
                            email_register.error = null
                        }
                        else{
                            email_register.error=getString(R.string.error_invalidEmail)
                            email_registerLayout.error=null
                        }
                    }
                    return false
                }
                if(phone.text.toString().errorPhone()){
                    phoneLayout.error=getString(R.string.error_invalidPhone)
                    phone.doOnTextChanged { _, _, _, _ ->
                        if(!phone.text.toString().errorPhone()){
                            phoneLayout.error=null
                            phone.error = null
                        }
                        else{
                            phone.error=getString(R.string.error_invalidPhone)
                            phoneLayout.error=null
                        }
                    }
                    return false
                }
                if(password.text.toString().errorPassword()){
                    password_registerLayout.error=getString(R.string.error_weakPassword)
                    password.doOnTextChanged { _, _, _, _ ->
                        if(!password.text.toString().errorPassword()){
                            password_registerLayout.error=null
                            password.error = null
                        }
                        else{
                            password_registerLayout.error=getString(R.string.error_weakPassword)
                        }
                    }
                    return false
                }
                if(cin.text.toString().errorCIN()){
                    cin_registerLayout.error=getString(R.string.error_invalidCIN)
                    cin.doOnTextChanged { _, _, _, _ ->
                        if(!cin.text.toString().errorCIN()){
                            cin_registerLayout.error=null
                            cin.error = null
                        }
                        else{
                            cin.error=getString(R.string.error_invalidCIN)
                            cin_registerLayout.error=null
                        }
                    }
                    return false
                }
                if(!confirmPassword.text.toString().equals(password.text.toString(),ignoreCase = false)){
                    showErrorSnackBar(registerScrollViewLayout,R.string.error_passwordsDoNoMatch).show()
                    return false
                }
                return true
            }
            else -> return false
        }
    }

    private fun setView(){
        val itemList=arrayListOf(getString(R.string.customerType_residential),getString(R.string.customerType_commercial))
        val adapter = ArrayAdapter(this, R.layout.list_item, itemList)
        customerTypeDropdown.setAdapter(adapter)
        customerTypeDropdown.setOnItemClickListener { parent,_, position,_ ->
            when {
                parent.adapter.getItem(position).toString()==getString(R.string.customerType_commercial) -> {
                    commercialViewConstraints()
                }
                parent.adapter.getItem(position).toString()==getString(R.string.customerType_residential) -> {
                    residentialViewConstraints()
                }
                else -> {
                    afterCustomerTypeSelected.visibility=View.GONE
                }
            }
        }
    }

    private fun residentialViewConstraints() {
        businessNameLayout.visibility=View.GONE
        cin_registerLayout.visibility=View.GONE
        firstNameLayout.visibility=View.VISIBLE
        lastNameLayout.visibility=View.VISIBLE
        registerBackground.visibility=View.GONE
        beforeCustomerTypeSelection.visibility=View.GONE
        afterCustomerTypeSelected.visibility=View.VISIBLE
        registerButton.setOnClickListener {
            if(isResidentialFieldValidated(firstName,lastName,email_register,phone,password_register,confirmPassword)){
                uiScope.launch {
                    insert(db,firstName.text.toString(),lastName.text.toString(),"",email_register.text.toString(),"",
                        phone.text.toString(),password_register.text.toString(),"Residential")
                }
                showDialog({ _, _ -> this.finish() },
                        { dialog, _ -> dialog.cancel() },
                        R.string.registrationDialogTitle,
                        R.string.residentialRegistrationSuccessful,
                        R.string.dialogPositive,R.string.dialogNegative)?.show()
            }
        }
    }

    private fun commercialViewConstraints() {
        businessNameLayout.visibility=View.VISIBLE
        cin_registerLayout.visibility=View.VISIBLE
        firstNameLayout.visibility=View.GONE
        lastNameLayout.visibility=View.GONE
        registerBackground.visibility=View.GONE
        beforeCustomerTypeSelection.visibility=View.GONE
        afterCustomerTypeSelected.visibility=View.VISIBLE
        registerButton.setOnClickListener {
            if(isCommercialFieldValidated(businessName,email_register,cin_register,phone,password_register,confirmPassword)){
                uiScope.launch {
                    if(checkBeforeRegister(email_register.text.toString())!=null){
                        showDialog({ dialog, _ -> dialog.cancel() },
                            R.string.registrationDialogTitle,
                            R.string.alreadyRegisteredEntryFound,
                            R.string.dialogPositive)?.show()
                    }
                    else{
                        insert(db,"","",businessName.text.toString(),email_register.text.toString(),cin_register.text.toString(),
                            phone.text.toString(),password_register.text.toString(),"Commercial")
                        showDialog({ _, _ -> this@RegisterActivity.finish() },
                            { dialog, _ -> dialog.cancel() },
                            R.string.registrationDialogTitle,
                            R.string.commercialRegistrationSuccessful,
                            R.string.dialogPositive,R.string.dialogNegative)?.show()
                    }
                }

            }
        }
    }

    private suspend fun insert(db:LoginDatabase,firstName:String, lastName:String, businessName: String, email:String, cin:String, phone:String, password:String, customerType:String){
        withContext(Dispatchers.IO) {
            db.loginDatabaseDao.insertUser(LoginEntity(firstName=firstName,lastName = lastName,businessName = businessName,email = email,cin = cin,phone = phone,password = password,customerType = customerType))
        }
    }

    private suspend fun checkBeforeRegister(email:String):LoginEntity?{
        return withContext(Dispatchers.IO) {
            val res=db.loginDatabaseDao.getByEmail(email)
            res
        }
    }

}

