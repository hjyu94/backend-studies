package me.hjeong.userservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // null 인 경우 json 필드에 포함하지 않는다.
public class ResponseUser {

    private String email;
    private String name;
    private String userId;
    private List<ResponseOrder> orders;

}
