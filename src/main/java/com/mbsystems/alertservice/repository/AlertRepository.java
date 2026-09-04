package com.mbsystems.alertservice.repository;

import com.mbsystems.alertservice.entity.Alert;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends ListCrudRepository<Alert, Long> {
}
