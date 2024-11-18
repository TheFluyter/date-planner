package com.thefluyter.dateplanner.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Friend {

    @JsonProperty("name")
    private String name;

    @JsonProperty("date-counter")
    private int dateCounter;

    @JsonProperty("last-date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastDate;

}
