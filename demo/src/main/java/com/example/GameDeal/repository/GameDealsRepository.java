package com.example.GameDeal.repository;

import com.example.GameDeal.model.GameDeals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameDealsRepository extends JpaRepository<GameDeals, Long> {

    // Custom query to find deals by store name
    List<GameDeals> findByStore(String store);
}
