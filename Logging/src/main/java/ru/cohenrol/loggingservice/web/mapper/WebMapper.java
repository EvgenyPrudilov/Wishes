package ru.cohenrol.loggingservice.web.mapper;

import lombok.extern.java.Log;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.cohenrol.loggingservice.datasource.model.LogEntity;
import ru.cohenrol.loggingservice.web.model.AddLogRequestDto;
import ru.cohenrol.loggingservice.web.model.AddLogsRequestDto;
import ru.cohenrol.loggingservice.web.model.GetLogResponseDto;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", imports = { Instant.class })
public interface WebMapper {
    LogEntity toLog(AddLogRequestDto request);
    List<LogEntity> toLogList(List<AddLogRequestDto> list);
    default List<LogEntity> toLog(AddLogsRequestDto request) {
        if (request == null || request.getLogs() == null) {
            return Collections.emptyList();
        }
        return toLogList(request.getLogs());
    }

    GetLogResponseDto toDto(LogEntity logEntity);
}
