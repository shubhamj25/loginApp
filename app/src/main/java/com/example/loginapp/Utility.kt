package com.example.loginapp
import android.content.DialogInterface
import android.graphics.Color
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.loginapp.screens.register.RegisterActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

const val myPrefs = "loginPrefs"
fun String.errorPassword(): Boolean {
    if(length <8){
        return true
    }
    return false
}

fun RegisterActivity.setInLineEmptyError(textInputLayout: TextInputLayout, textInputEditText: TextInputEditText, errorMessage:Int){
    if(textInputEditText.text.toString().isEmpty()) {
        textInputLayout.error=getString(errorMessage)
        textInputEditText.error=null
        textInputEditText.doOnTextChanged { _, _, _, _ ->
            if(textInputEditText.text.toString().isNotEmpty()){
                textInputLayout.error=null
                textInputEditText.error=null
            }
            else{
                textInputEditText.error=getString(errorMessage)
                textInputLayout.error=null
            }
        }
    }
}


fun String.errorCIN(): Boolean {
    if(length !=21){
        return true
    }
    return false
}

fun countEmptyFields(array: ArrayList<String>):Int{
    var count=0
    for(x in array){
        if(x.trim().isEmpty()){
            count++
        }
    }
    return count
}

fun AppCompatActivity.showDialog(positiveAction: DialogInterface.OnClickListener?,negativeAction:DialogInterface.OnClickListener, title:Int, message:Int, positiveButton:Int, negativeButton:Int): AlertDialog.Builder? {
    return AlertDialog.Builder(this).setMessage(getString(message)).setPositiveButton(
            positiveButton, positiveAction).setNegativeButton(negativeButton,negativeAction)
            .setTitle(getString(title))
}

fun AppCompatActivity.showDialog(positiveAction: DialogInterface.OnClickListener?, title:Int, message:Int, positiveButton:Int): AlertDialog.Builder? {
    return AlertDialog.Builder(this).setMessage(getString(message)).setPositiveButton(
        positiveButton, positiveAction).setTitle(getString(title))
}

fun AppCompatActivity.showErrorSnackBar(view: View, message: Int): Snackbar {
    return Snackbar.make(view,getString(message), Snackbar.LENGTH_LONG)
            .setBackgroundTint(Color.parseColor(getString(R.string.deepRed))).setTextColor(Color.WHITE)
}

fun String.errorPhone(): Boolean {
    if(length !=10){
        return true
    }
    return false
}

fun String.errorEmail(): Boolean {
    val regex= "^((([!#\$%&'*+\\-/=?^_`{|}~\\w])|([!#\$%&'*+\\-/=?^_`{|}~\\w][!#\$%&'*+\\-/=?^_`{|}~.\\w]*[!#\$%&'*+\\-/=?^_`{|}~\\w]))[@]\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)\$"
    if(!matches(regex.toRegex())){
        return true
    }
    return false
}