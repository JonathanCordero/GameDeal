package com.example.GameDeal.repository;

import com.example.GameDeal.model.GameDeals;
import com.example.GameDeal.model.Store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameDealsRepository extends JpaRepository<GameDeals, Long> {

    List<GameDeals> findByStore(Store store);
    
    @Query("SELECT CONCAT(g.title, '|', g.store.storeName) FROM GameDeals g")
    List<String> findAllTitlesAndStores();
    
    List<GameDeals> findByTitle(String title);
    boolean existsByDealID(String dealID);

    
    boolean existsByTitleAndStore(String title, Store store);
}
