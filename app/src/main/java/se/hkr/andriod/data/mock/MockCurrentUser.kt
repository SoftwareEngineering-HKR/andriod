package se.hkr.andriod.data.mock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

var currentUser by mutableStateOf(MockUsers.adminUser)
