package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    private LocalDateTime start;

    @Column(name = "end_date")
    private LocalDateTime end;

    @ManyToOne
    @CollectionTable(name = "items", joinColumns = @JoinColumn(name = "id"))
    private Item item;

    @OneToOne
    @CollectionTable(name = "users", joinColumns = @JoinColumn(name = "id"))
    private User booker;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
