package com.vandan.codereview.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Service
public class GitHubService {

    private final WebClient webClient;
    private final RestTemplate restTemplate;

    public GitHubService() {
        this.webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(5 * 1024 * 1024))
                .build();
        this.restTemplate = new RestTemplate();
    }

    public String fetchDiff(String diffUrl, String githubToken) {
        String apiResult = tryGitHubApi(diffUrl, githubToken);
        if (apiResult != null && !apiResult.trim().isEmpty() && !apiResult.startsWith("ERROR")) {
            return apiResult;
        }

        String restResult = tryRestTemplate(diffUrl);
        if (restResult != null && !restResult.trim().isEmpty()) {
            return restResult;
        }

        return tryWebClientWithBrowserHeaders(diffUrl);
    }

    private String tryGitHubApi(String diffUrl, String githubToken) {
        try {
            String[] parts = diffUrl.replace("https://github.com/", "").split("/");
            if (parts.length < 4) return "ERROR: Invalid URL format";

            String owner = parts[0];
            String repo = parts[1];
            String prNumber = parts[3].replace(".diff", "");
            String apiUrl = String.format("https://api.github.com/repos/%s/%s/pulls/%s", owner, repo, prNumber);

            return webClient.get()
                    .uri(apiUrl)
                    .headers(headers -> {
                        if (githubToken != null && !githubToken.trim().isEmpty()) {
                            headers.setBearerAuth(githubToken);
                        }
                        headers.set("Accept", "application/vnd.github.v3.diff");
                        headers.set("User-Agent", "CodeReview-Bot/1.0");
                    })
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> Mono.error(new RuntimeException("API Error: " + response.statusCode())))
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(30))
                    .onErrorReturn("ERROR: API request failed")
                    .block();

        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    private String tryRestTemplate(String diffUrl) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            headers.set("Accept", "text/plain,text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(diffUrl, HttpMethod.GET, entity, String.class);
            return response.getBody();

        } catch (Exception e) {
            return null;
        }
    }

    private String tryWebClientWithBrowserHeaders(String diffUrl) {
        try {
            return webClient.get()
                    .uri(diffUrl)
                    .headers(headers -> {
                        headers.set("User-Agent", "Mozilla/5.0");
                        headers.set("Accept", "*/*");
                    })
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(30))
                    .onErrorReturn(null)
                    .block();

        } catch (Exception e) {
            return null;
        }
    }

    public void postCommentToPullRequest(String owner, String repo, int prNumber, String comment, String githubToken) {
        try {
            String url = String.format("https://api.github.com/repos/%s/%s/issues/%d/comments", owner, repo, prNumber);

            WebClient client = WebClient.builder().build();

            Map<String, String> body = Map.of("body", comment);

            client.post()
                    .uri(url)
                    .header("Authorization", "Bearer " + githubToken)
                    .header("Accept", "application/vnd.github.v3+json")
                    .bodyValue(body)
                    .retrieve()
                    .toBodilessEntity()
                    .doOnSuccess(response -> System.out.println("Comment posted to PR successfully!"))
                    .doOnError(error -> System.err.println("Failed to post comment: " + error.getMessage()))
                    .block();

        } catch (Exception e) {
            System.err.println("Exception while posting comment: " + e.getMessage());
        }
    }
}
