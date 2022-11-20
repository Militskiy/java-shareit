package ru.practicum.shareit.request.model;

import com.google.common.base.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "item_requests")
public class ItemRequest {
    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "request_description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequest that = (ItemRequest) o;
        return getId() == that.getId() && Objects.equal(getRequester(), that.getRequester());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getRequester());
    }
}
