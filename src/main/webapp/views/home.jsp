<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="components/NavBar.jsp" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container mt-5">
    <div class="jumbotron">
        <h1 class="display-4">Welcome to Spring Boot JWT Demo</h1>
        <p class="lead">This is a simple application demonstrating JWT authentication with Spring Boot 3.</p>
        <hr class="my-4">
        <p>Use the navigation links to explore public and private pages.</p>
        <sec:authorize access="isAnonymous()">
            <a class="btn btn-primary btn-lg" href="/auth/login" role="button">Login to Access Private Content</a>
        </sec:authorize>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
