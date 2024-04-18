package org.konradchrzanowski.utils.dictionary.dto;

import lombok.Builder;

import java.math.BigDecimal;


@Builder
public record DictionaryDTO(Long id, String code, String value, String language, Long extraId, BigDecimal extraPrice,
                            String extraString) {
}

