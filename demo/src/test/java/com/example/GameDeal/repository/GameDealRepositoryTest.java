package com.example.GameDeal.repository;

import com.example.GameDeal.model.GameDeals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest  // Loads only JPA-related components, using an in-memory database
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GameDealRepositoryTest {

    @Autowired
    private GameDealsRepository gameDealRepository;

    @Test
    public void testSaveAndFindAll() {
        // Arrange: Create a game deal and save it
        GameDeals gameDeal = new GameDeals("Elden Ring", "Steam", 59.99, 39.99, "https://steam.com/eldenring");
        gameDealRepository.save(gameDeal);

        // Act: Retrieve all deals from the database
        List<GameDeals> deals = gameDealRepository.findAll();

        // Assert: Verify the deal was saved correctly
        assertThat(deals).isNotEmpty();
        assertThat(deals).hasSize(1);
        assertThat(deals.get(0).getTitle()).isEqualTo("Elden Ring");
    }

    @Test
    public void testFindByStore() {
        // Arrange: Add two game deals
        gameDealRepository.save(new GameDeals("Cyberpunk 2077", "Steam", 59.99, 29.99, "https://steam.com/cyberpunk"));
        gameDealRepository.save(new GameDeals("The Witcher 3", "GOG", 39.99, 9.99, "https://gog.com/witcher3"));

        // Act: Find all deals for Steam
        List<GameDeals> steamDeals = gameDealRepository.findByStore("Steam");

        // Assert: Verify only Steam deals are returned
        assertThat(steamDeals).hasSize(1);
        assertThat(steamDeals.get(0).getTitle()).isEqualTo("Cyberpunk 2077");
    }
}
