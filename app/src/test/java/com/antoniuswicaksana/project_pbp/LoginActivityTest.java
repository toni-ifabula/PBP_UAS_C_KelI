package com.antoniuswicaksana.project_pbp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginActivityTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void login() throws Exception {
        String email = "qwe@gmail.com",
                password = "qweqwe",
                output,
                expected = "login berhasil";

        LoginActivity loginActivity = new LoginActivity();

//        assertArrayEquals(expected, output, );
    }
}