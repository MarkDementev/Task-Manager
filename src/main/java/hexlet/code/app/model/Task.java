package hexlet.code.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Temporal;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToOne;
//import jakarta.persistence.JoinColumn;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @ManyToOne
    private User author;

    @ManyToOne
    private User executor;

    @NotNull
    @OneToOne
    private TaskStatus taskStatus;

//    @NotNull
//    @ManyToOne
//    @JoinColumn(name = "author_id")
//    private User author;
//
//    @ManyToOne
//    @JoinColumn(name = "executor_id")
//    private User executor;

//    @NotNull
//    @OneToOne
//    @JoinColumn(name = "taskStatus_id")
//    private TaskStatus taskStatus;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;

    public Task(final Long id) {
        this.id = id;
    }
}
