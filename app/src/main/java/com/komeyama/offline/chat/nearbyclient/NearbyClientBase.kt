package com.komeyama.offline.chat.nearbyclient

import androidx.lifecycle.LiveData
import com.komeyama.offline.chat.domain.ActiveUser

interface NearbyClientBase {
    val aroundEndpointInfo: LiveData<List<ActiveUser>>
}