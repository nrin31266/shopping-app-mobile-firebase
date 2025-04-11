package com.nrin31266.shoppingapp.domain.use_case

import com.nrin31266.shoppingapp.common.ResultState
import com.nrin31266.shoppingapp.domain.model.BannerDataModel
import com.nrin31266.shoppingapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetBannersUseCase @Inject constructor(private  val repo: Repo) {

    fun getBanners(): Flow<ResultState<List<BannerDataModel>>> {
        return repo.getBanners()
    }
}