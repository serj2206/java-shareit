package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "items")
@Data
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "name")
    @NotNull(message = "Название не может быть пустым")
    @NotBlank(message = "Название не может быть пустым")
    String name;

    @Column(name = "description")
    @NotNull(message = "Описание не может быть пустым")
    @NotBlank(message = "Описание не может быть пустым")
    String description;

    @Column(name = "available")
    @AssertTrue
    Boolean available;

    //@ElementCollection
    //@CollectionTable(name = "users", joinColumns = @JoinColumn(name = "id"))
    //@Column(name = "id") */
    @OneToOne
    User owner;

    @OneToOne
    ItemRequest request;


    public Item() {

    }
}

