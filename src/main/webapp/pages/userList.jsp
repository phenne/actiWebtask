<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="data.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="/src/lib/css/bootstrap.css">
    <script src="/src/lib/jquery-3.2.1.js"></script>
    <script src="/src/lib/bootstrap.min.js"></script>
    <script src="/src/User.js"></script>
    <script src="/src/RequestSender.js"></script>
    <script src="/src/userlist/UserListPageActions.js"></script>
    <script src="/src/validationService.js"></script>
    <script src="/src/modal/CreateEditModalFields.js"></script>
    <script src="/src/modal/CreateEditModal.js"></script>
    <script src="/src/modal/DeleteModal.js"></script>
    <script src="/src/modalstrategy/CreateUserStrategy.js"></script>
    <script src="/src/ErrorHandlers.js"></script>
    <script src="/src/modalstrategy/EditUserStrategy.js"></script>
    <script src="/src/Main.js"></script>
    <title>Title</title>
</head>
<body>
<div class="container" id="mainContainer" style="margin-top: 10px">
    <div id="successAlert" hidden class="alert alert-success alert-dismissable fade in">
        <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
        <strong>Success!</strong> The operation done successfully.
    </div>
    <div class="row">
        <table class="table">
            <tr>
                <td>You have logged in as
                    <strong>
                        <output id="thisUsername"></output>
                    </strong>
                </td>
                <td>
                    <form action="/logout" method="post">
                        <input class="btn" type="submit" value="Log out">
                    </form>
                </td>
            </tr>
        </table>
    </div>
    <div class="row">
        <table class="table" id="userListTable">
            <thead>
            <tr>
                <th>First name</th>
                <th>Second name</th>
                <th>Username</th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>
    <div id="bottomButtons" class="row">
        <input class="btn" type="submit" value="Refresh">
    </div>
</div>

<div id="generalModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 id="modalTitle" class="modal-title">Create new user</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label class="control-label col-sm-4" for="username">Username:</label>
                        <div class="col-sm-8">
                            <input class="form-control" type="text" id="username" value="hello">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-4" for="firstName">First name:</label>
                        <div class="col-sm-8">
                            <input class="form-control" type="text" id="firstName">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-4" for="lastName">Last name:</label>
                        <div class="col-sm-8">
                            <input class="form-control" type="text" id="lastName">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-4" for="password">Password:</label>
                        <div class="col-sm-8">
                            <input class="form-control" type="password" id="password">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-4" for="confirmPassword">Confirm password:</label>
                        <div class="col-sm-8">
                            <input class="form-control" type="password" id="confirmPassword">
                        </div>
                    </div>
                    <div class="checkbox">
                        <label class="control-label col-sm-4">Manager</label>
                        <div class="col-sm-8">
                            <input type="checkbox" id="manager">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-offset-4">
                            <output id="crModalErrorOutput"></output>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="submit" class="btn btn-success" id="requestButton">Create</button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="deleteModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Delete user</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                Are you sure you want to delete user <output style="display:inline" id="deleteUsername"></output>?
                <output id="deleteModalOutputError"></output>
            </div>
            <div class="modal-footer">
                <button type="submit" id="confirmDeleteButton" class="btn" onclick="">Delete</button>
                <button type="button" data-dismiss="modal" class="btn">Cancel</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>