<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Public Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="components/NavBar.jsp" />

<div class="container mt-5">
    <div class="card">
        <div class="card-header bg-success text-white">
            <h4>Public Content</h4>
        </div>
        <div class="card-body">
            <h5 class="card-title">This is a public page</h5>
            <p class="card-text">This content is accessible to all users, whether they are logged in or not.</p>
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla quam velit, vulputate eu pharetra nec, mattis ac neque. Duis vulputate commodo lectus, ac blandit elit tincidunt id.</p>
            <p>Sed convallis tristique sem. Proin ut ligula vel nunc egestas porttitor. Morbi lectus risus, iaculis vel, suscipit quis, luctus non, massa. Fusce ac turpis quis ligula lacinia aliquet.</p>

            <sec:authorize access="isAnonymous()">
                <div class="alert alert-info mt-3">
                    <p><strong>Note:</strong> To access the private content, you need to <a href="/auth/login">login</a> first.</p>
                </div>
            </sec:authorize>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
