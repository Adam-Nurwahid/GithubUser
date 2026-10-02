package com.damtoy.githubuser.presentation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.damtoy.githubuser.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchScreenTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun launch_showsSearchFieldAndIdleMessage() {
        onView(withId(R.id.etSearch)).check(matches(isDisplayed()))
        onView(withId(R.id.tvMessage)).check(matches(withText(R.string.search_idle)))
    }

    @Test
    fun typing_updatesSearchField() {
        onView(withId(R.id.etSearch)).perform(typeText("adam"), closeSoftKeyboard())
        onView(withId(R.id.etSearch)).check(matches(withText("adam")))
    }
}