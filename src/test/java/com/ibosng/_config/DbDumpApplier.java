package com.ibosng._config;

import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.StringUtils;
import org.testcontainers.containers.Container;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;

@Slf4j
public class DbDumpApplier {

    public static void apply(Container<?> container, String dbDumpPath, String bashCommand) {

        if (StringUtils.isBlank(dbDumpPath)) throw new RuntimeException("Path to db dump not supplied!");

        log.info("Applying DB dump: " + dbDumpPath);

        try {
            var startTime = System.currentTimeMillis();

            container.copyFileToContainer(MountableFile.forHostPath(dbDumpPath), "/tmp/dump.sql.gz");

            var result = container.execInContainer("bash", "-c", bashCommand);
            if (result.getExitCode() != 0) {
                throw new IllegalStateException("""
                        Failed to restore DB dump (exit code %d).
                        StdOut: %s
                        StdErr: %s
                        """.formatted(
                        result.getExitCode(),
                        result.getStdout(),
                        result.getStderr()
                ));
            }

           log.info("DB dump applied in " + (System.currentTimeMillis() - startTime) + " ms");

        } catch (IOException e) {
            throw new RuntimeException("I/O error while copying or executing DB dump", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while applying DB dump", e);
        }
    }
}
