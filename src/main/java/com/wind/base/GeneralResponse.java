package com.wind.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Format of response returned to client
 *
 * @param <T>
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralResponse<T> {

  @JsonProperty("status")
  private ResponseStatus status;

  @JsonProperty("data")
  private T data;

  public GeneralResponse(T data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "{" + "status=" + status +
        ", data=" + data.toString() +
        '}';
  }
}