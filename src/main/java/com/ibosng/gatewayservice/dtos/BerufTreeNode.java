package com.ibosng.gatewayservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BerufTreeNode {
    private String id;             // noteid des Elements
    private String title;          // bezeichnung des Elements
    private String type;           // "Berufsbereich/Stammdatenkategorie", "Berufsfeld", "Beruf", "Spezialisierung"

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<BerufTreeNode> children; // Unterelemente, zb. Spezialisierungen eines Berufs

    public void addChild(BerufTreeNode child) {
        children.add(child);
    }
}