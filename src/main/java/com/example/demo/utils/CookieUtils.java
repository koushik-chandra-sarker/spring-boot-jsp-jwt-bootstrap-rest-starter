package com.example.demo.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtils {
    
    public static void addCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Set to false in dev if not using HTTPS
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        if (request.getCookies() == null) return;

        for (Cookie cookie : request.getCookies()) {
            if (name.equals(cookie.getName())) {
                Cookie deleteCookie = new Cookie(name, null);
                deleteCookie.setPath("/");
                deleteCookie.setMaxAge(0);
                deleteCookie.setHttpOnly(true);
                deleteCookie.setSecure(true);
                response.addCookie(deleteCookie);
                break;
            }
        }
    }
    
    public static void deleteAllCookies(HttpServletRequest request, HttpServletResponse response) {
        if (request.getCookies() == null) return;

        for (Cookie cookie : request.getCookies()) {
            Cookie deleteCookie = new Cookie(cookie.getName(), null);
            deleteCookie.setPath("/");
            deleteCookie.setMaxAge(0);
            deleteCookie.setHttpOnly(true);
            deleteCookie.setSecure(true);
            response.addCookie(deleteCookie);
        }
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
