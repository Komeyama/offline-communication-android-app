package com.komeyama.offline.chat.service

import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.komeyama.offline.chat.database.userinfo.UserInformationDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class UserInformationServiceTest {


    lateinit var userInformationService: UserInformationService
    @Before
    fun setUp() {
        val db = Room.databaseBuilder(ApplicationProvider.getApplicationContext(), UserInformationDatabase::class.java, "user_information").build()
        userInformationService =
            UserInformationService(db.userInformationDao)

    }

    @Test
    fun existsUserInformationTest() = runBlocking{
        assertEquals(false, userInformationService.existsUserInformation())
    }

    @Test
    fun insertUserInformationTest() = runBlocking {
        if (!userInformationService.existsUserInformation()) {
            userInformationService.insertUserInformation("dummyName")
        }
        assertEquals(userInformationService.getUserInformation().userName,"dummyName")
    }

    @Test
    fun updateUserInformationTest() = runBlocking {
        var userId = ""
        if (!userInformationService.existsUserInformation()) {
            userInformationService.insertUserInformation("dummyName")
            val oldUserInformation = userInformationService.getUserInformation()
            userId = oldUserInformation.userId
            userInformationService.updateUserInformation(oldUserInformation.copy(userName = "newDummyName"))
        }
        assertEquals(userInformationService.getUserInformation().databaseId, 0)
        assertEquals(userInformationService.getUserInformation().userId, userId)
        assertEquals(userInformationService.getUserInformation().userName,"newDummyName")
    }


}