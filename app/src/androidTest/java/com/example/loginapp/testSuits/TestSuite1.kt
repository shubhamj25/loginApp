package com.example.loginapp.testSuits

import com.example.loginapp.screens.prelogin.activity.PreLoginViewPagerActivityTest
import com.example.loginapp.screens.prelogin.fragments.RegisterFragmentTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(PreLoginViewPagerActivityTest::class,RegisterFragmentTest::class)
class TestSuite1