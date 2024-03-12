<!DOCTYPE html>
<html>
<head>
    <title>BUYSELL</title>
</head>
<body>
    <h3>Имя пользователя: <i>${user.name}</i></h3>
    <h3>Почта: <i>${user.email}</i></h3>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <input type="submit" value="Выйти"/>
    </form>

    <h4>att</h4>
    <#list attempts as attempt>
        <div>
            <p>${attempt.x} ${attempt.y} ${attempt.r} ${attempt.hit} | <a href="/attempt/${attempt.id}">Подробнее...</a></p>
        </div>
    </#list>
    <hr>

    <h3>Создать новый товар</h3>
    <form action="/attempt/create" method="post">
        x: <input type="number" name="x"/><br><br>
        y: <input type="number" name="y"/><br><br>
        z: <input type="number" name="r"/><br><br>
        hit: <input type="number" name="hit"/><br><br>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <input type="submit" value="Добавить товар"/>
    </form>

</body>
</html>