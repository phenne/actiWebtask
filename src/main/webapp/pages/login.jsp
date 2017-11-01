<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="/src/lib/css/bootstrap.css">
    <script src="/src/lib/jquery-3.2.1.min.js"></script>
    <script src="/src/lib/bootstrap.min.js"></script>
    <title>Title</title>
</head>
<body>
<form class="form-horizontal" action="/login" method="post" style="margin-top: 20px;">
    <div class="form-group">
        <label class="control-label col-sm-2" for="username">Username:</label>
        <div class="col-sm-2">
            <input type="text" class="form-control" name="username" id="username">
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2" for="password">Password:</label>
        <div class="col-sm-2">
            <input type="password" class="form-control" name="password" id="password">
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-2">
            <input type="submit" value="Log in" class="btn btn-default"><br>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-2">
            <output name="ifWrong">
                <%
                    if (request.getAttribute("fail") != null) {
                        out.print(request.getAttribute("fail"));
                    }
                %>
            </output>
        </div>
    </div>
</form>
</body>
</html>