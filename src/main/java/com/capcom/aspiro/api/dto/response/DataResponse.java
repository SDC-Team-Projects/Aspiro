package com.capcom.aspiro.api.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataResponse<T> {

    private List<T> data;

    public static <T> DataResponse<T> of(List<T> list) {
        return new DataResponse<>(list);
    }
}
