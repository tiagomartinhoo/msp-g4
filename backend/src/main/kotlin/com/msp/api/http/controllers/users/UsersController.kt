package com.msp.api.http.controllers.users

import com.msp.api.domain.User
import com.msp.api.http.Uris
import com.msp.api.http.controllers.users.models.LoginInput
import com.msp.api.http.controllers.users.models.LoginOutput
import com.msp.api.http.pipeline.authentication.Authentication
import com.msp.api.http.pipeline.exceptionHandler.exceptions.ForbiddenRequest
import com.msp.api.services.UsersService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping
class UsersController(private val usersService: UsersService) {

    @PostMapping(Uris.LOGIN)
    fun login(
        @RequestBody loginInput: LoginInput
    ): ResponseEntity<LoginOutput> {
        val loginOutput = usersService.login(loginInput.email, loginInput.password)
        return ResponseEntity.ok(loginOutput)
    }

    @Authentication
    @PostMapping(Uris.USER_PHOTO)
    fun uploadProfilePicture(
        @PathVariable uid: String,
        @RequestParam photo: MultipartFile,
        user: User
    ): ResponseEntity<Unit> {
        if (user.uId != uid) throw ForbiddenRequest()

        usersService.uploadProfilePicture(uid, photo.bytes)

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @Authentication
    @GetMapping(Uris.USER_PHOTO)
    fun getProfilePicture(
        @PathVariable uid: String
    ): ResponseEntity<ByteArray> {
        val profilePicture = usersService.getProfilePicture(uid)

        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType("image/png")
        headers.contentLength = profilePicture.size.toLong()

        return ResponseEntity.ok().headers(headers).body(profilePicture)
    }
}
