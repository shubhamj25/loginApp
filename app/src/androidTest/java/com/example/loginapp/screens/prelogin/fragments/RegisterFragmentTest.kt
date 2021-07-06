package com.example.loginapp.screens.prelogin.fragments

import android.os.SystemClock.sleep
import android.view.View
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.example.loginapp.R
import com.example.loginapp.database.LoginDatabase
import com.example.loginapp.database.LoginDatabaseDao
import com.example.loginapp.database.LoginEntity
import com.example.loginapp.screens.prelogin.activity.PreLoginViewPagerActivity
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class RegisterFragmentTest {
    private lateinit var scenario: FragmentScenario<RegisterFragment>
    private lateinit var dao: LoginDatabaseDao

    /* Instantiate an ActivityScenarioRule object. */
    @get:Rule
    var activityRule: ActivityScenarioRule<PreLoginViewPagerActivity> =
        ActivityScenarioRule(PreLoginViewPagerActivity::class.java)

    @Before
    fun init() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_LoginApp)
        dao =
            LoginDatabase.getInstance(InstrumentationRegistry.getInstrumentation().targetContext).loginDatabaseDao
    }

    @Test
    fun isRegisterFragmentVisible() {
        onView(withId(R.id.registerScrollViewLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun checkPriorCustomerTypeSelect() {
        onView(withId(R.id.beforeCustomerTypeSelection)).check(matches(isDisplayed()))
        onView(withId(R.id.customerType)).check(matches(isDisplayed()))
        onView(withId(R.id.registerSubtitle)).check(matches(isDisplayed()))
        onView(withId(R.id.registerTitle)).check(matches(isDisplayed()))
    }

    @Test
    fun test_CustomerTypeDropDown_ResidentialSelect() {
        onView(withId(R.id.customerTypeDropdown)).perform(click())
        onData(equalTo("Residential")).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        sleep(1000)
        //verify
        onView(withId(R.id.firstName)).perform(scrollTo()).check(matches(isDisplayed()))
        onView(withId(R.id.lastName)).perform(scrollTo()).check(matches(isDisplayed()))
        onView(withId(R.id.email_register)).perform(scrollTo()).check(matches(isDisplayed()))
        onView(withId(R.id.phone)).perform(scrollTo()).check(matches(isDisplayed()))
        onView(withId(R.id.password_register)).perform(scrollTo()).check(matches(isDisplayed()))
        onView(withId(R.id.confirmPassword)).perform(scrollTo()).check(matches(isDisplayed()))
        onView(withId(R.id.registerButton)).perform(scrollTo()).check(matches(isDisplayed()))
    }

    @Test
    fun test_CustomerTypeDropDown_CommercialSelect() {
        onView(withId(R.id.customerTypeDropdown)).perform(click())
        onData(equalTo("Commercial")).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withId(R.id.registerScrollViewLayout)).perform(click())
        //verify
        onView(withId(R.id.businessNameLayout)).perform(scrollTo()).check(matches(isDisplayed()))
        onView(withId(R.id.email_registerLayout)).perform(scrollTo()).check(matches(isDisplayed()))
        onView(withId(R.id.cin_registerLayout)).perform(scrollTo()).check(matches(isDisplayed()))
        onView(withId(R.id.phoneLayout)).perform(scrollTo()).check(matches(isDisplayed()))
        onView(withId(R.id.password_registerLayout)).perform(scrollTo())
            .check(matches(isDisplayed()))
        onView(withId(R.id.confirmPasswordLayout)).perform(scrollTo()).check(matches(isDisplayed()))
        onView(withId(R.id.registerButton)).perform(scrollTo()).check(matches(isDisplayed()))
    }

    @Test
    fun registrationCase_Residential_Success() {
        val noOfUsers = dao.getAllUsersCount()
        val userObject = LoginEntity(
            firstName = "Laxman",
            lastName = "Gupta",
            email = "laxman@gmail.com",
            phone = "9276276384",
            password = "1234567890",
            customerType = "Residential",
            businessName = "",
            cin = ""
        )
        onView(withId(R.id.customerTypeDropdown)).perform(click())
        onData(equalTo(userObject.customerType)).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())
        //perform
        onView(withId(R.id.firstName)).perform(typeText(userObject.firstName), closeSoftKeyboard())
        onView(withId(R.id.lastName)).perform(typeText(userObject.lastName), closeSoftKeyboard())
        onView(withId(R.id.email_register)).perform(
            typeText(userObject.email),
            closeSoftKeyboard()
        )
        onView(withId(R.id.phone)).perform(
            typeText(userObject.phone),
            closeSoftKeyboard()
        )
        onView(withId(R.id.password_register)).perform(
            typeText(userObject.password),
            closeSoftKeyboard()
        )
        onView(withId(R.id.confirmPassword)).perform(
            typeText(userObject.password),
            closeSoftKeyboard()
        )
        onView(withId(R.id.registerButton)).perform(click())
        onView(withText("Okay")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())
        //verify
        val testUser = dao.getByEmail(userObject.email)
        if (testUser != null) {
            Assert.assertEquals(
                "Registration Failed: Email Already Registered",
                dao.getAllUsersCount(),
                noOfUsers + 1
            )
        } else {
            Assert.assertEquals(
                "Registration Failed: Email Not Found",
                dao.getAllUsersCount(),
                noOfUsers + 1
            )
        }

    }

    @Test
    fun registrationCase_Residential_FirstNameNull() {
        val userObject = LoginEntity(
            firstName = "",
            lastName = "Gupta",
            email = "sita12@gmail.com",
            phone = "9276276384",
            password = "1234567890",
            customerType = "Residential",
            businessName = "",
            cin = ""
        )
        onView(withId(R.id.customerTypeDropdown)).perform(click())
        onData(equalTo(userObject.customerType)).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())
        //perform
        onView(withId(R.id.firstName)).perform(typeText(userObject.firstName), closeSoftKeyboard())
        onView(withId(R.id.lastName)).perform(typeText(userObject.lastName), closeSoftKeyboard())
        onView(withId(R.id.email_register)).perform(
            typeText(userObject.email),
            closeSoftKeyboard()
        )
        onView(withId(R.id.phone)).perform(
            typeText(userObject.phone),
            closeSoftKeyboard()
        )
        onView(withId(R.id.password_register)).perform(
            typeText(userObject.password),
            closeSoftKeyboard()
        )
        onView(withId(R.id.confirmPassword)).perform(
            typeText(userObject.password),
            closeSoftKeyboard()
        )
        onView(withId(R.id.registerButton)).perform(click())
        //verify
        onView(withId(R.id.firstNameLayout)).check(matches(hasTextInputLayoutErrorText("Empty Field") as Matcher<in View>?))
    }

    @Test
    fun registrationCase_Residential_PasswordsDoNotMatch() {
        val userObject = LoginEntity(
            firstName = "Sita",
            lastName = "Gupta",
            email = "sita1@gmail.com",
            phone = "9276276384",
            password = "1234567890",
            customerType = "Residential",
            businessName = "",
            cin = ""
        )
        onView(withId(R.id.customerTypeDropdown)).perform(click())
        onData(equalTo(userObject.customerType)).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())
        //perform
        onView(withId(R.id.firstName)).perform(typeText(userObject.firstName), closeSoftKeyboard())
        onView(withId(R.id.lastName)).perform(typeText(userObject.lastName), closeSoftKeyboard())
        onView(withId(R.id.email_register)).perform(
            typeText(userObject.email),
            closeSoftKeyboard()
        )
        onView(withId(R.id.phone)).perform(
            typeText(userObject.phone),
            closeSoftKeyboard()
        )
        onView(withId(R.id.password_register)).perform(
            typeText(userObject.password),
            closeSoftKeyboard()
        )
        onView(withId(R.id.confirmPassword)).perform(
            typeText("123456789"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.registerButton)).perform(click())
    }

    private fun hasTextInputLayoutErrorText(expectedErrorText: String): TypeSafeMatcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: org.hamcrest.Description?) {}
            override fun matchesSafely(item: View?): Boolean {
                if (item !is TextInputLayout) return false
                val error = item.error ?: return false
                val hint = error.toString()
                return expectedErrorText == hint
            }
        }
    }
}