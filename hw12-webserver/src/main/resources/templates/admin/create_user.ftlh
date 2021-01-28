<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Создать пользователя</title>
    <link rel="stylesheet" type="text/css" href="/style.css">
    <link rel="stylesheet" type="text/css" href="/form.css">
</head>

<body>

<#include "/_include_menu.ftlh">

<h2>Страница администратора</h2>

<h3>Создать пользователя</h3>

<#list messages>
    <ul class="error">
        <#items as message>
            <li>${message}</li>
        </#items>
    </ul>
</#list>

<form action="/admin/user/create" method="post">
    <ul class="form-style-1">
        <li>
            <label for="login">Логин<span class="required">*</span></label>
            <input type="text" id="login" name="login" class="field-long" value="${form.login}">
        </li>

        <li>
            <label for="password">Пароль<span class="required">*</span></label>
            <input type="password" id="password" name="password" class="field-long" value="${form.password}">
        </li>

        <li>
            <label for="role">Роль</label>
            <select id="role" name="role" class="field-select">
                <option value="USER" <#if form.role.name() == "USER">selected</#if>>Пользователь</option>
                <option value="ADMIN" <#if form.role.name() == "ADMIN">selected</#if>>Администратор</option>
            </select>
        </li>

        <li>
            <label for="name">Имя<span class="required">*</span></label>
            <input type="text" id="name" name="name" class="field-long" value="${form.name}">
        </li>

        <li>
            <label for="age">Возраст</label>
            <input type="text" id="age" name="age" class="field-long" value="${form.age}">
        </li>

        <li>
            <label for="address">Адрес</label>
            <input type="text" id="address" name="address" class="field-long" value="${form.address}">
        </li>

        <li>
            <label for="phone">Телефон</label>
            <input type="text" id="phone" name="phone" class="field-long" value="${form.phone}">
        </li>

        <li>
            <input type="submit" value="Создать">
        </li>
    </ul>
</form>
</body>
</html>