package com.ibosng.dbservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "file_import_headers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class FileImportHeaders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "file_type", unique = true)
    private FileType fileType;

    @Column(name = "active_headers")
    private String activeHeaders;

    @Column(name = "inactive_headers")
    private String inactiveHeaders;

    @Column(name = "version")
    private String version;

    @Column(name = "created_on")
    @Builder.Default
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_on")
    private LocalDateTime changedOn;

    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileImportHeaders fileImportHeaders = (FileImportHeaders) o;
        return Objects.equals(id, fileImportHeaders.id) && fileType == fileImportHeaders.fileType && Objects.equals(activeHeaders, fileImportHeaders.activeHeaders) && Objects.equals(version, fileImportHeaders.version) && Objects.equals(inactiveHeaders, fileImportHeaders.inactiveHeaders) && Objects.equals(createdOn, fileImportHeaders.createdOn) && Objects.equals(createdBy, fileImportHeaders.createdBy) && Objects.equals(changedOn, fileImportHeaders.changedOn) && Objects.equals(changedBy, fileImportHeaders.changedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileType, activeHeaders, version, inactiveHeaders, createdOn, createdBy, changedOn, changedBy);
    }
}
