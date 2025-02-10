package rogue.game.domain.services.balance.impl;

import rogue.game.domain.services.balance.LevelEntitiesBalancer;
import rogue.game.services.stats.StatsService;
import rogue.game.domain.entities.Balancer;
import rogue.game.domain.entities.GameEntity;
import rogue.game.domain.entities.items.Item;
import rogue.game.domain.enums.EnemyType;
import rogue.game.domain.enums.GameDifficulty;
import rogue.game.domain.enums.ItemType;
import rogue.game.domain.enums.LevelAttribute;
import rogue.game.domain.factories.EnemyBuilderFactory;
import rogue.game.domain.factories.ItemBuilderFactory;
import rogue.game.domain.repository.GameRepository;
import rogue.game.domain.entities.Position;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class RogueLevelEntitiesBalancer implements LevelEntitiesBalancer {
    private static final int NUMBER_OF_LEVELS_TO_CHANGE_DIFFICULTY = 2;
    private static final List<ItemType> itemTypes = List.of(ItemType.values());
    private static final List<EnemyType> enemyTypes = List.of(EnemyType.values());

    /**
     * A map defining the probability of spawning each {@link Item} type.
     * <p> Keys represent the item type, and values represent the associated probability (between 0.0 and 1.0).
     */
    private static final Map<ItemType, Double> itemTypesProbabilities = Map.of(
            ItemType.FOOD, 0.4,
            ItemType.SCROLL, 0.4,
            ItemType.WEAPON, 0.3,
            ItemType.POTION, 0.3
    );

    private final Balancer balancer;

    /**
     * A map associating game difficulties with corresponding actions to set that difficulty.
     * <p> Keys represent the game difficulty, and values are lambdas that implement the
     * logic for setting that difficulty.
     */
    private final Map<GameDifficulty, Runnable> difficultyActions = Map.of(
            GameDifficulty.EASY, this::setEasyDifficulty,
            GameDifficulty.NORMAL, this::setNormalDifficulty,
            GameDifficulty.HARD, this::setHardDifficulty
    );

    public RogueLevelEntitiesBalancer(GameRepository gameRepository) {
        balancer = gameRepository.getBalancer().orElseGet(Balancer::new);
        setNormalDifficulty();
    }

    @Override
    public void reset(){
        balancer.setGameDifficulty(GameDifficulty.NORMAL);
        setNormalDifficulty();
    }

    @Override
    public Balancer getBalancer() {
        return balancer;
    }

    @Override
    public void updateDifficulty(StatsService statsService, int coefficient) {
        if(statsService.getLevel() != NUMBER_OF_LEVELS_TO_CHANGE_DIFFICULTY)
            return;

        double difficultyCoefficient = calculateDifficultyCoefficient(statsService);

        GameDifficulty gameDifficulty = balancer.getGameDifficulty();

        if (difficultyCoefficient > 0.8) {
            if(gameDifficulty.equals(GameDifficulty.NORMAL))
                balancer.setGameDifficulty(GameDifficulty.EASY);
            else if(gameDifficulty.equals(GameDifficulty.HARD))
                balancer.setGameDifficulty(GameDifficulty.NORMAL);
        } else if (difficultyCoefficient < 0.2) {
            if(gameDifficulty.equals(GameDifficulty.NORMAL))
                balancer.setGameDifficulty(GameDifficulty.HARD);
            else if(gameDifficulty.equals(GameDifficulty.EASY))
                balancer.setGameDifficulty(GameDifficulty.NORMAL);
        }else {
            balancer.setGameDifficulty(GameDifficulty.NORMAL);
        }

        difficultyActions.get(balancer.getGameDifficulty()).run();
    }

    private void setNormalDifficulty() {
        balancer.setMinNumberOfEnemies(0);
        balancer.setMaxNumberOfEnemies(2);
    }

    private void setHardDifficulty() {
        balancer.setMinNumberOfEnemies(1);
        balancer.setMaxNumberOfEnemies(LevelAttribute.MAX_ENEMIES_PER_ROOM.value);
    }

    private void setEasyDifficulty() {;
        balancer.setMinNumberOfEnemies(0);
        balancer.setMaxNumberOfEnemies(1);
    }

    /**
     * Calculates a difficulty coefficient based on player statistics.
     * <p> A higher coefficient indicates a more difficult game.
     */
     private double calculateDifficultyCoefficient(StatsService stats) {
        final double foodWeight = 0.3;
        final double missedShotsWeight = 0.4;
        final double hitsWeight = 0.3;

        final double coefficient = stats.getCountOfEatenFood() * foodWeight +
                stats.getCountOfMissedShots() * missedShotsWeight -
                stats.getCountOfBlowsInflicted() * hitsWeight;

        // make sure coefficient is between 0 and 1
        return Math.max(0, Math.min(1, coefficient));
    }

    @Override
    public int getCountOfItems(int complexityFactor) {
        int numberOfItems;

        if(balancer.getGameDifficulty().equals(GameDifficulty.HARD))
            numberOfItems = ThreadLocalRandom.current().nextInt(0, LevelAttribute.MAX_ITEMS_PER_ROOM.value);
        else
            numberOfItems = ThreadLocalRandom.current().nextInt(0, LevelAttribute.MAX_ITEMS_PER_ROOM.value + 1);

        return numberOfItems;
    }

    @Override
    public GameEntity spawnItem(int complexityFactor, Position position) {
        double treasureProbability = (double) complexityFactor / (LevelAttribute.FINAL_LEVEL.value - 1) + 0.1;

        ItemType type = null;

        if(ThreadLocalRandom.current().nextDouble() < treasureProbability) {
            type = ItemType.TREASURE;
        }else if(balancer.getGameDifficulty().equals(GameDifficulty.EASY)){
            double totalProbability = itemTypesProbabilities.values().stream().reduce(0.0, Double::sum);

            double rand = ThreadLocalRandom.current().nextDouble() * totalProbability;
            double probability = 0;

            for (Map.Entry<ItemType, Double> e : itemTypesProbabilities.entrySet()) {
                probability += e.getValue();
                if (rand < probability) {
                    type = e.getKey();
                    break;
                }
            }

        }else {
            type = itemTypes.get(ThreadLocalRandom.current().nextInt(itemTypes.size() - 1));
        }

        return (type == ItemType.WEAPON)
                ? ItemBuilderFactory.createComplexityBasedWeaponBuilder()
                    .withComplexity(complexityFactor)
                    .withPosition(position)
                    .build()
                : ItemBuilderFactory.createBaseItemBuilder()
                    .withType(type)
                    .withPosition(position)
                    .build();
        }

    @Override
    public int getCountOfEnemies(int coefficient) {
        if(balancer.getGameDifficulty().equals(GameDifficulty.HARD) && ThreadLocalRandom.current().nextBoolean())
            return 1;

        return ThreadLocalRandom.current().nextInt(balancer.getMinNumberOfEnemies(), balancer.getMaxNumberOfEnemies() + 1);
    }

    @Override
    public GameEntity spawnEnemy(int complexityFactor, Position position) {
        int complexity = complexityFactor;

        GameDifficulty gameDifficulty = balancer.getGameDifficulty();

        if (gameDifficulty.equals(GameDifficulty.HARD))
            complexity = complexityFactor + 3;
        else if (gameDifficulty.equals(GameDifficulty.EASY))
            complexity = (complexityFactor - 3 <= 0) ? complexityFactor : complexityFactor - 3;

        EnemyType type = enemyTypes.get(ThreadLocalRandom.current().nextInt(enemyTypes.size()));

        return EnemyBuilderFactory.createComplexityBasedEnemyBuilder()
                .withType(type)
                .withComplexity(complexity)
                .withPosition(position)
                .build();
    }
}
