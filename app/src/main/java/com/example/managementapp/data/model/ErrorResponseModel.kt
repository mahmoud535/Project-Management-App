package com.example.managementapp.data.model

import com.google.gson.annotations.SerializedName

data class ErrorResponseModel(
    @SerializedName("message") var message: String? = null,
)