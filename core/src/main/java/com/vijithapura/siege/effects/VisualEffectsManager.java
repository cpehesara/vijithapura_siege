package com.vijithapura.siege.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.vijithapura.siege.entities.Unit;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Enhanced visual effects system for better gameplay feedback
 */
public class VisualEffectsManager {

    private ArrayList<Effect> effects;
    private ArrayList<FloatingText> floatingTexts;
    private ArrayList<SelectionRing> selectionRings;
    private BitmapFont font; // Added font for rendering text

    public VisualEffectsManager() {
        effects = new ArrayList<>();
        floatingTexts = new ArrayList<>();
        selectionRings = new ArrayList<>();
        // Load default font. In a real game, you might want to pass the game's font via setter
        font = new BitmapFont();
    }

    // Allow injecting the game's main font if desired
    public void setFont(BitmapFont font) {
        this.font = font;
    }

    // ==========================================
    // MISSING METHODS ADDED HERE
    // ==========================================

    /**
     * Returns the total number of active particle effects.
     * Used for performance monitoring.
     */
    public int getEffectCount() {
        return effects.size();
    }

    /**
     * Removes the oldest particles if the count exceeds the limit.
     * Prevents performance drops when too many particles are on screen.
     */
    public void clearOldest(int count) {
        int toRemove = Math.min(count, effects.size());
        if (toRemove > 0) {
            // Efficiently remove from the start of the list
            effects.subList(0, toRemove).clear();
        }
    }

    // ==========================================

    public void update(float delta) {
        // Update all effects
        Iterator<Effect> effectIter = effects.iterator();
        while (effectIter.hasNext()) {
            Effect e = effectIter.next();
            e.update(delta);
            if (e.isFinished()) {
                effectIter.remove();
            }
        }

        Iterator<FloatingText> textIter = floatingTexts.iterator();
        while (textIter.hasNext()) {
            FloatingText ft = textIter.next();
            ft.update(delta);
            if (ft.isFinished()) {
                textIter.remove();
            }
        }

        Iterator<SelectionRing> ringIter = selectionRings.iterator();
        while (ringIter.hasNext()) {
            SelectionRing ring = ringIter.next();
            ring.update(delta);
            if (ring.isFinished()) {
                ringIter.remove();
            }
        }
    }

    public void render(ShapeRenderer sr, SpriteBatch batch) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Render shape-based effects
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (Effect e : effects) {
            e.render(sr);
        }
        for (SelectionRing ring : selectionRings) {
            ring.render(sr);
        }
        sr.end();

        // Render text effects
        if (!floatingTexts.isEmpty()) {
            batch.begin();
            for (FloatingText ft : floatingTexts) {
                ft.render(batch, font);
            }
            batch.end();
        }
    }

    // ===== Effect Creation Methods =====

    public void createBloodSplatter(Vector2 position, int count) {
        for (int i = 0; i < count; i++) {
            float angle = (float)(Math.random() * Math.PI * 2);
            float speed = 50 + (float)(Math.random() * 150);
            Vector2 velocity = new Vector2(
                (float)Math.cos(angle) * speed,
                (float)Math.sin(angle) * speed
            );

            Color color = new Color(
                0.7f + (float)Math.random() * 0.2f,
                0,
                0,
                1f
            );

            effects.add(new ParticleEffect(
                new Vector2(position),
                velocity,
                color,
                0.5f + (float)Math.random() * 0.5f,
                2 + (float)Math.random() * 3
            ));
        }
    }

    public void createDustCloud(Vector2 position, int count) {
        for (int i = 0; i < count; i++) {
            float angle = (float)(Math.random() * Math.PI * 2);
            float speed = 20 + ((float)Math.random() * 60);
            Vector2 velocity = new Vector2(
                (float)Math.cos(angle) * speed,
                (float)Math.sin(angle) * speed
            );

            Color color = new Color(
                0.6f + (float)Math.random() * 0.2f,
                0.5f + (float)Math.random() * 0.2f,
                0.4f + (float)Math.random() * 0.1f,
                0.6f
            );

            effects.add(new ParticleEffect(
                new Vector2(position),
                velocity,
                color,
                0.3f + (float)Math.random() * 0.4f,
                3 + (float)Math.random() * 4
            ));
        }
    }

    public void createHitEffect(Vector2 position) {
        // Impact flash
        effects.add(new FlashEffect(position, Color.YELLOW, 0.2f, 20));

        // Sparkles
        for (int i = 0; i < 8; i++) {
            float angle = (float)(Math.PI * 2 * i / 8);
            Vector2 velocity = new Vector2(
                (float)Math.cos(angle) * 100,
                (float)Math.sin(angle) * 100
            );
            effects.add(new ParticleEffect(
                new Vector2(position),
                velocity,
                Color.YELLOW,
                0.3f,
                2
            ));
        }
    }

    public void createHealEffect(Vector2 position) {
        for (int i = 0; i < 12; i++) {
            float angle = (float)(Math.random() * Math.PI * 2);
            float speed = 30 + (float)(Math.random() * 50);
            Vector2 velocity = new Vector2(
                (float)Math.cos(angle) * speed,
                (float)Math.sin(angle) * speed + 60 // Float upward
            );

            effects.add(new ParticleEffect(
                new Vector2(position),
                velocity,
                Color.GREEN,
                0.8f,
                3
            ));
        }
    }

    public void createLevelUpEffect(Vector2 position) {
        // Circular burst
        for (int i = 0; i < 20; i++) {
            float angle = (float)(Math.PI * 2 * i / 20);
            Vector2 velocity = new Vector2(
                (float)Math.cos(angle) * 120,
                (float)Math.sin(angle) * 120
            );

            effects.add(new ParticleEffect(
                new Vector2(position),
                velocity,
                Color.GOLD,
                0.8f,
                4
            ));
        }

        // Rising particles
        for (int i = 0; i < 10; i++) {
            Vector2 velocity = new Vector2(
                ((float)Math.random() - 0.5f) * 40,
                80 + (float)Math.random() * 40
            );

            effects.add(new ParticleEffect(
                new Vector2(position),
                velocity,
                new Color(1f, 0.9f, 0.2f, 1f),
                1.2f,
                5
            ));
        }
    }

    public void createDeathExplosion(Vector2 position) {
        // Large burst
        for (int i = 0; i < 30; i++) {
            float angle = (float)(Math.random() * Math.PI * 2);
            float speed = 100 + (float)(Math.random() * 200);
            Vector2 velocity = new Vector2(
                (float)Math.cos(angle) * speed,
                (float)Math.sin(angle) * speed
            );

            Color color = new Color(
                0.8f + (float)Math.random() * 0.2f,
                (float)Math.random() * 0.2f,
                0,
                1f
            );

            effects.add(new ParticleEffect(
                new Vector2(position),
                velocity,
                color,
                0.6f + (float)Math.random() * 0.6f,
                3 + (float)Math.random() * 5
            ));
        }
    }

    public void createBuildingConstructionEffect(Vector2 position) {
        for (int i = 0; i < 15; i++) {
            Vector2 velocity = new Vector2(
                ((float)Math.random() - 0.5f) * 50,
                50 + (float)Math.random() * 100
            );

            effects.add(new ParticleEffect(
                new Vector2(position),
                velocity,
                new Color(0.7f, 0.7f, 0.4f, 1f),
                0.8f,
                4
            ));
        }
    }

    public void createArrowTrail(Vector2 start, Vector2 end) {
        effects.add(new TrailEffect(start, end, Color.WHITE, 0.3f));
    }

    public void createSelectionRing(Unit unit) {
        selectionRings.add(new SelectionRing(unit, Color.YELLOW));
    }

    public void createDamageNumber(Vector2 position, float damage, boolean isCritical) {
        Color color = isCritical ? Color.RED : Color.ORANGE;
        float scale = isCritical ? 1.5f : 1.0f;
        floatingTexts.add(new FloatingText(
            position,
            "-" + (int)damage,
            color,
            scale
        ));
    }

    public void createHealNumber(Vector2 position, float amount) {
        floatingTexts.add(new FloatingText(
            position,
            "+" + (int)amount,
            Color.GREEN,
            1.0f
        ));
    }

    public void createStatusText(Vector2 position, String text, Color color) {
        floatingTexts.add(new FloatingText(position, text, color, 1.2f));
    }

    public void clear() {
        effects.clear();
        floatingTexts.clear();
        selectionRings.clear();
    }

    // ===== Effect Classes =====

    private interface Effect {
        void update(float delta);
        void render(ShapeRenderer sr);
        boolean isFinished();
    }

    private static class ParticleEffect implements Effect {
        Vector2 position;
        Vector2 velocity;
        Color color;
        float lifetime;
        float maxLifetime;
        float size;
        float gravity;

        public ParticleEffect(Vector2 pos, Vector2 vel, Color col, float life, float sz) {
            position = new Vector2(pos);
            velocity = new Vector2(vel);
            color = new Color(col);
            lifetime = life;
            maxLifetime = life;
            size = sz;
            gravity = -200f; // Particles fall
        }

        @Override
        public void update(float delta) {
            position.add(velocity.x * delta, velocity.y * delta);
            velocity.scl(0.95f); // Friction
            velocity.y += gravity * delta;
            lifetime -= delta;
        }

        @Override
        public void render(ShapeRenderer sr) {
            float alpha = lifetime / maxLifetime;
            sr.setColor(color.r, color.g, color.b, alpha * color.a);
            sr.circle(position.x, position.y, size * alpha);
        }

        @Override
        public boolean isFinished() {
            return lifetime <= 0;
        }
    }

    private static class FlashEffect implements Effect {
        Vector2 position;
        Color color;
        float lifetime;
        float maxLifetime;
        float maxSize;

        public FlashEffect(Vector2 pos, Color col, float life, float size) {
            position = new Vector2(pos);
            color = new Color(col);
            lifetime = life;
            maxLifetime = life;
            maxSize = size;
        }

        @Override
        public void update(float delta) {
            lifetime -= delta;
        }

        @Override
        public void render(ShapeRenderer sr) {
            float alpha = lifetime / maxLifetime;
            float size = maxSize * (1.0f - alpha); // Expand
            sr.setColor(color.r, color.g, color.b, alpha * 0.7f);
            sr.circle(position.x, position.y, size);
        }

        @Override
        public boolean isFinished() {
            return lifetime <= 0;
        }
    }

    private static class TrailEffect implements Effect {
        Vector2 start;
        Vector2 end;
        Color color;
        float lifetime;
        float maxLifetime;

        public TrailEffect(Vector2 s, Vector2 e, Color col, float life) {
            start = new Vector2(s);
            end = new Vector2(e);
            color = new Color(col);
            lifetime = life;
            maxLifetime = life;
        }

        @Override
        public void update(float delta) {
            lifetime -= delta;
        }

        @Override
        public void render(ShapeRenderer sr) {
            float alpha = lifetime / maxLifetime;
            sr.setColor(color.r, color.g, color.b, alpha * 0.8f);
            sr.rectLine(start, end, 2 * alpha);
        }

        @Override
        public boolean isFinished() {
            return lifetime <= 0;
        }
    }

    private static class SelectionRing {
        Unit unit;
        Color color;
        float pulseTimer;
        float lifetime;

        public SelectionRing(Unit u, Color col) {
            unit = u;
            color = new Color(col);
            pulseTimer = 0;
            lifetime = 0.5f; // Fade in
        }

        public void update(float delta) {
            pulseTimer += delta;
            if (lifetime < 1.0f) {
                lifetime += delta * 2;
            }
        }

        public void render(ShapeRenderer sr) {
            if (unit == null || !unit.isAlive() || !unit.isSelected()) return;

            float pulse = (float)Math.sin(pulseTimer * 3) * 0.2f + 0.8f;
            float alpha = Math.min(1.0f, lifetime);

            Vector2 pos = unit.getPosition();
            float size = unit.getSize() + 8;

            // Outer glow
            sr.setColor(color.r, color.g, color.b, alpha * 0.3f * pulse);
            sr.circle(pos.x, pos.y, size + 4);

            // Inner ring
            sr.setColor(color.r, color.g, color.b, alpha * pulse);
            sr.circle(pos.x, pos.y, size);

            // Core
            sr.setColor(color.r, color.g, color.b, alpha * 0.5f);
            sr.circle(pos.x, pos.y, size - 2);
        }

        public boolean isFinished() {
            return unit == null || !unit.isAlive() || !unit.isSelected();
        }
    }

    private static class FloatingText {
        Vector2 position;
        Vector2 velocity;
        String text;
        Color color;
        float lifetime;
        float scale;

        public FloatingText(Vector2 pos, String txt, Color col, float scl) {
            position = new Vector2(pos);
            velocity = new Vector2(0, 80); // Float upward
            text = txt;
            color = new Color(col);
            lifetime = 1.5f;
            scale = scl;
        }

        public void update(float delta) {
            position.add(velocity.x * delta, velocity.y * delta);
            velocity.scl(0.95f);
            lifetime -= delta;
        }

        public void render(SpriteBatch batch, BitmapFont font) {
            font.setColor(color.r, color.g, color.b, Math.min(1.0f, lifetime));
            font.getData().setScale(scale);
            font.draw(batch, text, position.x, position.y);
            font.getData().setScale(1.0f); // Reset scale
        }

        public boolean isFinished() {
            return lifetime <= 0;
        }
    }
}
