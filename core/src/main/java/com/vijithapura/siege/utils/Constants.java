package com.vijithapura.siege.utils;

public class Constants {
    // Screen & Map
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final int MAP_WIDTH = 3000;
    public static final int MAP_HEIGHT = 2500;
    public static final int TILE_SIZE = 32;

    // Fortress
    public static final int FORTRESS_X = 1500;
    public static final int FORTRESS_Y = 1250;
    public static final int FORTRESS_SIZE = 500;
    public static final int GATE_WIDTH = 80;
    public static final int GATE_HEIGHT = 30;
    public static final float GATE_HEALTH = 800f;

    // Unit Stats - Workers
    public static final float WORKER_SPEED = 85f;
    public static final float WORKER_HEALTH = 80f;
    public static final float WORKER_GATHER_RATE = 10f;
    public static final float WORKER_GATHER_TIME = 2.5f;

    // Unit Stats - Soldiers
    public static final float SOLDIER_SPEED = 110f;
    public static final float SOLDIER_HEALTH = 120f;
    public static final float SOLDIER_DAMAGE = 15f;
    public static final float SOLDIER_ATTACK_RANGE = 45f;
    public static final float SOLDIER_ATTACK_COOLDOWN = 1.0f;

    // Unit Stats - Archers
    public static final float ARCHER_SPEED = 100f;
    public static final float ARCHER_HEALTH = 80f;
    public static final float ARCHER_DAMAGE = 20f;
    public static final float ARCHER_ATTACK_RANGE = 150f;
    public static final float ARCHER_ATTACK_COOLDOWN = 1.5f;

    // Unit Stats - Champions
    public static final float CHAMPION_SPEED = 130f;
    public static final float CHAMPION_HEALTH = 250f;
    public static final float CHAMPION_DAMAGE = 35f;
    public static final float CHAMPION_ATTACK_RANGE = 55f;
    public static final float CHAMPION_ATTACK_COOLDOWN = 0.8f;

    // Unit Stats - Elephant
    public static final float ELEPHANT_SPEED = 70f;
    public static final float ELEPHANT_HEALTH = 400f;
    public static final float ELEPHANT_DAMAGE = 60f;
    public static final float ELEPHANT_ATTACK_RANGE = 40f;
    public static final float ELEPHANT_CHARGE_MULTIPLIER = 5.0f;

    // Unit Stats - Cavalry
    public static final float CAVALRY_SPEED = 180f;
    public static final float CAVALRY_HEALTH = 150f;
    public static final float CAVALRY_DAMAGE = 25f;
    public static final float CAVALRY_ATTACK_RANGE = 50f;

    // Unit Stats - Siege Ram
    public static final float RAM_SPEED = 40f;
    public static final float RAM_HEALTH = 300f;
    public static final float RAM_DAMAGE = 100f;
    public static final float RAM_GATE_BONUS = 3.0f;

    // Resources
    public static final int STARTING_GOLD = 1000;
    public static final int STARTING_WOOD = 300;
    public static final int STARTING_STONE = 200;
    public static final int STARTING_FOOD = 500;
    public static final int MAX_POPULATION = 100;

    // Unit Costs
    public static final int WORKER_COST_GOLD = 50;
    public static final int WORKER_COST_FOOD = 25;

    public static final int SOLDIER_COST_GOLD = 100;
    public static final int SOLDIER_COST_WOOD = 30;
    public static final int SOLDIER_COST_FOOD = 50;

    public static final int ARCHER_COST_GOLD = 120;
    public static final int ARCHER_COST_WOOD = 50;
    public static final int ARCHER_COST_FOOD = 40;

    public static final int CAVALRY_COST_GOLD = 200;
    public static final int CAVALRY_COST_FOOD = 80;

    public static final int CHAMPION_COST_GOLD = 400;
    public static final int CHAMPION_COST_WOOD = 100;
    public static final int CHAMPION_COST_FOOD = 100;

    public static final int RAM_COST_GOLD = 300;
    public static final int RAM_COST_WOOD = 200;

    // Building Costs
    public static final int BARRACKS_COST_GOLD = 200;
    public static final int BARRACKS_COST_WOOD = 150;

    public static final int ARCHERY_COST_GOLD = 250;
    public static final int ARCHERY_COST_WOOD = 200;

    public static final int STABLE_COST_GOLD = 300;
    public static final int STABLE_COST_WOOD = 200;

    public static final int WORKSHOP_COST_GOLD = 400;
    public static final int WORKSHOP_COST_WOOD = 300;
    public static final int WORKSHOP_COST_STONE = 100;

    public static final int SIEGE_WORKSHOP_COST_GOLD = 500;
    public static final int SIEGE_WORKSHOP_COST_WOOD = 400;
    public static final int SIEGE_WORKSHOP_COST_STONE = 200;

    // Training Times
    public static final float WORKER_TRAINING_TIME = 2.5f;
    public static final float SOLDIER_TRAINING_TIME = 3.5f;
    public static final float ARCHER_TRAINING_TIME = 4.0f;
    public static final float CAVALRY_TRAINING_TIME = 5.0f;
    public static final float CHAMPION_TRAINING_TIME = 8.0f;
    public static final float RAM_TRAINING_TIME = 10.0f;

    // Building Times
    public static final float BUILDING_TIME = 15.0f;
    public static final float COMMAND_CENTER_TIME = 20.0f;

    // Upgrades
    public static final int UPGRADE_ATTACK_COST = 300;
    public static final int UPGRADE_DEFENSE_COST = 300;
    public static final int UPGRADE_SPEED_COST = 250;
    public static final float UPGRADE_BONUS = 0.15f;

    // Wave System
    public static final float WAVE_INTERVAL = 45f;
    public static final int BASE_ENEMY_COUNT = 5;
    public static final float ENEMY_SCALING = 1.5f;

    // AI Behavior
    public static final float AI_UPDATE_INTERVAL = 0.5f;
    public static final float AI_ATTACK_RANGE = 250f;
    public static final float AI_DEFEND_RANGE = 400f;
    public static final float AI_RETREAT_HEALTH = 0.3f;

    // Fog of War
    public static final float VISION_RANGE = 200f;
    public static final float BUILDING_VISION = 250f;

    // Formation System
    public static final float FORMATION_SPACING = 45f;
    public static final float FORMATION_ROW_OFFSET = 50f;

    // Game Balance
    public static final float GAME_TIME_LIMIT = 1800f; // 30 minutes
    public static final int VICTORY_GOLD_BONUS = 5000;
    public static final float XP_MULTIPLIER = 1.2f;
    public static final int MAX_LEVEL = 10;

    // Camera
    public static final float CAMERA_SPEED = 450f;
    public static final float CAMERA_ZOOM_MIN = 0.3f;
    public static final float CAMERA_ZOOM_MAX = 0.8f;
    public static final float CAMERA_EDGE_SCROLL = 30f;

    // Particle Effects
    public static final int BLOOD_PARTICLES = 8;
    public static final int DUST_PARTICLES = 5;
    public static final float PARTICLE_LIFETIME = 1.5f;

    // Sound
    public static final float MASTER_VOLUME = 0.8f;
    public static final float MUSIC_VOLUME = 0.6f;
    public static final float SFX_VOLUME = 0.7f;

    // UI
    public static final float MINIMAP_SIZE = 200f;
    public static final int HOTKEY_GROUPS = 9;
    public static final float TOOLTIP_DELAY = 0.5f;
    public static final int WORKER_COST_WOOD = 0;
}
