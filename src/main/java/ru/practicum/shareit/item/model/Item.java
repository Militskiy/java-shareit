package ru.practicum.shareit.item.model;

import com.google.common.base.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "items")
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    @Column(name = "item_name")
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @NotNull
    private User owner;
    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return getId() == item.getId() && Objects.equal(getOwner(), item.getOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getOwner());
    }
}
