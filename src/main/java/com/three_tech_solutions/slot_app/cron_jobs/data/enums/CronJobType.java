package com.three_tech_solutions.slot_app.cron_jobs.data.enums;

public enum CronJobType {
    CREATE_UPCOMING_SPECIFIC_SLOTS("create_upcoming_specific_slots"),
    EXPIRE_PENDING_ABSENCES("expire_pending_absences"),
    CREATE_STUDENTS_MONTHLY_FEES("create_students_monthly_fees");

    private final String value;

    CronJobType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
