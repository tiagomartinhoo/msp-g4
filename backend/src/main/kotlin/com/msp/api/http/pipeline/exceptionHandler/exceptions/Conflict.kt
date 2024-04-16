package com.msp.api.http.pipeline.exceptionHandler.exceptions

abstract class Conflict(msg: String) : Exception(msg)

class EmailAlreadyExists : Conflict("User with this email already exists")
class NifAlreadyExists : Conflict("User with this nif already exists")
