package ru.cohenrol.profile.web.mapper;


import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = { LocalDateTime.class })
public interface WebMapper {
//    CourseEnrolledDto toDto(CourseEnrolled courseEnrolledDto);
}
