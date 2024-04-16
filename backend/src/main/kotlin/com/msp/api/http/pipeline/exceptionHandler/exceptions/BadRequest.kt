package com.msp.api.http.pipeline.exceptionHandler.exceptions

abstract class BadRequest(msg: String) : Exception(msg)

class BadEmail : BadRequest("Bad email")
class BadPhoneNumber : BadRequest("Bad phone number")
class WeakPassword : BadRequest("Password too weak")
class InvalidBirthDate : BadRequest("Invalid birthdate")
class InvalidPage : BadRequest("Page must be greater than or equal to 0")
class InvalidSize : BadRequest("Size must be greater than or equal to 1")
