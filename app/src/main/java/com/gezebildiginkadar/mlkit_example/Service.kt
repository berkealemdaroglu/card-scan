package com.gezebildiginkadar.mlkit_example

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface CardScanApiService {
        @GET("v1/access-token") // Burada endpoint'in tam ve doğru tanımlandığından emin olun
        suspend fun getSessionToken(
            @Header("Authorization") authorization: String
        ): SessionTokenResponse

        @GET("v1/cards/{cardId}") // Burada endpoint'in tam ve doğru tanımlandığından emin olun
        suspend fun getCard(
            @Header("Authorization") authorization: String,
            @Path("cardId") cardId: String
        ): CardResponse

}
