package com.msp.api.http.pipeline.exceptionHandler.exceptions

abstract class UnauthorizedRequest(msg: String) : Exception(msg)

class Unauthenticated : UnauthorizedRequest("Unauthenticated")
class LoginFailed : UnauthorizedRequest("The credentials do not match")
