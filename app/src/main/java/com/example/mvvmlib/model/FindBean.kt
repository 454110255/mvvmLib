package com.example.mvvmlib.model

data class FindBean(
    val apiStatusType: String?,
    val `data`: List<Data>?,
    val pageCount: Int?,
    val pageNo: Int?,
    val status: Int?,
    val statusMessage: String?
)

data class Data(
    val avatar: String?,
    val digest: String?,
    val discoveryArticleImgList: List<DiscoveryArticleImg>?,
    val discoveryArticleProductList: List<DiscoveryArticleProduct>?,
    val id: Int?,
    val no: String?,
    val releaseTime: String?,
    val releaseTimeStamp: String?,
    val title: String?,
    val userLoginName: String?,
    val userName: String?
)

data class DiscoveryArticleImg(
    val image: String?
)

data class DiscoveryArticleProduct(
    val activityNo: String?,
    val isActivity: Int?,
    val marketingPrice: String?,
    val plusPrice: String?,
    val price: String?,
    val productName: String?,
    val productNo: String?,
    val productStock: Int?,
    val showPlusPrice: String?,
    val sort: Int?,
    val thumbnail: String?
)