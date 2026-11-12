package be.atheneumboom.bibliotheek.service;

import be.atheneumboom.bibliotheek.model.DTO.BookMetadataDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookMetadataService {
    private static final String OPEN_LIBRARY_SEARCH_URL = "https://openlibrary.org/search.json";

    private final RestClient restClient;

    public BookMetadataService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(OPEN_LIBRARY_SEARCH_URL).build();
    }

    public Optional<BookMetadataDTO> findByBarcode(String barcode) {
        String normalizedBarcode = normalizeBarcode(barcode);
        if (normalizedBarcode.isBlank()) {
            return Optional.empty();
        }

        try {
            JsonNode response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("isbn", normalizedBarcode)
                            .queryParam("limit", 1)
                            .build())
                    .header("User-Agent", "BibliotheekAtheneumBoom/1.0")
                    .retrieve()
                    .body(JsonNode.class);

            JsonNode firstBook = response == null ? null : response.path("docs").path(0);
            if (firstBook == null || firstBook.isMissingNode() || firstBook.isEmpty()) {
                return Optional.empty();
            }

            List<String> authors = new ArrayList<>();
            firstBook.path("author_name").forEach(author -> authors.add(author.asText()));
            String author = String.join(", ", authors);
            Integer publicationYear = firstBook.has("first_publish_year")
                    ? firstBook.get("first_publish_year").asInt()
                    : null;

            return Optional.of(new BookMetadataDTO(
                    normalizedBarcode,
                    textOrNull(firstBook, "title"),
                    author.isBlank() ? null : author,
                    publicationYear,
                    "Open Library"
            ));
        } catch (RestClientException exception) {
            return Optional.empty();
        }
    }

    private String normalizeBarcode(String barcode) {
        return barcode == null
                ? ""
                : barcode.trim().replaceAll("[^0-9Xx]", "").toUpperCase();
    }

    private String textOrNull(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.asText();
    }
}
