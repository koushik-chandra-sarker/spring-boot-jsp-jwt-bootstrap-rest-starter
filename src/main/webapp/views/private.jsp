<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Private Page</title>
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
                <li class="nav-item">
                    <a class="nav-link active" href="/private">Private Page</a>
                </li>
            </ul>
            <div class="d-flex">
                <span class="navbar-text me-3">Welcome, ${username}!</span>
                <a href="/auth/logout" class="btn btn-outline-light">Logout</a>
            </div>
        </div>
    </div>
</nav>

<div class="container mt-5">
    <div class="card">
        <div class="card-header bg-primary text-white">
            <h4>Private Content</h4>
        </div>
        <div class="card-body">
            <div class="alert alert-success">
                <h5>Authentication Successful!</h5>
                <p>You are logged in as <strong>${username}</strong> and can access protected content.</p>
            </div>

            <h5 class="card-title mt-4">Your Secure User Information</h5>
            <div id="userInfo" class="mt-3">
                <div class="d-flex justify-content-center">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                </div>
            </div>

            <h5 class="card-title mt-4">Protected Content</h5>
            <p class="card-text">This is a private page that only authenticated users can access.</p>
            <p>Sed consectetur ornare ipsum. Curabitur feugiat elit ut tellus ultrices vestibulum.</p>
            <p>Proin tincidunt malesuada nibh, vel sollicitudin arcu varius id.</p>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        fetchUserInfo();

        document.getElementById('refreshTokenBtn').addEventListener('click', refreshToken);
    });

    function fetchUserInfo() {
        fetch('/api/user/info', {
            credentials: 'include'
        })
            .then(response => {
                if (response.ok) return response.json();
                else if (response.status === 401) return refreshToken(true).then(fetchUserInfo);
                else throw new Error('Failed to fetch user info');
            })
            .then(data => {
                const userInfoDiv = document.getElementById('userInfo');
                userInfoDiv.innerHTML = `
                <div class="card">
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <p><strong>ID:</strong> ${'${'}data.id}</p>
                                <p><strong>Username:</strong> ${'${'}data.username}</p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Email:</strong> ${'${'}data.email}</p>
                                <p><strong>Roles:</strong> ${'${'}data.roles.join(', ')}</p>
                            </div>
                        </div>
                    </div>
                </div>
            `;
            })
            .catch(error => {
                console.error('Error:', error);
                document.getElementById('userInfo').innerHTML = `
                <div class="alert alert-danger">
                    Failed to load user information. Please try logging in again.
                </div>
            `;
            });
    }

    function refreshToken(silent = false) {
        return fetch('/auth/refreshtoken', {
            method: 'POST',
            credentials: 'include'
        })
            .then(response => {
                if (!response.ok) throw new Error('Failed to refresh token');
                return response.json();
            })
            .then(data => {
                if (!silent) {
                    document.getElementById('tokenStatus').innerHTML = `
                    <div class="alert alert-success">Token refreshed successfully!</div>
                `;
                }
            })
            .catch(error => {
                console.error('Refresh error:', error);
                if (!silent) {
                    document.getElementById('tokenStatus').innerHTML = `
                    <div class="alert alert-danger">Failed to refresh token. Please log in again.</div>
                `;
                }
            });
    }
</script>
</body>
</html>
