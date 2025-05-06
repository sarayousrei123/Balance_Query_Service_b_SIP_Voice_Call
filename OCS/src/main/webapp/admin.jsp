<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            color: #333;
            margin: 0;
            padding: 0;
        }
        .header {
            background-color: #2c3e50;
            color: white;
            padding: 1rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .container {
            width: 80%;
            margin: 2rem auto;
            padding: 20px;
            background: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        h1 {
            text-align: center;
            color: #2c3e50;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #2c3e50;
            color: white;
        }
        .form-container {
            margin: 20px 0;
            padding: 10px;
            background: #f9f9f9;
            border-radius: 8px;
            border: 1px solid #ddd;
        }
        label {
            display: inline-block;
            margin-right: 10px;
        }
        input[type="text"], input[type="submit"] {
            padding: 10px;
            margin: 5px 0;
        }
        input[type="submit"] {
            background-color: #3498db;
            color: white;
            border: none;
            cursor: pointer;
            border-radius: 4px;
        }
        input[type="submit"]:hover {
            background-color: #2980b9;
        }
        .section-title {
            margin-top: 40px;
            color: #34495e;
        }
        .logout-btn {
            background-color: transparent;
            border: 1px solid white;
            color: white;
            padding: 0.5rem 1rem;
            border-radius: 4px;
            cursor: pointer;
        }
        .logout-btn:hover {
            background-color: rgba(255,255,255,0.1);
        }
    </style>
    <script>
        function confirmDelete() {
            return confirm('Are you sure you want to delete this user?');
        }
    </script>
</head>
<body>
    <div class="header">
        <h2>Welcome, <%= session.getAttribute("username") %></h2>
        <form action="/Balancer/LogoutServlet" method="post">
            <button type="submit" class="logout-btn">Logout</button>
        </form>
    </div>

    <div class="container">
        <h1>Admin Dashboard</h1>

        <%-- show error massage--%>
        <%
            String success = request.getParameter("success");
            String error = request.getParameter("error");
            if (success != null) {
        %>
            <div style="color: green; text-align: center; margin: 10px 0; padding: 10px; background-color: #dff0d8; border-radius: 4px;">
                <%= success %>
            </div>
        <%
            }
            if (error != null) {
        %>
            <div style="color: red; text-align: center; margin: 10px 0; padding: 10px; background-color: #f2dede; border-radius: 4px;">
                <%= error %>
            </div>
        <%
            }
        %>

        <!-- List -->
        <h2 class="section-title">Users List</h2>
        <form action="/Balancer/admin_dashboard" method="get" class="form-container">
            <input type="submit" value="Show Users">
        </form>

        <%-- عرض جدول المستخدمين --%>
        <%
            List<String[]> users = (List<String[]>) request.getAttribute("users");
            if (users != null && !users.isEmpty()) {
        %>
            <table>
                <tr>
                    <th>Phone Number</th>
                    <th>Username</th>
                    <th>Balance</th>
                    <th>Role</th>
                </tr>
                <%
                    for (String[] user : users) {
                %>
                    <tr>
                        <td><%= user[0] %></td>
                        <td><%= user[1] %></td>
                        <td><%= user[2] %></td>
                        <td><%= user[3] %></td>
                    </tr>
                <%
                    }
                %>
            </table>
        <%
            } else if (users != null) {
        %>
            <p style="text-align: center; color: #666;">No users found</p>
        <%
            }
        %>

        <!-- Delete -->
        <h2 class="section-title">Delete User</h2>
        <form action="/Balancer/deleteUser" method="get" class="form-container" onsubmit="return confirmDelete()">
            <label for="deleteUsername">Username:</label>
            <input type="text" id="deleteUsername" name="username" required>
            <input type="submit" value="Delete User">
        </form>

        <!-- Edit-->
        <h2 class="section-title">Edit User</h2>
        <form action="/Balancer/EditUserServlet" method="post" class="form-container">
            <div style="margin-bottom: 15px;">
                <label for="editUsername">Username:</label>
                <input type="text" id="editUsername" name="username" required>
            </div>
            <div style="margin-bottom: 15px;">
                <label for="editMsisdn">New Phone Number:</label>
                <input type="text" id="editMsisdn" name="newMsisdn" required>
            </div>
            <div style="margin-bottom: 15px;">
                <label for="editBalance">New Balance:</label>
                <input type="text" id="editBalance" name="newBalance" required>
            </div>
            <div style="margin-bottom: 15px;">
                <label for="editPassword">New Password:</label>
                <input type="password" id="editPassword" name="newPassword" required>
            </div>
            <input type="submit" value="Update User">
        </form>

        <!--  Create -->
        <h2 class="section-title">Create New User</h2>
        <form action="/Balancer/CreateUserServlet" method="post" class="form-container">
            <div style="margin-bottom: 15px;">
                <label for="newMsisdn">Phone Number:</label>
                <input type="text" id="newMsisdn" name="msisdn" required 
                       pattern="[0-9]+" title="Please enter numbers only">
            </div>
            <div style="margin-bottom: 15px;">
                <label for="newUsername">Username:</label>
                <input type="text" id="newUsername" name="username" required>
            </div>
            <div style="margin-bottom: 15px;">
                <label for="newPassword">Password:</label>
                <input type="password" id="newPassword" name="password" required>
            </div>
            <div style="margin-bottom: 15px;">
                <label for="newBalance">Initial Balance:</label>
                <input type="text" id="newBalance" name="balance" required 
                       pattern="[0-9]+(\.[0-9]+)?" title="Please enter a valid number">
            </div>
            <div style="margin-bottom: 15px;">
                <label for="newRole">Role:</label>
                <select id="newRole" name="role" required>
                    <option value="user">User</option>
                    <option value="admin">Admin</option>
                </select>
            </div>
            <input type="submit" value="Create New User" style="width: 100%;">
        </form>
    </div>
</body>
</html>