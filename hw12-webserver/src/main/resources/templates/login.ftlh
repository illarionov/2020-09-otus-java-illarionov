<html xmlns="http://www.w3.org/1999/xhtml" content="text/html;">
<head>
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="/style.css">
    <link rel="stylesheet" type="text/css" href="/form.css">
</head>
<body>

<#include "/_include_menu.ftlh">

<div class="top">
    <img src="img/login.png">
    <br/>

    <h3>Вход</h3>

    <#list messages>
        <ul class="error">
            <#items as message>
                <li>${message}</li>
            </#items>
        </ul>
    </#list>

    <form action="login" method="post">
        <ul class="form-style-1">
            <li>
                <label for="login">Логин</label>
                <input type="text" name="login" class="field-long" value="${form.login}">
            </li>

            <li>
                <label for="password">Пароль</label>
                <input type="password" name="password" class="field-long">
            </li>

            <li>
                <input type="submit" value="Перейти к списку пользователей">
            </li>
        </ul>
    </form>
</div>
</body>
</html>