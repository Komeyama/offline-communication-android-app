package com.komeyama.offline.chat

import android.os.SystemClock.sleep
import androidx.appcompat.app.AlertDialog
import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.komeyama.offline.chat.domain.ActiveUser
import com.komeyama.offline.chat.nearbyclient.NearbyClient
import com.komeyama.offline.chat.ui.MainActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import timber.log.Timber
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.DrawerActions.close
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.rule.ActivityTestRule
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.junit.After
import org.junit.Rule
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class ApplicationTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    val countingTaskExecutorRule = CountingTaskExecutorRule()

    private lateinit var testAppComponent: TestAppComponent
    @Inject lateinit var nearbyClient: NearbyClient
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var requestedEndpointInfo: MutableLiveData<ActiveUser>
    private lateinit var aroundEndpointInfo: MutableLiveData<List<ActiveUser>>

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as MainApplication
        testAppComponent = DaggerTestAppComponent.factory().create(appContext)
        appContext.appComponent = testAppComponent
        testAppComponent.injection(this)
        ActivityScenario.launch(MainActivity::class.java)
        bottomNavigationView = activityTestRule.activity.findViewById(R.id.bottom_navigation)

        val aroundEndpointInfoFiled = NearbyClient::class.java.getDeclaredField("_aroundEndpointInfo")
        aroundEndpointInfoFiled.isAccessible = true
        aroundEndpointInfo = aroundEndpointInfoFiled.get(nearbyClient) as MutableLiveData<List<ActiveUser>>

        val requestedEndpointInfoFiled = NearbyClient::class.java.getDeclaredField("_requestedEndpointInfo")
        requestedEndpointInfoFiled.isAccessible = true
        requestedEndpointInfo = requestedEndpointInfoFiled.get(nearbyClient) as MutableLiveData<ActiveUser>
    }

    @Test
    fun runApp() {
        val settingFragmentId = bottomNavigationView.menu.findItem(R.id.SettingFragment).itemId
        onView(withId(settingFragmentId)).perform(click())
        countingTaskExecutorRule.drainTasks(3, TimeUnit.SECONDS)
        requestedEndpointInfo.postValue(ActiveUser("12345a", "nearby test0", "dummy0"))
        waitNextProcess(2)
        onView(withId(android.R.id.button1)).perform(click())
        waitNextProcess(1)

        val communicationHistoryListFragmentId = bottomNavigationView.menu.findItem(R.id.CommunicationHistoryListFragment).itemId
        onView(withId(communicationHistoryListFragmentId)).perform(click())
        countingTaskExecutorRule.drainTasks(3, TimeUnit.SECONDS)
        requestedEndpointInfo.postValue(ActiveUser("12345a", "nearby test0", "dummy0"))
        waitNextProcess(2)
        onView(withId(android.R.id.button1)).perform(click())
        waitNextProcess(1)

        val communicableUserListFragmentId = bottomNavigationView.menu.findItem(R.id.CommunicableUserListFragment).itemId
        onView(withId(communicableUserListFragmentId)).perform(click())
        countingTaskExecutorRule.drainTasks(3, TimeUnit.SECONDS)
        requestedEndpointInfo.postValue(ActiveUser("12345a", "nearby test0", "dummy0"))
        waitNextProcess(2)
        onView(withId(android.R.id.button1)).perform(click())
        waitNextProcess(1)

        val currentActiveUsrList = mutableListOf<ActiveUser>()
        waitNextProcess(2)
        currentActiveUsrList.add(
            ActiveUser("12345a", "nearby test0", "dummy0")
        )
        currentActiveUsrList.add(
            ActiveUser("12345b", "nearby test1", "dummy1")
        )
        currentActiveUsrList.add(
            ActiveUser("12345c", "nearby test2", "dummy2")
        )
        aroundEndpointInfo.postValue(currentActiveUsrList)
        waitNextProcess(2)
        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        waitNextProcess(2)
    }

    @After
    fun cleanup() {
        close()
    }

    // Because of checking dialog design. But I want to change this later.
    fun waitNextProcess(second:Long) {
        val milliSecond = second * 1000L
        sleep(milliSecond)
    }

}