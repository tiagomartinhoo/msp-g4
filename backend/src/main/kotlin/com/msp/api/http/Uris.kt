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

    // DOCTORS
    const val DOCTORS = "$USERS/doctors"

    const val DOCTOR_BY_ID = "$DOCTORS/{dID}"
}
