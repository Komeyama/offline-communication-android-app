package com.komeyama.offline.chat

import com.komeyama.offline.chat.util.splitUserIdAndName
import com.komeyama.offline.chat.util.toDate
import com.komeyama.offline.chat.util.toDateString
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UtilUnitTest {
    @Test
    fun splitUserIdAndName_isCorrect() {
        val userIdAndName =  "abcdefghi:Test Hoge"
        assertEquals("abcdefghi",userIdAndName.splitUserIdAndName().userId)
        assertEquals("Test Hoge",userIdAndName.splitUserIdAndName().userName)
    }

    @Test
    fun toDate_toDateString_isCorrect() {
        val date = "2020/03/03 12:23:34".toDate()
        val dateString = date.toDateString()
        assertEquals("2020/03/03 12:23:34", dateString)
    }
}
