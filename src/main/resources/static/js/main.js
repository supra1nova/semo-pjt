let result = null;

async function requestKakaoUserInfo(){
    const kakaoUserInfoUrl = "http://semo.com/api/login-out/kakao-login/user-info";
    const data = await fetch(kakaoUserInfoUrl, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        }
    })
        .then(res => res.json())
        .then(data => data);
    if(data.message === "OK"){
        result = data.data;
        const newContent = document.createTextNode(result.email + "님 안녕하세요");
        let testDiv = document.getElementById("testDiv");
        testDiv.appendChild(newContent)
        testDiv.hidden = false;
    }
}

async function requestNaverUserInfo(){
    const naverUserInfoUrl = "/api/login-out/naver-login/user-info";
    const data = await fetch(naverUserInfoUrl)
        .then(res => res.json())
        .then(data => data);
    if(data.message === "OK"){
        result = data.data;
        const newDiv = document.createElement("newDiv");
        const newContent = document.createTextNode(result.email + "님 안녕하세요");
        newDiv.appendChild(newContent);
        const currentDiv = document.getElementById("div1");
        document.body.insertBefore(newDiv, currentDiv);
    };
}

function loginChecking(){
    requestKakaoUserInfo();
    requestNaverUserInfo();
}

loginChecking();
sessionStorage.set
