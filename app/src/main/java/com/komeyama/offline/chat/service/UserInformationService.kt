package com.komeyama.offline.chat.service

import com.komeyama.offline.chat.database.userinfo.UserInformationDao
import com.komeyama.offline.chat.database.userinfo.UserInformationEntities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserInformationService @Inject constructor(
    private val userInformationDao:UserInformationDao
) {
    suspend fun existsUserInformation() =
        withContext(Dispatchers.IO) {
            userInformationDao.existsUserInformation()
        }

    suspend fun getUserInformation(): List<UserInformationEntities> =
        withContext(Dispatchers.IO) {
            userInformationDao.getUserInformation()
        }

    suspend fun insertUserInformation(userName: String) {
        /**
         * todo: userId must be the value created by 'userIDGenerator' method. I will create this method.
         */
        val userInformation = UserInformationEntities(userName = userName, userId = "dummy")
        withContext(Dispatchers.IO) {
            userInformationDao.insert(userInformation)
        }
    }

    suspend fun updateUserInformation(newUserInformationEntities: UserInformationEntities) =
        withContext(Dispatchers.IO) {
            userInformationDao.update(newUserInformationEntities)
        }
}