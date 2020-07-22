package com.stepashka.buildinglocator2.models

data class RegisterResponse (val error: Boolean, val message: String, val token: String, val userId: Int)