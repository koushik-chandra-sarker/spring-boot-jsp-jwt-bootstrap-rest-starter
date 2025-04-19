<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Access Denied</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="/">JWT Demo</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/public">Public Page</a>
                </li>
            </ul>
            <div class="d-flex">
                <c:choose>
                    <c:when test="${not empty username}">
                        <span class="navbar-text me-3">Welcome, ${username}!</span>
                        <a href="/auth/logout" class="btn btn-outline-light">Logout</a>
                    </c:when>
                    <c:otherwise>
                        <a href="/auth/login" class="btn btn-outline-light me-2">Login</a>
                        <a href="/auth/register" class="btn btn-light">Register</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>

<div class="container mt-5">
    <div class="card">
        <div class="card-header bg-warning text-dark">
            <h4>Access Denied</h4>
        </div>
        <div class="card-body">
            <div class="text-center mb-4">
                <i class="bi bi-shield-exclamation" style="font-size: 4rem; color: #dc3545;"></i>
            </div>
            <h5 class="card-title text-center">You don't have permission to access this page</h5>
            <p class="card-text text-center">
                Sorry, you don't have the necessary permissions to view the requested content.
            </p>
            <div class="d-flex justify-content-center mt-4">
                <a href="/" class="btn btn-primary me-2">Go to Home</a>
                <a href="/auth/login" class="btn btn-outline-primary">Login with different account</a>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
