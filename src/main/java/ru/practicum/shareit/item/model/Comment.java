package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@Builder
public class Comment {
    public Comment() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text")
    private String text;

    @CollectionTable(name = "items", joinColumns = @JoinColumn(name = "id"))
    @ManyToOne
    private Item item;

    @CollectionTable(name = "users", joinColumns = @JoinColumn(name = "id"))
    @ManyToOne
    private User author;

    @Column(name = "created")
    private LocalDateTime created;
}
