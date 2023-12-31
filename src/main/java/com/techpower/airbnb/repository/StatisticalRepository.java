package com.techpower.airbnb.repository;

import com.techpower.airbnb.entity.StatisticalEntity;
import com.techpower.airbnb.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticalRepository extends JpaRepository<StatisticalEntity, Long> {
    StatisticalEntity findOneByUserAndYearAndMonthAndDay(UserEntity userEntity, int year, int month, int day);

    List<StatisticalEntity> findByUser_IdAndYear(Long id, int year);

    List<StatisticalEntity> findByYear(int year);

    List<StatisticalEntity> findAllByUserAndYear(UserEntity userEntity, int year);
}
