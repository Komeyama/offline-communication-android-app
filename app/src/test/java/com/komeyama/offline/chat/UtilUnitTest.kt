package com.komeyama.offline.chat

import com.komeyama.offline.chat.util.splitUserIdAndName
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UtilUnitTest {
    @Test
    fun splitUserIdAndName_isCorrect() {
        val userIdAndName =  "abcdefghi:Test Hoge"
        assertEquals(userIdAndName.splitUserIdAndName().userId, "abcdefghi")
        assertEquals(userIdAndName.splitUserIdAndName().userName, "Test Hoge")
    }
}
