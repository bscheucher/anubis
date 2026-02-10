package com.ibosng.personalverwaltung.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "lhr_outbox")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LhrOutboxEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Operation operation;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> data;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "synced_at")
    private LocalDateTime syncedAt;

    @Column(name = "error_message")
    private String errorMessage;


    public enum Operation {
        CREATE_ABWESENHEIT_REQUEST
    }

    public enum Status {
        NEW,
        DONE,
        ERROR
    }

    public static LhrOutboxEntry forCreateAbwesenheitRequest(@NotNull Integer abwesenheitId) {
        return new LhrOutboxEntry(null, Operation.CREATE_ABWESENHEIT_REQUEST, Status.NEW, buildDataFieldFrom(String.valueOf(abwesenheitId)), null, null, null);
    }

    private static Map<String, Object> buildDataFieldFrom(@NotNull String entityId) {
        return Map.of("entityId", entityId);
    }
}
