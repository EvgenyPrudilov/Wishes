package ru.cohenrol.configservice;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@EnableScheduling
public class SimpleConfigWatcher {

    @Value("${config.watcher.refresh-url-template}")
    private String refreshUrlTemplate;
    @Value("${config.watcher.repo-path}")
    private String configuredRepoPath;

    private final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(3))
        .build();

//    private final Path repoPath = Paths.get(configuredRepoPath);
    private Path repoPath;
    private final ConcurrentHashMap<Path, Long> timestamps = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        this.repoPath = Paths.get(configuredRepoPath);
    }

    @Scheduled(fixedDelay = 2000)
    public void watchFiles() {
        if (!Files.exists(repoPath)) return;

        Set<Path> currentFiles = new HashSet<>();
        Set<String> servicesToRefresh = new HashSet<>();

        try (var stream = Files.walk(repoPath)) {
            var filesList = stream.filter(Files::isRegularFile).toList();

            for (Path path : filesList) {
                currentFiles.add(path);
                if (checkModified(path)) {
                    // Extract the service name directly from the changed file's path
                    String serviceName = extractServiceName(path);
                    if (serviceName != null) {
                        servicesToRefresh.add(serviceName);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[Watcher] Error walking file tree: " + e.getMessage());
            return;
        }

        // Clean up deleted files from memory
        timestamps.keySet().retainAll(currentFiles);

        // Trigger pinpoint updates for affected services only
        for (String serviceName : servicesToRefresh) {
            triggerServiceRefresh(serviceName);
        }
    }

    private boolean checkModified(Path path) {
        try {
            long currentMod = Files.getLastModifiedTime(path).toMillis();
            Long prevMod = timestamps.put(path, currentMod);
            return prevMod != null && currentMod > prevMod;
        } catch (IOException e) {
            return false;
        }
    }

    private String extractServiceName(Path path) {
        // Relativize path to get the structure inside /var/config-repo
        Path relativePath = repoPath.relativize(path);

        // Scenario 1: File is inside a subfolder (e.g., auth-service/application.yml)
        if (relativePath.getNameCount() > 1) {
            return relativePath.getName(0).toString();
        }

        // Scenario 2: File is in the root directory (e.g., logging-service.properties)
        String fileName = relativePath.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            String cleanName = fileName.substring(0, dotIndex);
            // Ignore global shared configs like 'application.properties' or 'shared'
            if (!"application".equals(cleanName) && !"shared".equals(cleanName)) {
                return cleanName;
            }
        }
        return null;
    }

    private void triggerServiceRefresh(String serviceName) {
        String targetUrl = String.format(refreshUrlTemplate, serviceName);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(targetUrl))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("[Watcher] Configuration updated successfully for: " + serviceName);
            } else {
                System.err.println("[Watcher] Service '" + serviceName + "' returned status code: " + response.statusCode());
            }
        } catch (Exception ex) {
            System.err.println("[Watcher] Network error reaching service '" + serviceName + "': " + ex.getMessage());
        }
    }
}
