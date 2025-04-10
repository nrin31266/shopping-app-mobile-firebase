package com.nrin31266.shoppingapp.domain.use_case

import android.net.Uri
import com.nrin31266.shoppingapp.common.ResultState
import com.nrin31266.shoppingapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserProfileImageUseCase @Inject constructor(private val repo: Repo) {

    fun uploadProfileImage(uri: Uri): Flow<ResultState<String>> {
        return repo.userProfileImage(uri)
    }
} 