package com.msp.api.http

object Uris {

    private const val PREFIX = "/rest"

    // USERS
    const val USERS = "$PREFIX/users"

    const val LOGIN = "$USERS/login"
    const val USER_PHOTO = "$USERS/{uid}/photo"

    // PATIENTS
    const val PATIENTS = "$USERS/patients"

    const val PATIENT_BY_ID = "$PATIENTS/{pID}"

    const val CHECK_IN = "$PATIENTS/{pID}/checkIn"


    // DOCTORS
    const val DOCTORS = "$USERS/doctors"

    const val DOCTOR_BY_ID = "$DOCTORS/{dID}"

    // Appointments
    const val APPOINTMENTS = "$PATIENT_BY_ID/appointments"

    const val APPOINTMENT_BY_ID = "$PATIENT_BY_ID/appointments/{aID}"

    const val SERVICES_AVAILABLE = "$PREFIX/services"

    const val SERVICE_BY_ID = "$SERVICES_AVAILABLE/{id}"

    const val EXAMS_AVAILABLE = "$PREFIX/exams"

    const val EXAM_BY_ID = "$EXAMS_AVAILABLE/{id}"

    const val SCHEDULE_EXAM = "$PATIENT_BY_ID/exams"

    const val SCHEDULED_EXAM_BY_ID = "$PATIENT_BY_ID/exams/{eID}"
}
