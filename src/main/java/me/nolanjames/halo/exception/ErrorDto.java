package me.nolanjames.halo.exception;

import java.util.Date;
import java.util.List;

public record ErrorDto(
        Date timestamp,
        int Status,
        List<String> errors,
        String path
) {

}
