package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@AllArgsConstructor
@Builder
public class Booking {
    public Booking() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    private LocalDateTime start;

    @Column(name = "end_date")
    private LocalDateTime end;

    @ManyToOne
    @CollectionTable(name = "items", joinColumns = @JoinColumn(name = "id"))
    //@Column(name = "item_id")
    private Item item;

    @OneToOne
    @CollectionTable(name = "users", joinColumns = @JoinColumn(name = "id"))
    //@Column(name = "booker_id")
    private User booker;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
