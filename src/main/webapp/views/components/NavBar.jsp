
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="currentUri" value="${pageContext.request.requestURI}" />

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="/home">JWT Demo</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link active" href="/home">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/public">Public Page</a>
                </li>
                <sec:authorize access="isAuthenticated()">
                    <li class="nav-item">
                        <a class="nav-link" href="/private">Private Page</a>
                    </li>
                </sec:authorize>
            </ul>
            <div class="d-flex">
                <sec:authorize access="isAuthenticated()">
                    <span class="navbar-text me-3">Welcome, ${username}!</span>
                    <a href="/auth/logout" class="btn btn-outline-light">Logout</a>
                </sec:authorize>
                <sec:authorize access="isAnonymous()">
                    <c:if test="${currentUri != '/views/login.jsp'}">
                        <a href="/auth/login" class="btn btn-outline-light me-2">Login</a>
                    </c:if>
                    <a href="/auth/register" class="btn btn-light">Register</a>
                </sec:authorize>
            </div>
        </div>
    </div>
</nav>
