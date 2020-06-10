package net.thumbtack.ptpb.handler.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.NoArgsConstructor;

@JsonSerialize
@NoArgsConstructor
public class EmptyResponse implements Response {

}
