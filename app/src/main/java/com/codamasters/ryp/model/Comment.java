package com.codamasters.ryp.model;

public class Comment {

    private String message;
    private String author_id;
    private String author_name;
    private Long timestamp;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public Comment() {
    }

    public Comment(String message, String author_id, String author_name, Long timestamp) {
        this.message = message;
        this.author_id = author_id;
        this.author_name = author_name;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public Long getTimestamp() {
        return timestamp;
    }


}
