package com.ibosng.dbservice.entities.ai;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "openai_assistant_anweisung")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenaiAssistantAnweisung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "openai_assistant_id")
    private OpenaiAssistant openaiAssistantId;

    @Column(name = "anweisung")
    private String anweisung;

    @Column(name = "version")
    private Integer version;

    @JsonIgnore
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();

    @JsonIgnore
    @Column(name = "created_by")
    private String createdBy;

    @JsonIgnore
    @Column(name = "changed_by")
    private String changedBy;

    @JsonIgnore
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "changed_on")
    private LocalDateTime changedOn = LocalDateTime.now();
}
