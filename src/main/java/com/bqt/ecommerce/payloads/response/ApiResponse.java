package com.bqt.ecommerce.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
//We can use the @JsonPropertyOrder annotation to specify the order of properties on serialization.
@JsonPropertyOrder({
        "success",
        "message"
})
public class ApiResponse implements Serializable {

    // We can add the @JsonProperty annotation to indicate the property name in JSON.
    @JsonProperty("success")
    private boolean success;

    @JsonProperty("message")
    private String message;

    // In contrast, the @JsonIgnore annotation is used to mark a property to be ignored at the field level.
    @JsonIgnore
    private HttpStatus status;

}
