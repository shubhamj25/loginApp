package com.example.loginapp.screens.preLogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.loginapp.*
import com.example.loginapp.database.LoginDatabase
import com.example.loginapp.database.LoginEntity
import com.example.loginapp.databinding.FragmentRegisterBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment(viewPager: ViewPager):Fragment(){
    private lateinit var db:LoginDatabase
    private var vPager=viewPager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        val binding:FragmentRegisterBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_register,container,false)
        setView(binding,vPager)
        db= getDatabaseInstance(requireActivity().application)
        return binding.root
    }

    private fun isResidentialFieldValidated(firstName:TextInputEditText, lastName:TextInputEditText, email:TextInputEditText, phone:TextInputEditText, password:TextInputEditText, confirmPassword:TextInputEditText): Boolean {
        val countOfEmptyFields=countEmptyFields(arrayListOf(firstName.text.toString(),lastName.text.toString(),
                email.text.toString(),phone.text.toString(),password.text.toString(),confirmPassword.text.toString()))
        when {
            countOfEmptyFields>=2 -> {
                requireActivity().application.showErrorSnackBar(registerScrollViewLayout, R.string.error_emptyFields).show()
                return false
            }
            countOfEmptyFields==1 -> {
                requireActivity().application.setInLineEmptyError(firstNameLayout,firstName,R.string.error_emptyField)
                requireActivity().application.setInLineEmptyError(lastNameLayout,lastName,R.string.error_emptyField)
                requireActivity().application.setInLineEmptyError(email_registerLayout,email_register,R.string.error_emptyField)
                requireActivity().application.setInLineEmptyError(phoneLayout,phone,R.string.error_emptyField)
                requireActivity().application.setInLineEmptyError(password_registerLayout,password_register,R.string.error_emptyField)
                requireActivity().application.setInLineEmptyError(confirmPasswordLayout,confirmPassword,R.string.error_emptyField)
                return false
            }
            countOfEmptyFields==0 -> {
                if(!email.text.toString().isAValidEmail()){
                    email_registerLayout.error=getString(R.string.error_invalidEmail)
                    return false
                }
                if(!phone.text.toString().isAValidPhoneNumber()){
                    phoneLayout.error=getString(R.string.error_invalidPhone)
                    return false
                }
                if(!password.text.toString().isAValidPassword()){
                    password_registerLayout.error=getString(R.string.error_weakPassword)
                    return false
                }
                if(!confirmPassword.text.toString().equals(password.text.toString(),ignoreCase = false)){
                    requireActivity().application.showErrorSnackBar(registerScrollViewLayout,R.string.error_passwordsDoNoMatch).show()
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
                requireActivity().application.showErrorSnackBar(registerScrollViewLayout, R.string.error_emptyFields).show()
                return false
            }
            countOfEmptyFields==1 -> {
                requireActivity().application.setInLineEmptyError(businessNameLayout,businessName,R.string.error_emptyField)
                requireActivity().application.setInLineEmptyError(email_registerLayout,email_register,R.string.error_emptyField)
                requireActivity().application.setInLineEmptyError(cin_registerLayout,cin_register,R.string.error_emptyField)
                requireActivity().application.setInLineEmptyError(phoneLayout,phone,R.string.error_emptyField)
                requireActivity().application.setInLineEmptyError(password_registerLayout,password_register,R.string.error_emptyField)
                requireActivity().application.setInLineEmptyError(confirmPasswordLayout,confirmPassword,R.string.error_emptyField)
                return false
            }
            countOfEmptyFields==0 -> {
                if(!email.text.toString().isAValidEmail()){
                    email_registerLayout.error=getString(R.string.error_invalidEmail)
                    return false
                }
                if(!phone.text.toString().isAValidPhoneNumber()){
                    phoneLayout.error=getString(R.string.error_invalidPhone)
                    return false
                }
                if(!password.text.toString().isAValidPassword()){
                    password_registerLayout.error=getString(R.string.error_weakPassword)
                    return false
                }
                if(!cin.text.toString().isAValidCIN()){
                    cin_registerLayout.error=getString(R.string.error_invalidCIN)
                    return false
                }
                if(!confirmPassword.text.toString().equals(password.text.toString(),ignoreCase = false)){
                    requireActivity().application.showErrorSnackBar(registerScrollViewLayout,R.string.error_passwordsDoNoMatch).show()
                    return false
                }
                return true
            }
            else -> return false
        }
    }

    private fun setView(binding: FragmentRegisterBinding,viewPager: ViewPager){
        val itemList=arrayListOf(getString(R.string.customerType_residential),getString(R.string.customerType_commercial))
        val adapter = ArrayAdapter(requireActivity().applicationContext, R.layout.list_item, itemList)
        binding.customerTypeDropdown.setAdapter(adapter)
        binding.customerTypeDropdown.setOnItemClickListener { _,_, position,_ ->
            when(itemList[position]) {
                getString(R.string.customerType_commercial) -> viewConstraints(getString(R.string.customerType_commercial),viewPager)
                getString(R.string.customerType_residential) -> viewConstraints(getString(R.string.customerType_residential),viewPager)
                else ->afterCustomerTypeSelected.visibility=View.GONE
            }
        }
    }

    private fun viewConstraints(customerType: String,viewPager: ViewPager) {
        val isAResidentialCustomer:Boolean= customerType == getString(R.string.customerType_residential)
        businessNameLayout.visibility=if(isAResidentialCustomer) View.GONE else View.VISIBLE
        cin_registerLayout.visibility=if(isAResidentialCustomer) View.GONE else View.VISIBLE
        firstNameLayout.visibility=if(isAResidentialCustomer) View.VISIBLE else View.GONE
        lastNameLayout.visibility=if(isAResidentialCustomer) View.VISIBLE else View.GONE
        registerBackground.visibility=View.GONE
        beforeCustomerTypeSelection.visibility=View.GONE
        afterCustomerTypeSelected.visibility=View.VISIBLE
        registerButton.setOnClickListener {
            when(customerType){
                getString(R.string.customerType_residential) ->{
                    if(isResidentialFieldValidated(firstName,lastName,email_register,phone,password_register,confirmPassword)){
                        uiScope.launch {

                            if (checkBeforeRegister(email_register.text.toString()) != null) {
                                requireActivity().showDialog({ dialog, _ -> dialog.cancel() },
                                        R.string.registrationDialogTitle,
                                        R.string.alreadyRegisteredEntryFound,
                                        R.string.dialogPositive)?.show()
                            }
                            else {

                                insert(db, firstName.text.toString(), lastName.text.toString(), "", email_register.text.toString(), "",
                                        phone.text.toString(), password_register.text.toString(), getString(R.string.customerType_residential))

                                requireActivity().showDialog({ _, _ ->
                                    viewPager.currentItem = 0
                                    resetRegisterScreen()
                                    //Navigation.findNavController(registerScrollViewLayout).navigate(R.id.action_registerFragment_to_loginFragment)

                                    },
                                        { dialog, _ -> dialog.cancel() },
                                        R.string.registrationDialogTitle,
                                        R.string.residentialRegistrationSuccessful,
                                        R.string.dialogPositive, R.string.dialogNegative)?.show()
                            }
                        }
                    }
                }
                getString(R.string.customerType_commercial)->{
                    if(isCommercialFieldValidated(businessName,email_register,cin_register,phone,password_register,confirmPassword)){
                        uiScope.launch {
                            if(checkBeforeRegister(email_register.text.toString())!=null){

                                requireActivity().showDialog({ dialog, _ -> dialog.cancel() },
                                        R.string.registrationDialogTitle,
                                        R.string.alreadyRegisteredEntryFound,
                                        R.string.dialogPositive)?.show()
                            }
                            else {
                                insert(db,"","",businessName.text.toString(),email_register.text.toString(),cin_register.text.toString(),
                                        phone.text.toString(),password_register.text.toString(),getString(R.string.customerType_commercial))
                                requireActivity().showDialog({ _, _ ->
                                    viewPager.currentItem = 0
                                    resetRegisterScreen()
                                    //Navigation.findNavController(registerScrollViewLayout).navigate(R.id.action_registerFragment_to_loginFragment)
                                },
                                        { dialog, _ -> dialog.cancel() },
                                        R.string.registrationDialogTitle,
                                        R.string.commercialRegistrationSuccessful,
                                        R.string.dialogPositive,R.string.dialogNegative)?.show()
                            }
                        }

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

    private fun resetRegisterScreen(){
        registerBackground.visibility=View.VISIBLE
        beforeCustomerTypeSelection.visibility=View.VISIBLE
        afterCustomerTypeSelected.visibility=View.GONE
        clearAllFields()
    }

    private fun clearAllFields() {
        firstName.text=null
        lastName.text=null
        cin_register.text=null
        email_register.text=null
        phone.text=null
        password_register.text=null
        confirmPassword.text=null
        customerTypeDropdown.text=null
    }
}