<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Пользователи</title>
    <link rel="stylesheet" type="text/css" href="/style.css">
    <style>
        table.user th, td {
            text-align: left;
        }
    </style>
</head>

<body>

<#include "/_include_menu.ftlh">

<h2>Страница пользователя</h2>

<table class="user">
    <tr>
        <th>Id:</th>
        <td>${user.id}</td>
    </tr>
    <tr>
        <th>Логин:</th>
        <td>${user.login}</td>
    </tr>
    <tr>
        <th>Пароль:</th>
        <td>${user.password}</td>
    </tr>
    <tr>
        <th>Роль:</th>
        <td>${user.role?switch("ADMIN", "Администратор", "USER", "Пользователь", user.role)}</td>
    </tr>
    <tr>
        <th>Имя:</th>
        <td>${user.name}</td>
    </tr>
    <tr>
        <th>Возраст:</th>
        <td>${(user.age)!("Не&nbsp;указан"?no_esc)}</td>
    </tr>
    <tr>
        <th>Адрес:</th>
        <td>${(user.address.street)!""}</td>
    </tr>
    <tr>
        <th>Телефоны:</th>
        <td>${user.phones?map(phone -> phone.number)?join("<br/>")}</td>
    </tr>
</table>

