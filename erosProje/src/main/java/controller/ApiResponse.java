package controller;

public class ApiResponse<T> {
    private T data;
    private String message;
    private int status;
    private long timestamp;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    private ApiResponse(){
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> ApiResponse<T> success(T data){
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.data = data;
        apiResponse.status = 200;
        apiResponse.message = "Success";

        return apiResponse;
    }
    public static <T> ApiResponse<T> success(String message){
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.data = null;
        apiResponse.message = message;
        apiResponse.status = 200;

        return apiResponse;
    }
    public static <T> ApiResponse<T> error(String message,int code){
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.data = null;
        apiResponse.message = message;
        apiResponse.status = code;

        return apiResponse;
    }


}
