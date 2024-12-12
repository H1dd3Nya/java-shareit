package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.shareit.user.model.User;

@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;


    @Size(max = 60)
    @Column(name = "name")
    private String name;


    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Column(name = "is_available")
    private Boolean isAvailable;
}
