package com.msp.api.http.pipeline.exceptionHandler.exceptions

import com.msp.api.domain.UserRole

abstract class NotFound(msg: String) : Exception(msg)

class UserNotFound : NotFound("This user does not exists")
class UserRoleNotFound : NotFound("This specific user role does not exists. Try one of these: ${UserRole.entries}")
class FileDoesNotExists : NotFound("This file does not exists")

class AppointmentNotFound : NotFound("This Appointment does not exists")

class ServiceNotFound : NotFound("This service does not exists")

class ExamNotFound : NotFound("This exam Does not exists")

class NotCheckIn : NotFound("You are not check in yet")
