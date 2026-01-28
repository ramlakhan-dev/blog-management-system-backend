package com.rl.blogmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogResponse {

    private Long id;
    private String title;
    private String content;
    private String authorName;
}
