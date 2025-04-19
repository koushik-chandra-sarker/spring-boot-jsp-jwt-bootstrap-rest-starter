<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register</title>
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
                <a href="/auth/login" class="btn btn-outline-light me-2">Login</a>
                <a href="/auth/register" class="btn btn-light active">Register</a>
            </div>
        </div>
    </div>
</nav>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    <h4>Register</h4>
                </div>
                <div class="card-body">
                    <form:form action="/auth/register" method="post" modelAttribute="user">
                        <div class="mb-3">
                            <label for="username" class="form-label">Username</label>
                            <form:input path="username" id="username" class="form-control" required="true" />
                            <form:errors path="username" cssClass="text-danger" />
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <form:input path="email" type="email" id="email" class="form-control" required="true" />
                            <form:errors path="email" cssClass="text-danger" />
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">Password</label>
                            <form:password path="password" id="password" class="form-control" required="true" />
                            <form:errors path="password" cssClass="text-danger" />
                        </div>
                        <button type="submit" class="btn btn-primary">Register</button>
                    </form:form>
                    <div class="mt-3">
                        <p>Already have an account? <a href="/auth/login">Login here</a></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>