package hexlet.code.app.repository;

import hexlet.code.app.model.TaskStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    TaskStatus findByName(String name);
}
