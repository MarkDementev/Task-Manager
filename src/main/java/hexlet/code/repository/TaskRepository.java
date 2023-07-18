package hexlet.code.repository;

import hexlet.code.model.QTask;
import hexlet.code.model.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository
        extends JpaRepository<Task, Long>,
        QuerydslPredicateExecutor<Task>,
        QuerydslBinderCustomizer<QTask> {
    @Override
    default void customize(QuerydslBindings bindings, QTask root) {

    }

    Task findByName(String name);
}
