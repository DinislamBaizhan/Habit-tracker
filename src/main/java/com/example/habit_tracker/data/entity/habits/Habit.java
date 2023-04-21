package com.example.habit_tracker.data.entity.habits;

import com.example.habit_tracker.data.enums.RepeatType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@MappedSuperclass
public abstract class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer habitId;
    protected final Integer userId;
    protected String name;
    protected String description;
    protected String category;
    protected RepeatType repeatType;
    protected int repeatTarget;
    protected int repeatActual;
    protected LocalDate startDate;
    protected LocalDate endDate;
    protected int habitDaysCount;
    protected boolean isHabitTotallyCompleted;

    @JsonCreator
    public Habit(Integer userId, @JsonProperty("name") String name, @JsonProperty("description") String description,
                 @JsonProperty("category") String category, @JsonProperty("repeatType") RepeatType repeatType,
                 @JsonProperty("repeatTarget") int repeatTarget, @JsonProperty("endDate") LocalDate endDate) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.repeatType = repeatType;
        this.repeatTarget = repeatTarget;
        this.startDate = LocalDate.now();
        this.endDate = endDate;
        this.habitDaysCount = (int) ChronoUnit.DAYS.between(LocalDate.now(), endDate);
        this.isHabitTotallyCompleted = false;
    }

    public boolean checkIfHabitIsLocallyCompleted() {
        switch (repeatType) {
            case DAY -> {
                //TODO смотрим запись в habit_completion по habit_id с сегодняшней датой
                // если completedToday = true, возвращаем true
            }
            case WEEK -> {
                //TODO берем записи из habit_completion по habit_id за текущую неделю
                // смотрим, есть ли там completedWeek = true, если да, возвращаем true
                // иначе возвращаем false
            }
            case MONTH -> {
                //TODO берем записи из habit_completion по habit_id за текущий месяц
                // смотрим, есть ли там completedMonth = true, если да, возвращаем true
                // иначе возвращаем false
            }
        }

        return false;
    }

    // метод, чтобы сосчитать количество закрытых дней по привычке
    public int countClosedDays() {
        //TODO делаем запрос в habit_completion по habit_id за период от startDate до endDate
        // и статусом completedToday = true, считаем количество строк и возвращаем
        return 0;
    }

    // метод, чтобы проверить, выполнены ли условия привычки за весь срок успешно и записать результат
    public boolean checkIfHabitIsTotallyCompleted() {
        if (!isHabitTotallyCompleted) {
            boolean firstCondition = LocalDate.now().isAfter(endDate);
            boolean secondCondition = false;

            switch (repeatType) {
                case DAY -> {
                    //TODO берем записи из habit_completion по habit_id за период от startDate до endDate
                    // пробегаем и ищем, если есть хоть одна запись с completedToday = false,
                    // то устанавливаем secondCondition = false
                    // если все были completedToday = true, то устанавливаем secondCondition = true;
                }
                case WEEK -> {
                    //TODO берем записи из habit_completion по habit_id за период от startDate до endDate
                    // пробегаем *понедельно* и смотрим: если в каждой неделе была completedWeek = true,
                    // то устанавливаем secondCondition = true;
                    // иначе устанавливаем secondCondition = false
                }
                case MONTH -> {
                    //TODO берем записи из habit_completion по habit_id за период от startDate до endDate
                    // пробегаем *помесячно* и смотрим: если в каждом месяце был completedMonth = true,
                    // то устанавливаем secondCondition = true;
                    // иначе устанавливаем secondCondition = false
                }
            }

            if (firstCondition && secondCondition) {
                isHabitTotallyCompleted = true;
            }
        }
        return isHabitTotallyCompleted;
    }
}
