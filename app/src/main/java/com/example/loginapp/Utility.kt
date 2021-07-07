package com.example.loginapp
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.os.AsyncTask
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.loginapp.database.LoginDatabase
import com.example.loginapp.screens.home.LocationActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.sqrt

val dToPt= LocationActivity.EQUIVALENT_TO_A_KM * 2 * sqrt(2F)
fun squareMap(myLocation: LatLng): MutableList<LatLng> {
    return mutableListOf(
        LatLng(myLocation.latitude + dToPt, myLocation.longitude + dToPt),
        LatLng(myLocation.latitude - dToPt, myLocation.longitude + dToPt),
        LatLng(myLocation.latitude - dToPt, myLocation.longitude - dToPt),
        LatLng(myLocation.latitude + dToPt, myLocation.longitude - dToPt)
    )
}

fun showSnackBarOnTop(contextView: View, activity: Activity, message: Int, color: Int):Snackbar{
    val snack = Snackbar.make(contextView, activity.getString(message), Snackbar.LENGTH_LONG)
    val view = snack.view
    val params = view.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
    view.layoutParams = params
    snack.setBackgroundTint(color).setTextColor(Color.WHITE)
    return snack
}

var uiScope = CoroutineScope(Dispatchers.Main + Job())
fun getSharedPreferenceInstance(application: Application):SharedPreferences{
    return application.getSharedPreferences(
        application.getString(R.string.myPrefs),
        Context.MODE_PRIVATE
    )
}

fun getLinearLayoutManager(application: Application): LinearLayoutManager {
    return LinearLayoutManager(application, LinearLayoutManager.VERTICAL, false)
}


fun clearSharedPreferences(sharedPreferences: SharedPreferences){
    sharedPreferences.edit().clear().apply()
}

fun getDatabaseInstance(application: Application):LoginDatabase{
    return Room.databaseBuilder(
        application,
        LoginDatabase::class.java,
        application.resources.getString(R.string.databaseName)
    ).build()

}

fun String.isAValidPassword(): Boolean {
    return length>=6
}

fun String.isAValidCIN(): Boolean {
    return length==21
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

fun AppCompatActivity.showDialog(
    positiveAction: DialogInterface.OnClickListener? = null,
    negativeAction: DialogInterface.OnClickListener,
    title: Int,
    message: Int,
    positiveButton: Int,
    negativeButton: Int
): AlertDialog.Builder? {
    return AlertDialog.Builder(this).setMessage(getString(message)).setPositiveButton(
        positiveButton, positiveAction
    ).setNegativeButton(negativeButton, negativeAction)
            .setTitle(getString(title))
}

fun FragmentActivity.showDialog(
    positiveAction: DialogInterface.OnClickListener? = null,
    negativeAction: DialogInterface.OnClickListener,
    title: Int,
    message: Int,
    positiveButton: Int,
    negativeButton: Int
): AlertDialog.Builder? {
    return AlertDialog.Builder(this).setMessage(getString(message)).setPositiveButton(
        positiveButton, positiveAction
    ).setNegativeButton(negativeButton, negativeAction)
        .setTitle(getString(title))
}

fun FragmentActivity.showDialog(
    positiveAction: DialogInterface.OnClickListener?,
    title: Int,
    message: Int,
    positiveButton: Int
): AlertDialog.Builder? {
    return AlertDialog.Builder(this).setMessage(getString(message)).setPositiveButton(
        positiveButton, positiveAction
    ).setTitle(getString(title))
}

fun Application.showErrorSnackBar(view: View, message: Int): Snackbar {
    return Snackbar.make(view, getString(message), Snackbar.LENGTH_LONG)
        .setBackgroundTint(Color.parseColor(getString(R.string.deepRed))).setTextColor(Color.WHITE)
}

fun String.isAValidPhoneNumber(): Boolean {
    return length==10
}

fun String.isAValidEmail(): Boolean {
    val regex= "^((([!#\$%&'*+\\-/=?^_`{|}~\\w])|([!#\$%&'*+\\-/=?^_`{|}~\\w][!#\$%&'*+\\-/=?^_`{|}~.\\w]*[!#\$%&'*+\\-/=?^_`{|}~\\w]))[@]\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)\$"
    return matches(regex.toRegex())
}

fun Application.setInLineEmptyError(
    textInputLayout: TextInputLayout,
    textInputEditText: TextInputEditText,
    errorMessage: Int
){
    if(textInputEditText.text.toString().isEmpty()) {
        textInputLayout.error=getString(errorMessage)
    }
}

fun itos(number:Int):String{
    return number.toString()
}

class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        handler()
        return null
    }
}

suspend fun getDailyQuote():String{
    return withContext(Dispatchers.IO){
        var quote=""
        val url="https://type.fit/api/quotes"
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        BufferedReader(InputStreamReader(connection.inputStream, "utf-8")).use { br ->
            val response = StringBuffer()
            var inputLine = br.readLine()
            while (inputLine != null) {
                response.append(inputLine)
                inputLine = br.readLine()
            }
            br.close()
            val q=JsonParser.parseString(response.toString())
            val jsonObject= q.asJsonArray.get((Math.random() * q.asJsonArray.size()).toInt()).asJsonObject
            quote=jsonObject["text"].toString()+"\n- "+jsonObject["author"].toString()
        }
        quote
    }
}
