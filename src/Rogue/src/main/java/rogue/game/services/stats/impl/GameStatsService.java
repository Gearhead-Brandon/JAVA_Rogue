package rogue.game.services.stats.impl;

import rogue.game.services.stats.StatsService;
import rogue.game.domain.entities.GameStats;
import rogue.game.domain.repository.GameRepository;

import java.util.List;

public class GameStatsService implements StatsService {
    private final GameStats totalGameStats;
    private final GameStats tempGameStats;
    private final GameRepository gameRepository;

    public GameStatsService(GameRepository gameRepository) {
        totalGameStats = gameRepository.getTotalGameStats().orElseGet(GameStats::new);
        tempGameStats = gameRepository.getCurrentGameStats().orElseGet(GameStats::new);
        this.gameRepository = gameRepository;

        if(totalGameStats.getLevel() == 0)
            totalGameStats.setLevel(1);
    }

    @Override
    public List<String> getListOfStats() {
        return gameRepository.getListOfStats().stream()
                .map(GameStats::toString)
                .toList();
    }

    @Override
    public int getLevel() {
        return tempGameStats.getLevel();
    }

    @Override
    public int getCountOfEatenFood() {
        return tempGameStats.getCountOfEatenFood();
    }

    @Override
    public int getCountOfMissedShots() {
        return tempGameStats.getCountOfMissedShots();
    }

    @Override
    public int getCountOfBlowsInflicted() {
        return tempGameStats.getCountOfBlowsInflicted();
    }

    @Override
    public void addTreasureAmount(int treasure) {
        tempGameStats.setTreasureAmount(tempGameStats.getTreasureAmount() + treasure);
    }

    @Override
    public void addLevel() {
        tempGameStats.setLevel(tempGameStats.getLevel() + 1);
    }

    @Override
    public void addPassedCell() {
        tempGameStats.setCountOfPassedCells(tempGameStats.getCountOfPassedCells() + 1);
    }

    @Override
    public void addEatenFood() {
        tempGameStats.setCountOfEatenFood(tempGameStats.getCountOfEatenFood() + 1);
    }

    @Override
    public void addReadScroll() {
        tempGameStats.setCountOfReadScrolls(tempGameStats.getCountOfReadScrolls() + 1);
    }

    @Override
    public void addPotionDrank() {
        tempGameStats.setCountOfPotionsDrank(tempGameStats.getCountOfPotionsDrank() + 1);
    }

    @Override
    public void addBlowInflicted() {
        tempGameStats.setCountOfBlowsInflicted(tempGameStats.getCountOfBlowsInflicted() + 1);
    }

    @Override
    public void addMissedShot() {
        tempGameStats.setCountOfMissedShots(tempGameStats.getCountOfMissedShots() + 1);
    }

    @Override
    public void addDefeatedEnemy() {
        tempGameStats.setCountOfDefeatedEnemies(tempGameStats.getCountOfDefeatedEnemies() + 1);
    }

    @Override
    public void prepareData() {
        if(tempGameStats.getLevel() == GameStats.NUMBER_OF_LEVELS_FOR_DATA_TRANSFER)
            totalGameStats.merge(tempGameStats);
    }

    @Override
    public void reset() {
        totalGameStats.merge(tempGameStats);

        gameRepository.insert(totalGameStats);

        totalGameStats.reset();
        totalGameStats.setLevel(1);
    }

    @Override
    public void save() {
        gameRepository.update(totalGameStats, tempGameStats);
    }
}
