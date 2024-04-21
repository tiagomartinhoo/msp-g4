package com.msp.api.http.pipeline.exceptionHandler.exceptions

abstract class Forbidden(msg: String) : Exception(msg)

class ForbiddenRequest : Forbidden("You cannot access this resource")

class NotYourAppointment : Forbidden("Not your appointment")

class NotYourExam : Forbidden("Not your exam")
