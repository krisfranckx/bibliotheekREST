package be.atheneumboom.bibliotheek.model.DTO;

public record BookMetadataDTO(
        String barcode,
        String titel,
        String auteur,
        Integer publicatiejaar,
        String bron
) {
}
