package ru.sber.appointment.model;

public class JwtResponse {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;
    private User user;

    public JwtResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public JwtResponse() {
    }

    public String getType() {
        return type;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
