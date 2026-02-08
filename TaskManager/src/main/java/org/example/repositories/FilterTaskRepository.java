package org.example.repositories;

import lombok.RequiredArgsConstructor;
import org.example.dtos.filter.TaskFilter;
import org.example.dtos.response.CommentResponseDto;
import org.example.dtos.response.TaskResponseDto;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.example.generated.jooq.Tables.COMMENT;
import static org.example.generated.jooq.Tables.TASK;

@Repository
@RequiredArgsConstructor
public class FilterTaskRepository {
    private final DSLContext dslContext;

    public List<TaskResponseDto> findTaskByFilter(TaskFilter filter, int page, int size) {
        int offset = page * size;
        Condition condition = buildTaskCondition(filter);

        return dslContext.select(
                        TASK.asterisk(),
                        DSL.multiset(
                                dslContext.selectFrom(COMMENT)
                                        .where(COMMENT.TASK_ID.eq(TASK.ID))
                        ).convertFrom(r -> r.into(CommentResponseDto.class)).as("comments")
                )
                .from(TASK)
                .where(condition)
                .orderBy(TASK.ID)
                .limit(size)
                .offset(offset)
                .fetchInto(TaskResponseDto.class);
    }

    private Condition buildTaskCondition(TaskFilter filter) {
        Condition condition = DSL.noCondition();

        if (filter.getAuthor() != null) {
            condition = condition.and(TASK.AUTHOR.eq(filter.getAuthor()));
        }

        if (filter.getExecutor() != null) {
            condition = condition.and(TASK.EXECUTOR.eq(filter.getExecutor()));
        }

        return condition;
    }
}
