package com.techpower.airbnb.service.impl;

import com.techpower.airbnb.converter.StatisticalConverter;
import com.techpower.airbnb.dto.StatisticalDTO;
import com.techpower.airbnb.entity.StatisticalEntity;
import com.techpower.airbnb.entity.UserEntity;
import com.techpower.airbnb.repository.StatisticalRepository;
import com.techpower.airbnb.repository.UserRepository;
import com.techpower.airbnb.service.IStatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticalService implements IStatisticalService {
    @Autowired
    private StatisticalRepository statisticalRepository;
    @Autowired
    private StatisticalConverter statisticalConverter;
    @Autowired
    private UserRepository userRepository;

    public List<StatisticalDTO> sortStatisticalListByMonth(List<StatisticalDTO> statisticalList) {
        return statisticalList.stream()
                .sorted(Comparator.comparingInt(StatisticalDTO::getMonth))
                .collect(Collectors.toList());
    }

    @Override
    public StatisticalDTO getStatisticalByYearAndMonthAndDay(long idUser, int year, int month, int day) {
        UserEntity userEntity = userRepository.findOneById(idUser);
        StatisticalEntity statisticalEntity = statisticalRepository.findOneByUserAndYearAndMonthAndDay(
                userEntity, year, month, day
        );
        return statisticalConverter.toDTO(statisticalEntity);
    }

    @Override
    public List<StatisticalDTO> getStatisticalByYear(long idUser, int year) {

        List<StatisticalEntity> statisticalEntities = statisticalRepository.findByUser_IdAndYear(idUser, year);
        return sortStatisticalListByMonth(statisticalConverter.mapperTOList(statisticalEntities));
    }
}
