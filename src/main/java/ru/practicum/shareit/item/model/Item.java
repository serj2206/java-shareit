package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Entity
@Table(name = "items")
@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "available")
    Boolean available;


    @CollectionTable(name = "users", joinColumns = @JoinColumn(name = "id"))
    @ManyToOne
    User owner;

    @ManyToOne
    ItemRequest request;
}

