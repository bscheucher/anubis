package com.ibosng.dms;

import com.networknt.schema.Error;
import com.networknt.schema.Schema;
import com.networknt.schema.SchemaLocation;
import com.networknt.schema.SchemaRegistry;
import com.networknt.schema.dialect.Dialects;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.dataformat.yaml.YAMLFactory;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DmsFileStructureSchemaTest {

    private static final String SCHEMA_RESOURCE = "dms/file-structure-schema.json";
    private static final String SCHEMA_ID = "https://example.invalid/schemas/tn-file-structure.schema.json";
    private static final String SCHEMA_ERROR_PREFIX = "Schema validation failed:";

    @Test
    void tnFileStructureYaml_matches_schema() throws Exception {
        assertYamlMatchesSchema("dms/TN-file-structure.yaml");
    }

    @Test
    void maFileStructureYaml_matches_schema() throws Exception {
        assertYamlMatchesSchema("dms/MA-file-structure.yaml");
    }

    private void assertYamlMatchesSchema(String yamlResource) throws Exception {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

        try (InputStream yaml = getRequiredResource(yamlResource);
             InputStream schemaStream = getRequiredResource(SCHEMA_RESOURCE)) {

            JsonNode yamlAsJson = yamlMapper.readTree(yaml);
            Schema schema = loadSchema(schemaStream);
            List<Error> errors = schema.validate(yamlAsJson);

            assertTrue(errors.isEmpty(), () ->
                    String.format("%s\n%s", schemaErrorMessage(yamlResource), joinErrorMessages(errors)));
        }
    }

    private InputStream getRequiredResource(String resource) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(resource);
        assertNotNull(stream, "Missing resource: " + resource);
        return stream;
    }

    private Schema loadSchema(InputStream schemaStream) throws Exception {
        String schemaJson = new String(schemaStream.readAllBytes(), StandardCharsets.UTF_8);
        SchemaRegistry schemaRegistry = SchemaRegistry.withDialect(
                Dialects.getDraft202012(),
                builder -> builder.schemas(Map.of(SCHEMA_ID, schemaJson))
        );
        return schemaRegistry.getSchema(SchemaLocation.of(SCHEMA_ID));
    }

    private String schemaErrorMessage(String yamlResource) {
        if ("dms/TN-file-structure.yaml".equals(yamlResource)) {
            return SCHEMA_ERROR_PREFIX;
        }
        return SCHEMA_ERROR_PREFIX + " for " + yamlResource + ":";
    }

    private String joinErrorMessages(List<Error> errors) {
        return String.join("\n", errors.stream().map(Error::getMessage).toList());
    }
}