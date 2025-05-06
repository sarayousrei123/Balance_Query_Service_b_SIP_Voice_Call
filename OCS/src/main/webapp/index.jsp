<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome to Your Balance Project</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #f0f2f5;
        }
        .welcome-container {
            text-align: center;
            padding: 2rem;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 {
            color: #1a73e8;
            margin-bottom: 1rem;
        }
        .login-link {
            display: inline-block;
            margin-top: 1rem;
            padding: 0.5rem 1rem;
            background-color: #1a73e8;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        .login-link:hover {
            background-color: #1557b0;
        }
    </style>
</head>
<body>
    <div class="welcome-container">
        <h1>Welcome to Your Balance Project</h1>
        <p>Manage your balance and transactions with ease</p>
        <a href="login.jsp" class="login-link">Login</a>
    </div>
</body>
</html> 