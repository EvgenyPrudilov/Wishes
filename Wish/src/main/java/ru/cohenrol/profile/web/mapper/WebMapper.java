package ru.cohenrol.profile.web.mapper;


import org.mapstruct.Mapper;
import ru.cohenrol.profile.domain.model.CourseEnrolled;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = { LocalDateTime.class })
public interface WebMapper {
    CourseEnrolledDto toDto(CourseEnrolled courseEnrolledDto);
}
