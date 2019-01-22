package com.himanshurawat.onelabs.pojo

import java.io.Serializable

data class Person(
    val id: String,
    val name: String,
    val phoneNumber: List<String>
): Serializable