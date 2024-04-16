package com.msp.api.storage.cloud

import java.util.*

interface CloudStorageUtils {

    fun uploadProfilePicture(fileName: String, file: ByteArray)

    fun downloadProfilePicture(fileName: String): ByteArray

    fun uploadOrderDocument(fileName: String, file: ByteArray)

    fun downloadOrderDocument(fileName: String): ByteArray

    fun getUserPhotosIDs(): List<UUID>

    fun deleteUserPicture(fileName: UUID)
}
