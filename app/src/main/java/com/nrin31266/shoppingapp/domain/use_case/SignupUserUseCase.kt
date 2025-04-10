package com.nrin31266.shoppingapp.domain.use_case

import com.nrin31266.shoppingapp.common.ResultState
import com.nrin31266.shoppingapp.domain.model.UserDataModel
import com.nrin31266.shoppingapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignupUserUseCase @Inject constructor(private val repo: Repo) {

    fun signupUser(userDataModel: UserDataModel): Flow<ResultState<String>> {
        return repo.signupUser(userDataModel)
    }
} 