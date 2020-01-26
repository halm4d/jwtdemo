package com.davidhalma.jwtdemo.onboarding.model.db;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Data
public class BaseModel {

    @CreatedDate
    private Date createdAt = new Date();

}
