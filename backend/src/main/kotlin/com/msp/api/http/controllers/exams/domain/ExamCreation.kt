package com.msp.api.http.controllers.exams.domain

import java.time.LocalDateTime

data class ExamCreation(val examID : String, val examDateTime: LocalDateTime)