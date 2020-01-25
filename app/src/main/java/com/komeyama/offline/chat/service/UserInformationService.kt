package com.komeyama.offline.chat.service

import com.komeyama.offline.chat.database.userinfo.UserInformationDao
import com.komeyama.offline.chat.database.userinfo.UserInformationEntities
import com.komeyama.offline.chat.util.userIdGenerator
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

    suspend fun getUserInformation(): UserInformationEntities =
        withContext(Dispatchers.IO) {
            userInformationDao.getUserInformation()
        }

    suspend fun insertUserInformation(userName: String) {
        val userInformation = UserInformationEntities(userName = userName, userId = userIdGenerator())
        withContext(Dispatchers.IO) {
            userInformationDao.insert(userInformation)
        }
    }

    suspend fun updateUserInformation(newUserInformationEntities: UserInformationEntities) =
        withContext(Dispatchers.IO) {
            userInformationDao.update(newUserInformationEntities)
        }
}