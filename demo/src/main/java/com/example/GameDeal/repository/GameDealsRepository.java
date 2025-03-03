package com.example.GameDeal.repository;

import com.example.GameDeal.model.GameDeals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameDealsRepository extends JpaRepository<GameDeals, Long> {

    List<GameDeals> findByStore(String store);
    
    @Query("SELECT CONCAT(g.title, '|', g.store) FROM GameDeals g")
    List<String> findAllTitlesAndStores();
    
    boolean existsByTitleAndStore(String title, String store);
}
