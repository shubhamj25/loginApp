package com.example.loginapp.screens.prelogin.activity

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.example.loginapp.R
import com.example.loginapp.database.LoginDatabase
import com.example.loginapp.database.LoginDatabaseDao
import com.example.loginapp.database.LoginEntity
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PreLoginViewPagerActivityTest {
    private lateinit var dao: LoginDatabaseDao

    /* Instantiate an ActivityScenarioRule object. */
    @get:Rule
    var activityRule: ActivityScenarioRule<PreLoginViewPagerActivity> =
        ActivityScenarioRule(PreLoginViewPagerActivity::class.java)

    @Before
    fun init() {
        dao = LoginDatabase.getInstance(getInstrumentation().targetContext).loginDatabaseDao
    }

    @Test
    fun isViewPagerVisible() {
        onView(withId(R.id.viewpager)).check(matches(isDisplayed()))
    }

    @Test
    fun testViewPagerFunctionality() {
        //Perform swipe up
        onView(withId(R.id.viewpager)).perform(swipeLeft())
        //verify swipe up
        onView(withId(R.id.registerScrollViewLayout)).check(matches(isDisplayed()))
        //Perform swipe down
        onView(withId(R.id.viewpager)).perform(swipeRight())
        //verify swipe down
        onView(withId(R.id.loginFragmentConstraintLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun fullAppTest_Registration() {
        //check viewpager visibility
        onView(withId(R.id.viewpager))
            .check(matches(isDisplayed()))

        //swipe to registration page
        onView(withId(R.id.viewpager)).perform(swipeLeft())
        onView(withId(R.id.registerScrollViewLayout)).check(
            matches(
                isDisplayed()
            )
        )

        //check Registration Fragment View layout visibility
        onView(withId(R.id.registerScrollViewLayout)).check(
            matches(
                isDisplayed()
            )
        )
        onView(withId(R.id.beforeCustomerTypeSelection)).check(
            matches(
                isDisplayed()
            )
        )
        onView(withId(R.id.customerType)).check(matches(isDisplayed()))
        onView(withId(R.id.registerSubtitle)).check(matches(isDisplayed()))
        onView(withId(R.id.registerTitle)).check(matches(isDisplayed()))


        //perform registration
        val noOfUsers = dao.getAllUsersCount()
        val userObject = LoginEntity(
            firstName = "Ram",
            lastName = "Gupta",
            email = "xyz@gmail.com",
            phone = "9276276384",
            password = "1234567890",
            customerType = "Residential",
            businessName = "",
            cin = ""
        )
        onView(withId(R.id.customerTypeDropdown)).perform(click())
        Espresso.onData(CoreMatchers.equalTo(userObject.customerType))
            .inRoot(RootMatchers.isPlatformPopup()).perform(
                click()
            )
        onView(withId(R.id.firstName)).perform(
            typeText(userObject.firstName),
            closeSoftKeyboard()
        )
        onView(withId(R.id.lastName)).perform(
            typeText(userObject.lastName),
            closeSoftKeyboard()
        )
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
        //Perform Dialog click
        onView(withText("Okay")).inRoot(isDialog()).check(
            matches(
                isDisplayed()
            )
        ).perform(click())
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
        onView(withId(R.id.loginFragmentConstraintLayout)).check(matches(isDisplayed()))

        onView(withId(R.id.email)).perform(
            clearText(),
            typeText(userObject.email),
            closeSoftKeyboard()
        )
        onView(withId(R.id.password)).perform(
            clearText(),
            typeText(userObject.password),
            closeSoftKeyboard()
        )
        onView(withId(R.id.loginButton)).perform(click())
        onView(withText("Okay")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click())

        //check Home Activity Visibility
        onView(withId(R.id.homeActivityLinearLayout)).check(matches(isDisplayed()))
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)

        //check visibility of all users

        onView(withText("Logout")).perform(click())
        onView(withId(R.id.loginFragmentConstraintLayout)).check(matches(isDisplayed()))
    }
}