package org.core.implementation.folia.scheduler;

import org.bukkit.Bukkit;
import org.core.TranslateCore;
import org.core.implementation.folia.scheduler.type.ScheduleType;
import org.core.platform.plugin.Plugin;
import org.core.schedule.Scheduler;
import org.core.schedule.SchedulerBuilder;
import org.core.schedule.unit.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalTime;
import java.util.Optional;
import java.util.function.Consumer;

public class BNativeScheduler implements Scheduler.Native {

    private class RunAfterScheduler implements Runnable {

        @Override
        public void run() {
            BNativeScheduler.this.startRunner = LocalTime.now();
            try {
                BNativeScheduler.this.taskToRun.accept(BNativeScheduler.this);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            ((BScheduleManager) TranslateCore.getScheduleManager()).unregister(BNativeScheduler.this);
            Scheduler scheduler = BNativeScheduler.this.runAfter;
            if (scheduler != null) {
                if (scheduler instanceof BNativeScheduler) {
                    ((BNativeScheduler) scheduler).parent = BNativeScheduler.this.parent;
                }
                scheduler.run();
            }
            Bukkit.getScheduler().cancelTask(BNativeScheduler.this.task);
            BNativeScheduler.this.endTime = LocalTime.now();
        }

        @Override
        public String toString() {
            String str =
                    BNativeScheduler.this.displayName + ": Delay(Time: " + BNativeScheduler.this.delayCount + " Unit: "
                            + BNativeScheduler.this.delayTimeUnit + ") Iteration: (Time: "
                            + BNativeScheduler.this.iteration + " Unit: " + BNativeScheduler.this.iterationTimeUnit
                            + ") Plugin: " + BNativeScheduler.this.plugin.getPluginId() + " ID:"
                            + BNativeScheduler.this.task;
            if (BNativeScheduler.this.runAfter == null) {
                return str + " ToRunAfter: None";
            } else if (BNativeScheduler.this.runAfter instanceof BNativeScheduler) {
                return str + " ToRunAfter: " + ((BNativeScheduler) BNativeScheduler.this.runAfter).task;
            }
            return str + " ToRunAfter: Unknown";

        }

    }

    protected final @NotNull Consumer<Scheduler> taskToRun;
    protected final int delayCount;
    protected final @NotNull TimeUnit delayTimeUnit;
    protected final @Nullable Integer iteration;
    protected final @Nullable TimeUnit iterationTimeUnit;
    protected final @NotNull String displayName;
    protected final boolean async;
    protected final @NotNull Plugin plugin;
    private final ScheduleType scheduleType;
    protected @Nullable Scheduler runAfter;
    protected int task;
    private @Nullable String parent;
    private LocalTime endTime;
    private LocalTime startSchedule;
    private LocalTime startRunner;

    public BNativeScheduler(@NotNull SchedulerBuilder builder,
                            @NotNull Plugin plugin,
                            @NotNull ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
        this.taskToRun = builder.getRunner();
        this.iteration = builder.getIteration().orElse(null);
        this.iterationTimeUnit = builder.getIterationUnit().orElse(TimeUnit.MINECRAFT_TICKS);
        this.delayCount = builder.getDelay().orElse(0);
        this.delayTimeUnit = builder.getDelayUnit().orElse(TimeUnit.MINECRAFT_TICKS);
        this.plugin = plugin;
        this.displayName = builder.getDisplayName().orElseThrow(() -> new RuntimeException("No Displayname"));
        this.async = builder.isAsync();
        builder.getToRunAfter().ifPresent(s -> this.runAfter = s);
    }

    @Override
    public Optional<LocalTime> getStartScheduleTime() {
        return Optional.ofNullable(this.startSchedule);
    }

    @Override
    public Optional<LocalTime> getStartRunnerTime() {
        return Optional.ofNullable(this.startRunner);
    }

    @Override
    public Optional<LocalTime> getEndTime() {
        return Optional.ofNullable(this.endTime);
    }

    @Override
    public boolean isAsync() {
        return this.async;
    }

    @Override
    public @NotNull String getDisplayName() {
        return this.displayName;
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public void run() {
        this.startSchedule = LocalTime.now();
        long ticks = this.delayTimeUnit.toTicks(this.delayCount);
        Integer iter = null;
        if (this.iteration != null) {
            if (this.iterationTimeUnit == null) {
                throw new RuntimeException("Iteration time was set however the timeunit was not");
            }
            iter = this.iterationTimeUnit.toTicks(this.iteration);
        }
        Runnable runAfterScheduler = new RunAfterScheduler();
        this.task = this.scheduleType.start((org.bukkit.plugin.Plugin) this.plugin.getPlatformLauncher(),
                                            runAfterScheduler, ticks, iter);
    }

    @Override
    public Consumer<Scheduler> getRunner() {
        return this.taskToRun;
    }

    @Override
    public void cancel() {
        Bukkit.getScheduler().cancelTask(this.task);
    }

    @Override
    public String toString() {
        String str = this.displayName + ": Delay(Time: " + this.delayCount + " Unit: " + this.delayTimeUnit
                + ") Iteration: (Time: " + this.iteration + " Unit: " + this.iterationTimeUnit + ") Plugin: "
                + this.plugin.getPluginId() + " ID:" + this.task;
        if (this.runAfter == null) {
            return str + " ToRunAfter: None";
        } else if (this.runAfter instanceof BNativeScheduler) {
            return str + " ToRunAfter: " + ((BNativeScheduler) this.runAfter).task;
        }
        return str + " ToRunAfter: Unknown";

    }
}
