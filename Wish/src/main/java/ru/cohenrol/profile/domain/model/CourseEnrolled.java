package ru.cohenrol.profile.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseEnrolled {
    private String studentName;
    private UUID courseId;
    private CourseStatus status;
    private Instant enrolledAt;
}
