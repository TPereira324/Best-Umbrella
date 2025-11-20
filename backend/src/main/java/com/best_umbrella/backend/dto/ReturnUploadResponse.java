package com.best_umbrella.backend.dto;

public class ReturnUploadResponse {
    private boolean success;
    private String message;
    private Long returnId;
    private String imageUrl;

    public ReturnUploadResponse() {}

    public ReturnUploadResponse(boolean success, String message, Long returnId, String imageUrl) {
        this.success = success;
        this.message = message;
        this.returnId = returnId;
        this.imageUrl = imageUrl;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getReturnId() { return returnId; }
    public void setReturnId(Long returnId) { this.returnId = returnId; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}