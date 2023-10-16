<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>로그인</title>
</head>
<body>
<form method="post" action="/auth/login" id="login_form">
    <input type="text" id="id" name="id" placeholder="아이디">
    <br/>
    <input type="password" id="pwd" name="pwd" maxlength="64" placeholder="비밀번호">
    <br/>
    <button type="submit">로그인</button>
</form>
<script>
    const form = document.getElementById('login_form');
    var token = "";
    form.addEventListener('submit', e => {
        e.preventDefault();

        const data = new FormData(form);
        const param = JSON.stringify(Object.fromEntries(data));

        fetch('/auth/login', {
            method: 'POST',
            body: param,
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                debugger;
                if (response.status == 200) {
                    document.location= "/view/dashboard";
                } else {
                    alert("로그인 실패")
                }
            })
            .catch(error => console.log(error))
        function testLogin(){
            fetch('/view/dashboard', {
                method: 'get',
                headers: {
                    'Authorization': sessionStorage.getItem("jwt-access-token"),
                    'RefreshToken': sessionStorage.getItem("jwt-refresh-token"),
                    'Content-Type': 'application/json'
                }
            }).then((json)=>{
                console.log(json);
            }).catch(err => console.log(err))
        }

    });

</script>
</body>
</html>

