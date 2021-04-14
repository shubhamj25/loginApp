package com.example.loginapp.screens.prelogin.activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.loginapp.R
import kotlinx.android.synthetic.main.activity_prelogin_viewpager.view.*
import org.junit.Test
import org.junit.Rule
class PreLoginViewPagerActivityTest {
    @get:Rule
    val activityScenarioRule=ActivityScenarioRule(PreLoginViewPagerActivity::class.java)

    @Test
    fun isViewPagerVisible() {
        onView(withId(R.id.viewpager)).check(matches(isDisplayed()))
    }

    @Test
    fun testViewPagerFunctionality(){
        //Perform swipe up
        onView(withId(R.id.viewpager)).perform(swipeUp())
        //verify swipe up
        onView(withId(R.id.registerScrollViewLayout)).check(matches(isDisplayed()))
        //Perform swipe down
        onView(withId(R.id.viewpager)).perform(swipeDown())
        //verify swipe down
        onView(withId(R.id.loginFragmentConstraintLayout)).check(matches(isDisplayed()))
    }
}