package com.antoniuswicaksana.project_pbp.UnitTestRegister;

public interface RegisterView {
    String getEmail();
    String getPassword();
    String getNama();
    String getAlamat();
    String getTelp();
    void showEmailError(String message);
    void showPasswordError(String message);
    void showNamaError(String message);
    void showAlamatError(String message);
    void showTelpError(String message);
    void showLoginError(String message);
    void showErrorResponse(String message);
}
