package com.vijithapura.siege.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.vijithapura.siege.utils.TextureManager;

public class Unit {
    // Identity
    protected int id;
    protected Vector2 position;
    protected Vector2 targetPosition;
    protected String name;
    protected String spriteKey;
    protected Color color;
    protected float size;
    protected UnitType type;

    // Stats
    protected float health;
    protected float maxHealth;
    protected float speed;
    protected float baseSpeed;
    protected float attackDamage;
    protected float baseDamage;
    protected float attackRange;
    protected float attackCooldown;
    protected float armor;
    protected float baseArmor;

    // State
    protected boolean isAlive;
    protected boolean isSelected;
    protected boolean isMoving;
    protected boolean isAttacking;
    protected boolean isGathering;
    protected boolean isRetreating;
    protected UnitStance stance;

    // Combat
    protected Unit currentTarget;
    protected Gate targetGate;
    protected float attackTimer;
    protected float attackAnimTimer;

    // Pathfinding
    protected ArrayList<Vector2> path;
    protected int pathIndex;

    // Experience & Leveling
    protected int level;
    protected int experience;
    protected int experienceToNextLevel;
    protected int killCount;

    // Buffs & Debuffs
    protected float speedMultiplier;
    protected float damageMultiplier;
    protected float armorMultiplier;
    protected ArrayList<Buff> activeBuffs;

    // Team
    protected int teamId; // 0 = player, 1 = enemy

    // Formation
    protected int formationRow;
    protected int formationCol;
    protected Vector2 formationOffset;

    // Rally Point
    protected Vector2 rallyPoint;

    public enum UnitType {
        WORKER, SOLDIER, ARCHER, CAVALRY, CHAMPION, ELEPHANT, SIEGE_RAM
    }

    public enum UnitStance {
        AGGRESSIVE, // Attack everything in range
        DEFENSIVE,  // Only attack when attacked
        STAND_GROUND, // Don't move, attack in range
        PASSIVE     // Never attack
    }

    public static class Buff {
        public String name;
        public float duration;
        public float speedBonus;
        public float damageBonus;
        public float armorBonus;

        public Buff(String name, float duration, float speed, float damage, float armor) {
            this.name = name;
            this.duration = duration;
            this.speedBonus = speed;
            this.damageBonus = damage;
            this.armorBonus = armor;
        }
    }

    public Unit(float x, float y, float health, float speed, float damage, Color color, String name) {
        this.id = (int)(Math.random() * 1000000);
        this.position = new Vector2(x, y);
        this.targetPosition = new Vector2(x, y);
        this.health = health;
        this.maxHealth = health;
        this.speed = speed;
        this.baseSpeed = speed;
        this.attackDamage = damage;
        this.baseDamage = damage;
        this.color = new Color(color);
        this.name = name;

        this.size = 20;
        this.armor = 0;
        this.baseArmor = 0;
        this.isAlive = true;
        this.isSelected = false;
        this.isMoving = false;
        this.isAttacking = false;
        this.isGathering = false;
        this.isRetreating = false;
        this.stance = UnitStance.AGGRESSIVE;

        this.attackRange = 50f;
        this.attackCooldown = 1.0f;
        this.attackTimer = 0;
        this.attackAnimTimer = 0;

        this.path = new ArrayList<>();
        this.pathIndex = 0;

        this.level = 1;
        this.experience = 0;
        this.experienceToNextLevel = 100;
        this.killCount = 0;

        this.speedMultiplier = 1.0f;
        this.damageMultiplier = 1.0f;
        this.armorMultiplier = 1.0f;
        this.activeBuffs = new ArrayList<>();

        this.teamId = 0;
        this.formationOffset = new Vector2();
        this.rallyPoint = null;
    }

    public void update(float delta) {
        if (!isAlive) return;

        // Update timers
        if (attackTimer > 0) attackTimer -= delta;
        if (attackAnimTimer > 0) {
            attackAnimTimer -= delta;
            if (attackAnimTimer <= 0) isAttacking = false;
        }

        // Update buffs
        updateBuffs(delta);

        // Update movement
        if (!path.isEmpty()) {
            followPath(delta);
        } else if (isMoving) {
            moveToTarget(delta);
        }

        // Update combat behavior based on stance
        if (stance != UnitStance.PASSIVE) {
            updateCombat(delta);
        }

        // Auto-retreat if low health
        if (health < maxHealth * 0.2f && !isRetreating && stance != UnitStance.STAND_GROUND) {
            initiateRetreat();
        }
    }

    protected void updateBuffs(float delta) {
        speedMultiplier = 1.0f;
        damageMultiplier = 1.0f;
        armorMultiplier = 1.0f;

        ArrayList<Buff> expiredBuffs = new ArrayList<>();
        for (Buff buff : activeBuffs) {
            buff.duration -= delta;
            if (buff.duration <= 0) {
                expiredBuffs.add(buff);
            } else {
                speedMultiplier += buff.speedBonus;
                damageMultiplier += buff.damageBonus;
                armorMultiplier += buff.armorBonus;
            }
        }
        activeBuffs.removeAll(expiredBuffs);

        // Apply multipliers
        speed = baseSpeed * speedMultiplier;
        attackDamage = baseDamage * damageMultiplier;
        armor = baseArmor * armorMultiplier;
    }

    protected void updateCombat(float delta) {
        // Check if current target is still valid
        if (currentTarget != null && (!currentTarget.isAlive() ||
            position.dst(currentTarget.position) > attackRange * 1.5f)) {
            currentTarget = null;
        }

        // Auto-attack in aggressive stance
        if (stance == UnitStance.AGGRESSIVE && currentTarget == null) {
            // This will be handled by combat system
        }
    }

    protected void followPath(float delta) {
        if (pathIndex >= path.size()) {
            path.clear();
            pathIndex = 0;
            isMoving = false;
            return;
        }

        Vector2 waypoint = path.get(pathIndex);
        Vector2 direction = new Vector2(waypoint).sub(position);
        float distance = direction.len();

        if (distance < 5f) {
            pathIndex++;
            if (pathIndex >= path.size()) {
                path.clear();
                pathIndex = 0;
                isMoving = false;
            }
        } else {
            direction.nor();
            position.add(direction.scl(speed * delta));
            isMoving = true;
        }
    }

    protected void moveToTarget(float delta) {
        Vector2 direction = new Vector2(targetPosition).sub(position);
        float distance = direction.len();

        if (distance < speed * delta || distance < 2f) {
            position.set(targetPosition);
            isMoving = false;
        } else {
            direction.nor();
            position.add(direction.scl(speed * delta));
            isMoving = true;
        }
    }

    public void render(ShapeRenderer renderer, SpriteBatch batch, TextureManager textureManager) {
        if (!isAlive) return;

        boolean hasSprite = spriteKey != null && textureManager != null && textureManager.hasTexture(spriteKey);
        float renderSize = hasSprite ? size * 4 : size;

        // === RENDER UNIT BODY ===
        if (hasSprite) {
            // ShapeRenderer should be active when this is called
            // End it, use batch, then restart it
            renderer.end();
            batch.begin();

            Sprite sprite = textureManager.getSprite(spriteKey);
            if (sprite != null) {
                sprite.setPosition(position.x - renderSize/2, position.y - renderSize/2);
                sprite.setSize(renderSize, renderSize);

                // Team color tint
                if (teamId == 1) {
                    sprite.setColor(1f, 0.6f, 0.6f, 1f); // Red tint for enemies
                } else {
                    sprite.setColor(Color.WHITE);
                }

                if (isAttacking) {
                    sprite.setScale(1.15f);
                } else {
                    sprite.setScale(1.0f);
                }

                sprite.draw(batch);
            }

            batch.end();
            renderer.begin(ShapeRenderer.ShapeType.Filled);
        } else {
            // Just use the already-active ShapeRenderer
            renderer.setColor(color);
            renderer.circle(position.x, position.y, size);
        }

        // === SELECTION RING ===
        if (isSelected) {
            renderer.end();
            renderer.begin(ShapeRenderer.ShapeType.Line);
            Gdx.gl.glLineWidth(3);
            renderer.setColor(teamId == 0 ? Color.YELLOW : Color.RED);
            renderer.circle(position.x, position.y, renderSize/2 + 5, 30);
            renderer.end();
            renderer.begin(ShapeRenderer.ShapeType.Filled);
        }

        // === ATTACK LINE ===
        if (isAttacking && currentTarget != null && currentTarget.isAlive()) {
            renderer.end();
            renderer.begin(ShapeRenderer.ShapeType.Line);
            Gdx.gl.glLineWidth(2);
            renderer.setColor(Color.RED);
            renderer.line(position.x, position.y, currentTarget.position.x, currentTarget.position.y);
            renderer.end();
            renderer.begin(ShapeRenderer.ShapeType.Filled);
        }

        // === HEALTH BAR ===
        renderHealthBar(renderer, renderSize);

        // === LEVEL INDICATOR ===
        if (level > 1) {
            renderer.setColor(Color.GOLD);
            float starSize = 3;
            int stars = Math.min(level - 1, 5);
            for (int i = 0; i < stars; i++) {
                renderer.circle(position.x - (stars * starSize) + i * (starSize * 3),
                    position.y + renderSize/2 + 18, starSize);
            }
        }

        // === BUFF INDICATORS ===
        renderBuffIndicators(renderer, renderSize);
    }

    protected void renderHealthBar(ShapeRenderer renderer, float renderSize) {
        float barWidth = renderSize;
        float healthPercent = health / maxHealth;
        float barY = position.y + renderSize/2 + 8;

        // Background
        renderer.setColor(0.2f, 0.2f, 0.2f, 0.8f);
        renderer.rect(position.x - barWidth/2, barY, barWidth, 5);

        // Health
        Color healthColor = healthPercent > 0.6f ? Color.GREEN :
            healthPercent > 0.3f ? Color.ORANGE : Color.RED;
        renderer.setColor(healthColor);
        renderer.rect(position.x - barWidth/2, barY, barWidth * healthPercent, 5);

        // Armor indicator
        if (armor > 0) {
            renderer.setColor(Color.CYAN);
            renderer.rect(position.x - barWidth/2, barY - 3, barWidth * (armor / (maxHealth * 0.5f)), 2);
        }
    }

    protected void renderBuffIndicators(ShapeRenderer renderer, float renderSize) {
        if (activeBuffs.isEmpty()) return;

        float indicatorSize = 4;
        float startX = position.x - renderSize/2;
        float y = position.y - renderSize/2 - 8;

        for (int i = 0; i < Math.min(activeBuffs.size(), 5); i++) {
            renderer.setColor(Color.CYAN);
            renderer.circle(startX + i * (indicatorSize * 2 + 2), y, indicatorSize);
        }
    }

    public void moveTo(float x, float y) {
        targetPosition.set(x, y);
        path.clear();
        pathIndex = 0;
        isMoving = true;
        currentTarget = null;
        isRetreating = false;
    }

    public void setPath(ArrayList<Vector2> newPath) {
        if (newPath != null && !newPath.isEmpty()) {
            this.path = new ArrayList<>(newPath);
            this.pathIndex = 0;
            this.isMoving = true;
            this.currentTarget = null;
        }
    }

    public void takeDamage(float damage) {
        float actualDamage = Math.max(1, damage - armor);
        health -= actualDamage;

        if (health <= 0) {
            health = 0;
            isAlive = false;
            onDeath();
        }
    }

    protected void onDeath() {
        Gdx.app.log("Unit", name + " has been defeated!");
    }

    public boolean canAttack() {
        return attackTimer <= 0 && isAlive;
    }

    public void attack(Unit target) {
        if (canAttack() && target != null && target.isAlive()) {
            target.takeDamage(attackDamage);
            attackTimer = attackCooldown;
            currentTarget = target;
            isAttacking = true;
            attackAnimTimer = 0.3f;

            if (!target.isAlive()) {
                gainExperience(50);
                killCount++;
            }
        }
    }

    public void attackGate(Gate gate) {
        if (canAttack() && gate != null && !gate.isDestroyed()) {
            float damage = attackDamage;
            // Siege units do bonus damage to gates
            if (type == UnitType.SIEGE_RAM) {
                damage *= 3.0f;
            }
            gate.takeDamage(damage);
            attackTimer = attackCooldown;
            targetGate = gate;
            isAttacking = true;
            attackAnimTimer = 0.3f;
        }
    }

    public void gainExperience(int exp) {
        experience += exp;
        if (experience >= experienceToNextLevel) {
            levelUp();
        }
    }

    protected void levelUp() {
        level++;
        experience -= experienceToNextLevel;
        experienceToNextLevel = (int)(experienceToNextLevel * 1.3f);

        // Stat increases
        maxHealth *= 1.12f;
        health = maxHealth;
        attackDamage *= 1.12f;
        baseDamage *= 1.12f;
        speed *= 1.05f;
        baseSpeed *= 1.05f;
        armor += 1;
        baseArmor += 1;

        Gdx.app.log("Unit", name + " leveled up to " + level + "!");
    }

    public void addBuff(Buff buff) {
        activeBuffs.add(buff);
    }

    public void heal(float amount) {
        health = Math.min(health + amount, maxHealth);
    }

    protected void initiateRetreat() {
        isRetreating = true;
        // Move away from danger
        Vector2 retreatDir = new Vector2(position).sub(targetPosition).nor().scl(200);
        moveTo(position.x + retreatDir.x, position.y + retreatDir.y);
    }

    public void setStance(UnitStance newStance) {
        this.stance = newStance;
    }

    // Getters and Setters
    public int getId() { return id; }
    public Vector2 getPosition() { return position; }
    public float getHealth() { return health; }
    public float getMaxHealth() { return maxHealth; }
    public float getAttackDamage() { return attackDamage; }
    public float getBaseDamage() { return baseDamage; }
    public float getAttackRange() { return attackRange; }
    public boolean isAlive() { return isAlive; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { this.isSelected = selected; }
    public float getSize() { return size; }
    public String getName() { return name; }
    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = new Color(color); }
    public int getLevel() { return level; }
    public int getExperience() { return experience; }
    public int getExperienceToNextLevel() { return experienceToNextLevel; }
    public Unit getCurrentTarget() { return currentTarget; }
    public void setCurrentTarget(Unit target) { this.currentTarget = target; }
    public boolean isMoving() { return isMoving; }
    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }
    public UnitType getType() { return type; }
    public void setType(UnitType type) { this.type = type; }
    public UnitStance getStance() { return stance; }
    public int getKillCount() { return killCount; }
    public float getArmor() { return armor; }
    public void setSprite(String key) { this.spriteKey = key; }
    public ArrayList<Buff> getActiveBuffs() { return activeBuffs; }

    // ADDED MISSING GETTER HERE
    public float getMoveSpeed() { return speed; }
    public float getSpeed() { return speed; }
    public boolean isAttacking() { return isAttacking; }
    public boolean isGathering() { return isGathering; }

    // Added Setters for external modification
    public void setMaxHealth(float maxHealth) { this.maxHealth = maxHealth; }
    public void setHealth(float health) { this.health = health; }
    public void setAttackDamage(float damage) { this.attackDamage = damage; }
    public void setBaseDamage(float damage) { this.baseDamage = damage; }
}
