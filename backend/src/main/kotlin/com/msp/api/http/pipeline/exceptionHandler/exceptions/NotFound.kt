package com.msp.api.http.pipeline.exceptionHandler.exceptions

import com.msp.api.domain.UserRole

abstract class NotFound(msg: String) : Exception(msg)

class UserNotFound : NotFound("This user does not exists")
class UserRoleNotFound : NotFound("This specific user role does not exists. Try one of these: ${UserRole.entries}")
class FileDoesNotExists : NotFound("This file does not exists")
