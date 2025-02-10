# Rogue — Java_Bootcamp

## Project Overview

The goal of this project is to develop a console-based "roguelike" game using Java, based on the classic *Rogue* game from 1980. The game should feature randomly generated dungeons and basic RPG mechanics. It should be designed as a fun and challenging game where players explore dungeons, battle enemies, collect items, and level up their character.

This game will use the **JCurses** library for the user interface, allowing the display of text-based graphics and a clean console-based interface.

## Technologies and Libraries

- **Programming Language**: Java (version 21)
- **Library for Console UI**: JCurses, which will be used for creating the text-based interface and handling user input in the console.
  
## Project Architecture

The project should follow a **clean architecture** with clear separation of concerns:
- **Presentation Layer**: Handles user interface and displays game state (implemented using JCurses).
- **Business Logic Layer**: Handles the game mechanics, such as combat, character progression, and dungeon generation.
- **Data Layer**: Handles persistence of game data (e.g., saving/loading game state, managing scores, and stats).

The game should be implemented using **Object-Oriented Programming** (OOP) principles, with distinct classes representing different game entities.

## Gameplay Mechanics

### Game Structure:
- The game consists of **21 levels**, with each level containing **9 rooms** connected by **corridors**.
- **Rooms** are randomly generated, and each room can contain various entities like enemies, treasure chests, and doors.
- The game features **fog of war**: rooms and corridors that the player has not explored are hidden from view.

### Character:
- The player controls a character with various stats such as:
  - **Health**: Determines the player's life points.
  - **Strength**: Influences the character's ability to deal damage.
  - **Dexterity**: Affects the character's ability to dodge attacks.
  - **Experience**: Earned by defeating enemies and completing levels.

- The character can **level up** after gaining enough experience points, improving their stats.

### Combat:
- The combat system is **turn-based**, with each turn allowing the player to either move, attack, or interact with the environment.
- **Chance to hit/miss** is determined by the player's stats (Strength, Dexterity) and the stats of the enemy.
- **Enemies** become progressively stronger with each level, introducing increasing difficulty.

### Inventory and Items:
- The player can collect various **items**, including weapons, potions, armor, and keys.
- **Items** can be used to heal, enhance stats, or aid in combat.
- The player has an **inventory system** that manages the items they collect and use. This is represented by a **backpack** that can store a limited number of items.

### Random Level Generation:
- Each level of the dungeon is **procedurally generated**, meaning the layout of rooms, corridors, and item placements will change every time the game is played.
- **Random encounters** with enemies, treasure, and traps will make each playthrough unique.

### Keys and Doors:
- **Colored keys** are scattered throughout the levels, which are needed to unlock **doors** of corresponding colors, allowing the player to progress through different areas of the dungeon.

## Saving and Loading Game State

- After each level, the game state (including the player's progress, current level, and inventory) should be saved in a **JSON file**.
- The game should allow for **saving and loading** progress, so players can pick up where they left off without losing any data.
- This feature also ensures that players can return to previously completed levels or reattempt higher levels with the same character.

## Extra Features

- **Game Difficulty Scaling**: The game should dynamically adjust the difficulty based on the player's progress. As the player advances through levels, the enemies will become stronger, requiring the player to upgrade their equipment and level up to keep up.
- **Procedural Dungeon Layouts**: Each game session should offer a new dungeon layout, providing variety and replayability.
- **User Interface**: The JCurses library will be used to implement the **text-based user interface**, displaying the dungeon map, player stats, inventory, and messages to the player. A simple, clean interface will help maintain the roguelike atmosphere.

## Key Entities and Classes

### Core Game Entities:
- **GameSession**: Manages the game state, including the current level and player stats.
- **DungeonLevel**: Represents a single level of the dungeon, including its rooms, enemies, and layout.
- **Room**: Represents a room within a dungeon level, potentially containing items, enemies, and traps.
- **Corridor**: Represents a corridor connecting rooms within a level.
- **Character**: The player character, with attributes like health, strength, experience, and inventory.
- **Enemy**: Represents non-player characters (NPCs) that the player will encounter and fight in the dungeon.
- **Item**: Objects that the player can collect, use, or equip to affect their stats or abilities.
- **Backpack**: The player's inventory system, holding items collected during the game.

## Conclusion

This project will provide a fun, engaging, and challenging experience as players explore dungeons, level up their characters, and defeat enemies. The use of random generation and the JCurses library will ensure that each playthrough is unique, with plenty of opportunities for strategic decision-making and character growth.

By following a clean architecture and implementing core gameplay mechanics, the project will serve as a great learning experience for understanding game development principles, Java programming, and working with libraries for console-based applications.