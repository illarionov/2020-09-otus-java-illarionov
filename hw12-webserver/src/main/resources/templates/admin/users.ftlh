<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Пользователи</title>
    <link rel="stylesheet" type="text/css" href="/style.css">
</head>

<body>

<#include "/_include_menu.ftlh">

<h2>Страница администратора</h2>

<h3>Пользователи</h3>
<div>
    <a href="/admin/user/create">Добавить пользователя</a>
</div>
<br/>

<table>
    <thead>
    <tr>
        <td style="width: 50px">Id</td>
        <td style="width: 100px">Логин</td>
        <td style="width: 100px">Пароль</td>
        <td style="width: 100px">Роль</td>
        <td style="width: 150px">Имя</td>
        <td style="width: 150px">Возраст</td>
        <td style="width: 150px">Адрес</td>
        <td style="width: 200px">Телефоны</td>
    </tr>
    </thead>
    <#list users>
    <tbody>
        <#items as user>
        <tr>
            <td>${user.id}</td>
            <td>${user.login}</td>
            <td>${user.password}</td>
            <td>${user.role?switch("ADMIN", "Администратор", "USER", "Пользователь", user.role)}</td>
            <td nowrap>${user.name}</td>
            <td>${(user.age)!("Не&nbsp;указан"?no_esc)}</td>
            <td nowrap>${(user.address.street)!""}</td>
            <td>${user.phones?map(phone -> phone.number)?join(", ")}</td>
        </tr>
        </#items>
    </tbody>
    </#list>
</table>

</body>
</html>
