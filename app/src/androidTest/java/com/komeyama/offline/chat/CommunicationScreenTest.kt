package com.komeyama.offline.chat

import android.os.SystemClock
import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.komeyama.offline.chat.domain.ActiveUser
import com.komeyama.offline.chat.nearbyclient.NearbyClient
import com.komeyama.offline.chat.nearbyclient.NearbyCommunicationContent
import com.komeyama.offline.chat.ui.MainActivity
import com.komeyama.offline.chat.util.RequestResult
import com.komeyama.offline.chat.util.toDateString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class CommunicationScreenTest {
    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    val countingTaskExecutorRule = CountingTaskExecutorRule()

    private var confirmUiDesign = true
    private lateinit var testAppComponent: TestAppComponent
    @Inject lateinit var nearbyClient: NearbyClient
    private lateinit var aroundEndpointInfo: MutableLiveData<List<ActiveUser>>
    private lateinit var requestResult: MutableLiveData<RequestResult>

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as MainApplication
        testAppComponent = DaggerTestAppComponent.factory().create(appContext)
        appContext.appComponent = testAppComponent
        testAppComponent.injectionCommunicationScreenTest(this)

        val aroundEndpointInfoFiled = NearbyClient::class.java.getDeclaredField("_aroundEndpointInfo")
        aroundEndpointInfoFiled.isAccessible = true
        aroundEndpointInfo = aroundEndpointInfoFiled.get(nearbyClient) as MutableLiveData<List<ActiveUser>>

        val requestedEndpointInfoFiled = NearbyClient::class.java.getDeclaredField("_requestResult")
        requestedEndpointInfoFiled.isAccessible = true
        requestResult = requestedEndpointInfoFiled.get(nearbyClient) as MutableLiveData<RequestResult>

        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun runApp() {
        val currentActiveUsrList = mutableListOf<ActiveUser>()
        currentActiveUsrList.add(
            ActiveUser("12345a", "nearby test0", "dummy0")
        )
        currentActiveUsrList.add(
            ActiveUser("dummySenderID_0", "dummySenderName", "dummyEndpointId")
        )
        currentActiveUsrList.add(
            ActiveUser("12345c", "nearby test2", "dummy2")
        )
        aroundEndpointInfo.postValue(currentActiveUsrList)
        waitNextProcess(2)
        countingTaskExecutorRule.drainTasks(3, TimeUnit.SECONDS)
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1,
                    ViewActions.click()
                ))
        waitNextProcess(1)
        Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(ViewActions.click())
        requestResult.postValue(RequestResult.SUCCESS)
        countingTaskExecutorRule.drainTasks(3, TimeUnit.SECONDS)
        waitNextProcess(3)
        nearbyClient.receiveContent.onNext(
            NearbyCommunicationContent("dummySenderID_0", "dummySenderName_0", "dummyReceiverID", "dummyReceiverName", Date().toDateString(), "Hello")
        )
        waitNextProcess(3)
        nearbyClient.receiveContent.onNext(
            NearbyCommunicationContent("dummySenderID_1", "dummySenderName_1", "dummyReceiverID", "dummyReceiverName", Date().toDateString(),  "Hello!")
        )
        waitNextProcess(10)
    }

    @After
    fun cleanup() {
        DrawerActions.close()
    }

    // Because of checking dialog design. But I want to change this later.
    fun waitNextProcess(second:Long) {
        var milliSecond = 0L
        if (confirmUiDesign) {
            milliSecond = second * 1000L
        }
        SystemClock.sleep(milliSecond)
    }
}