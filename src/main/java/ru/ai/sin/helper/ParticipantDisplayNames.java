package ru.ai.sin.helper;

import ru.ai.sin.logic.recruiter.RecruiterEnt;
import ru.ai.sin.logic.student.StudentEnt;
import ru.ai.sin.models.embeddables.UserInformation;

public final class ParticipantDisplayNames {

    private ParticipantDisplayNames() {}

    public static String recruiter(RecruiterEnt recruiter) {
        if (recruiter == null) {
            return null;
        }
        UserInformation info = recruiter.getUserInformation();
        String person = formatPerson(info != null ? info.getFirstName() : null, info != null ? info.getLastName() : null);
        String company = recruiter.getCompanyName();
        if (person != null && company != null) {
            return person + " (" + company + ")";
        }
        if (company != null) {
            return company;
        }
        return person;
    }

    public static String student(StudentEnt student) {
        if (student == null) {
            return null;
        }
        UserInformation info = student.getUserInformation();
        return formatPerson(info != null ? info.getFirstName() : null, info != null ? info.getLastName() : null);
    }

    private static String formatPerson(String first, String last) {
        String f = first != null ? first.trim() : "";
        String l = last != null ? last.trim() : "";
        String combined = (f + " " + l).trim();
        return combined.isEmpty() ? null : combined;
    }
}
