package com.myclinic.http;

public class Endpoints {
    public static final String API_URL = "https://api-dot-myclinic-424418.ew.r.appspot.com/rest";

    // USERS
    public static final String USERS = API_URL + "/users";

    public static final String LOGIN = USERS + "/login";

    public static String patientUserPhoto(String id) {
        return USERS + "/" + id + "/photo";
    }

    // PATIENTS
    public static final String PATIENTS = USERS + "/patients";

    public static String patientByID(String id) {
        return PATIENTS + "/" + id;
    }

    public static String checkIn(String pID) {
        return patientByID(pID) + "/checkIn";
    }

    // DOCTORS
    public static final String DOCTORS = USERS + "/doctors";

    public static String doctorByID(String id) {
        return DOCTORS + "/" + id;
    }

    // APPOINTMENTS
    public static String appointments(String pID) {
        return patientByID(pID) + "/appointments";
    }

    public static String appointmentByID(String pID, String aID) {
        return appointments(pID) + "/" + aID;
    }

    public static String cancelAppointment(String pID, String aID) {
        return appointmentByID(pID, aID) + "/cancel";
    }

    // SERVICES
    public static final String SERVICES_AVAILABLE = API_URL + "/services";

    public static String serviceByID(String id) {
        return SERVICES_AVAILABLE + "/" + id;
    }

    // EXAMS
    public static final String EXAMS_AVAILABLE = API_URL + "/exams";

    public static String examByID(String id) {
        return EXAMS_AVAILABLE + "/" + id;
    }

    public static String exams(String pID) {
        return patientByID(pID) + "/exams";
    }

    public static String scheduleExamByID(String pID, String eID) {
        return exams(pID) + "/" + eID;
    }

}
