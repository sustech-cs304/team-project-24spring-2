package cn.edu.sustech.ces.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreationResponse {
    private Boolean success;
    private String message;
    public CreationResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
