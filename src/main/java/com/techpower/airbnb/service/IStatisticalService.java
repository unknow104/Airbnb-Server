package com.techpower.airbnb.service;

import com.techpower.airbnb.dto.StatisticalDTO;

import java.util.List;

public interface IStatisticalService {
    StatisticalDTO getStatisticalByYearAndMonthAndDay(long idUser, int year, int month, int day);
    List<StatisticalDTO> getStatisticalByYear(long idUser, int year);
}
