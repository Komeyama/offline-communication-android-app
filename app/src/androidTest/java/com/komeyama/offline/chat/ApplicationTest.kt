package com.komeyama.offline.chat

import android.os.SystemClock.sleep
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
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.DrawerActions.close
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.rule.ActivityTestRule
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.komeyama.offline.chat.util.RequestResult
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

    private var confirmUiDesign = false

    private lateinit var testAppComponent: TestAppComponent
    @Inject lateinit var nearbyClient: NearbyClient
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var invitedEndpointInfo: MutableLiveData<ActiveUser>
    private lateinit var aroundEndpointInfo: MutableLiveData<List<ActiveUser>>
    private lateinit var requestResult: MutableLiveData<RequestResult>


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

        val invitedEndpointInfoFiled = NearbyClient::class.java.getDeclaredField("_inviteEndpointInfo")
        invitedEndpointInfoFiled.isAccessible = true
        invitedEndpointInfo = invitedEndpointInfoFiled.get(nearbyClient) as MutableLiveData<ActiveUser>

        val requestedEndpointInfoFiled = NearbyClient::class.java.getDeclaredField("_requestResult")
        requestedEndpointInfoFiled.isAccessible = true
        requestResult = requestedEndpointInfoFiled.get(nearbyClient) as MutableLiveData<RequestResult>
    }

    @Test
    fun runApp() {
        val settingFragmentId = bottomNavigationView.menu.findItem(R.id.SettingFragment).itemId
        onView(withId(settingFragmentId)).perform(click())
        countingTaskExecutorRule.drainTasks(3, TimeUnit.SECONDS)
        invitedEndpointInfo.postValue(ActiveUser("12345a", "nearby test0", "dummy0"))
        waitNextProcess(2)
        onView(withId(android.R.id.button1)).perform(click())
        waitNextProcess(1)
        onView(isRoot()).perform(ViewActions.pressBack())

        val communicationHistoryListFragmentId = bottomNavigationView.menu.findItem(R.id.CommunicationHistoryListFragment).itemId
        onView(withId(communicationHistoryListFragmentId)).perform(click())
        countingTaskExecutorRule.drainTasks(3, TimeUnit.SECONDS)
        invitedEndpointInfo.postValue(ActiveUser("12345a", "nearby test0", "dummy0"))
        waitNextProcess(2)
        onView(withId(android.R.id.button1)).perform(click())
        waitNextProcess(1)
        onView(isRoot()).perform(ViewActions.pressBack())

        val communicableUserListFragmentId = bottomNavigationView.menu.findItem(R.id.CommunicableUserListFragment).itemId
        onView(withId(communicableUserListFragmentId)).perform(click())
        countingTaskExecutorRule.drainTasks(3, TimeUnit.SECONDS)
        invitedEndpointInfo.postValue(ActiveUser("12345a", "nearby test0", "dummy0"))
        waitNextProcess(2)
        onView(withId(android.R.id.button1)).perform(click())
        waitNextProcess(1)
        onView(isRoot()).perform(ViewActions.pressBack())

        val currentActiveUsrList = mutableListOf<ActiveUser>()
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
        countingTaskExecutorRule.drainTasks(3, TimeUnit.SECONDS)
        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        waitNextProcess(1)
        onView(withId(android.R.id.button1)).perform(click())
        waitNextProcess(3)
        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click()))
        requestResult.postValue(RequestResult.SUCCESS)
        countingTaskExecutorRule.drainTasks(3, TimeUnit.SECONDS)
        waitNextProcess(3)
        onView(isRoot()).perform(ViewActions.pressBack())
        waitNextProcess(2)

    }

    @After
    fun cleanup() {
        close()
    }

    // Because of checking dialog design. But I want to change this later.
    fun waitNextProcess(second:Long) {
        var milliSecond = 0L
        if (confirmUiDesign) {
            milliSecond = second * 1000L
        }
        sleep(milliSecond)
    }

}