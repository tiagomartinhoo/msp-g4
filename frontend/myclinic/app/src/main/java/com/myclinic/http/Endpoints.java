package com.myclinic.http;

public class Endpoints {
    public static final String API_URL = "https://babb-2a01-11-8120-37e0-51a-ed70-2810-6df3.ngrok-free.app/rest";

    // USERS
    public static final String USERS = API_URL + "/users";

    public static final String LOGIN = USERS + "/login";
    public static final String USER_PHOTO = USERS + "/{uid}/photo";

    // PATIENTS
    public static final String PATIENTS = USERS + "/patients";

    public static final String PATIENT_BY_ID = PATIENTS + "/{pID}";

    public static final String CHECK_IN = PATIENTS + "/{pID}/checkIn";

    // DOCTORS
    public static final String DOCTORS = USERS + "/doctors";

    public static final String DOCTOR_BY_ID = DOCTORS + "/{dID}";

    // Appointments
    public static final String APPOINTMENTS = PATIENT_BY_ID + "/appointments";

    public static final String APPOINTMENT_BY_ID = PATIENT_BY_ID + "/appointments/{aID}";

    public static final String SERVICES_AVAILABLE = API_URL + "/services";

    public static final String SERVICE_BY_ID = SERVICES_AVAILABLE + "/{id}";

    public static final String EXAMS_AVAILABLE = API_URL + "/exams";

    public static final String EXAM_BY_ID = EXAMS_AVAILABLE + "/{id}";

    public static final String SCHEDULE_EXAM = PATIENT_BY_ID + "/exams";

    public static final String SCHEDULED_EXAM_BY_ID = PATIENT_BY_ID + "/exams/{eID}";



}
