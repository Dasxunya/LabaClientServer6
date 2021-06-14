package ru.dasxunya.other;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class ServerResponse {
    private String command;

    @Builder.Default
    private String error = "";
    @Builder.Default
    private String message = "";
}
