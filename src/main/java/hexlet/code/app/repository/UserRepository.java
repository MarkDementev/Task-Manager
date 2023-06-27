package hexlet.code.app.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import hexlet.code.app.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
