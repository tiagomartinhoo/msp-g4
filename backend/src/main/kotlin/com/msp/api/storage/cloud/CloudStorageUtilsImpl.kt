package com.msp.api.storage.cloud

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.msp.api.http.pipeline.exceptionHandler.exceptions.FileDoesNotExists
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream
import java.util.*

@Component
class CloudStorageUtilsImpl(
    cloudStorageConfiguration: CloudStorageConfiguration
) : CloudStorageUtils {

    private val storage: Storage = cloudStorageConfiguration.storage

    private val userPhotosBucket = cloudStorageConfiguration.userPhotosBucket
    private val orderDocumentsBucket = cloudStorageConfiguration.orderDocumentsBucket

    private val pngContentType = "image/png"
    private val pdfContentType = "application/pdf"

    private fun upload(fileName: String, content: ByteArray, contentType: String, bucketName: String) {
        val blobId = BlobId.of(bucketName, fileName)

        val blobInfo = BlobInfo.newBuilder(blobId)
            .setContentType(contentType)
            .build()

        storage.create(blobInfo, content)
    }

    private fun download(fileName: String, bucketName: String): ByteArray {
        val blob = storage.get(bucketName, fileName) ?: throw FileDoesNotExists()

        val outputStream = ByteArrayOutputStream()

        blob.downloadTo(outputStream)
        outputStream.close()

        return outputStream.toByteArray()
    }

    private fun deleteFile(bucketName: String, fileName: String) {
        BlobId.of(bucketName, fileName)?.let { storage.delete(it) }
    }

    override fun uploadProfilePicture(fileName: String, file: ByteArray) =
        upload(fileName = fileName, content = file, contentType = pngContentType, bucketName = userPhotosBucket)

    override fun downloadProfilePicture(fileName: String): ByteArray =
        download(fileName = fileName, bucketName = userPhotosBucket)

    override fun uploadOrderDocument(fileName: String, file: ByteArray) =
        upload(fileName = fileName, content = file, contentType = pdfContentType, bucketName = orderDocumentsBucket)

    override fun downloadOrderDocument(fileName: String): ByteArray =
        download(fileName = fileName, bucketName = orderDocumentsBucket)

    override fun getUserPhotosIDs(): List<UUID> =
        storage.list(userPhotosBucket).iterateAll().toList().map { UUID.fromString(it.name) }

    override fun deleteUserPicture(fileName: UUID) {
        deleteFile(bucketName = userPhotosBucket, fileName = fileName.toString())
    }
}
